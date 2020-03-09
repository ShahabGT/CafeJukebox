package ir.cafejukebox.app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;

import ir.cafejukebox.app.R;
import ir.cafejukebox.app.activities.MainActivity;
import ir.cafejukebox.app.classes.MyPreference;
import ir.cafejukebox.app.classes.Utils;
import ir.cafejukebox.app.data.RetrofitClient;
import ir.cafejukebox.app.models.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CodeFragment extends Fragment {

    private Context context;
    private FragmentActivity activity;
    private MaterialButton send,reSend;
    private TextInputEditText code;
    private CountDownTimer timer;
    private long timerCount=80000;
    private String number;


    public CodeFragment() {
    }

    private BroadcastReceiver rec = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            assert intent.getExtras()!=null;
            String c = intent.getExtras().getString("code");
            code.setText(c);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_code, container, false);

        context = getContext();
        activity=getActivity();

        context.registerReceiver(rec,new IntentFilter("codeReceived"));

        init(v);
        return v;
    }

    @Override
    public void onDestroy() {
        context.unregisterReceiver(rec);
        super.onDestroy();
    }

    private void init(View v){

        number = MyPreference.getInstance(context).getNumber();

        code = v.findViewById(R.id.code_code);
        send = v.findViewById(R.id.code_send);
        reSend = v.findViewById(R.id.code_resend);
        reSend.setEnabled(false);
        initTimer();
        onClicks();
    }
    private void initTimer(){
        timer = new CountDownTimer(timerCount,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reSend.setText(Utils.convertToTimeFormat(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                try {
                    reSend.setEnabled(true);
                    reSend.setText(getString(R.string.code_xml_resend));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        timer.start();
    }

    private void onClicks(){
        reSend.setOnClickListener(w->{
            if(!Utils.checkInternet(context)){
                Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }else{
                resendSMS();
            }
        });

        send.setOnClickListener(w->{
            if(!Utils.checkInternet(context)){
                Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }else{
                String c = code.getText().toString().trim().replace(" ","");
                if(c.length()<6 || c.startsWith("0"))
                    Toast.makeText(context, getString(R.string.code_error), Toast.LENGTH_SHORT).show();
                else
                    doAuth(c);
            }
        });
    }

    private void doAuth(String code){
        send.setEnabled(false);
        send.setText("...");
        String fbToken = MyPreference.getInstance(context).getFbToken();
        RetrofitClient.getInstance().getApi().verityCode(number,code,fbToken)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        send.setEnabled(true);
                        send.setText(getString(R.string.code_xml_send));
                        if(response.isSuccessful() && response.body()!=null){
                            if(response.body().getStatus().equals("success")){
                                MyPreference.getInstance(context).setAccessToken(response.body().getAccessToken());
                                MyPreference.getInstance(context).setUserId(response.body().getUserId());
                                if(response.body().getName()!=null && !response.body().getName().isEmpty()){
                                    MyPreference.getInstance(context).setName(response.body().getName());
                                    MyPreference.getInstance(context).setIsLogin();
                                    Intent in = new Intent(activity, MainActivity.class);
                                    startActivity(in);
                                    activity.finish();
                                    Toast.makeText(context, getString(R.string.code_welcome,response.body().getName()), Toast.LENGTH_SHORT).show();
                                }else{
                                    activity.getSupportFragmentManager().beginTransaction()
                                            .setCustomAnimations(R.anim.enter_left,R.anim.exit_right)
                                            .replace(R.id.login_container,new ProfileFragment())
                                            .commit();

                                }

                            }else if (response.body().getMessage().equals("Wrong code")){
                                Toast.makeText(context, getString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        send.setEnabled(true);
                        send.setText(getString(R.string.code_xml_send));
                        Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void resendSMS(){
        reSend.setEnabled(false);
        SmsRetriever.getClient(activity).startSmsUserConsent("98300077");
        RetrofitClient.getInstance().getApi()
                .login(number)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            if(response.body().getStatus().equals("success")) {
                                timerCount *= 2;
                                timer.cancel();
                                timer.start();
                            }else{
                                Toast.makeText(context, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                                reSend.setEnabled(true);
                            }


                        }else{
                            Toast.makeText(context, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                            reSend.setEnabled(true);


                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        Toast.makeText(context, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                        reSend.setEnabled(true);
                    }
                });
    }

}
