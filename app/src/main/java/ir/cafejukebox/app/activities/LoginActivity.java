package ir.cafejukebox.app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import ir.cafejukebox.app.R;
import ir.cafejukebox.app.classes.MyPreference;
import ir.cafejukebox.app.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    private static final int SMS_CONSENT_REQUEST = 365;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container,new LoginFragment()).commit();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task-> {
                if(task.isSuccessful() && task.getResult()!=null){
                    String fbToken = task.getResult().getToken();
                    MyPreference.getInstance(LoginActivity.this).setFbToken(fbToken);
                }


        });

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                assert data!=null;
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                assert message!=null;
                String oneTimeCode = message.substring(message.length()-14,message.length() - 8);
                Intent intent = new Intent("codeReceived");
                intent.putExtra("code", oneTimeCode);
                sendBroadcast(intent);
            }
        }
    }

    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                assert  extras!=null;
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                assert smsRetrieverStatus!=null;
                if (smsRetrieverStatus.getStatusCode() == CommonStatusCodes.SUCCESS) {
                    Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    try {
                        startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                    } catch (ActivityNotFoundException e) {
                    }
                }
            }
        }
    };
}
