package ir.cafejukebox.app.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ir.cafejukebox.app.R;
import ir.cafejukebox.app.classes.MyPreference;
import ir.cafejukebox.app.classes.Utils;
import ir.cafejukebox.app.data.RetrofitClient;
import ir.cafejukebox.app.models.GeneralResponse;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class LoginFragment extends Fragment {

    private static final int RESOLVE_HINT = 581;
    private TextInputEditText uNumber;
    private Context context;
    private FragmentActivity activity;
    private MaterialButton send;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_login, container, false);
        context = getContext();
        activity = getActivity();
        init(v);

        return v;
    }

    private void init(View v){
        uNumber = v.findViewById(R.id.login_number);
        send = v.findViewById(R.id.login_send);

        try {
            requestHint();
        } catch (Exception e) {
            e.printStackTrace();
        }

        onClicks();
    }

    private void onClicks(){

        send.setOnClickListener(w->{
            if(!Utils.checkInternet(context)){
                Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }else {
                assert  uNumber.getText()!=null;
                String n = uNumber.getText().toString().trim().replace(" ", "");
                if (n.isEmpty() || n.length() < 11 || !n.startsWith("09")) {
                    Toast.makeText(context, getString(R.string.number_error), Toast.LENGTH_SHORT).show();
                }else {
                    login(n);
                }

            }

        });


    }

    private void login(String number){
        send.setText("...");
        send.setEnabled(false);
        SmsRetriever.getClient(activity).startSmsUserConsent("98300077");
        RetrofitClient.getInstance().getApi()
                .login(number)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        send.setText(getString(R.string.login_xml_send));
                        send.setEnabled(true);
                        if(response.isSuccessful() && response.body()!=null){
                            if(response.body().getStatus().equals("success")){
                               // Toast.makeText(context, getString(R.string.welcome_error), Toast.LENGTH_SHORT).show();
                                MyPreference.getInstance(context).setNumber(number);
                                activity.getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.enter_left,R.anim.exit_right)
                                        .replace(R.id.login_container,new CodeFragment())
                                        .commit();
                            }else if(response.body().getMessage().equals("user blocked")){
                                Toast.makeText(context, getString(R.string.blocked_error), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        send.setText(getString(R.string.login_xml_send));
                        send.setEnabled(true);
                        Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void requestHint() throws Exception {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(context).getHintPickerIntent(hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                RESOLVE_HINT, null, 0, 0, 0,null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                    assert data != null;
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    assert credential != null;
                    String number = credential.getId();
                    if (number.startsWith("+98"))
                        number = "0" + number.substring(3);
                    else if (number.startsWith("0098"))
                        number = "0" + number.substring(4);
                    uNumber.setText(number);
            }
        }
    }
}
