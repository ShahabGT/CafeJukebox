package ir.cafejukebox.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import ir.cafejukebox.app.R;
import ir.cafejukebox.app.classes.MyPreference;

public class SplashActivity extends AppCompatActivity {

    private MaterialButton user,cafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init(){
        if(MyPreference.getInstance(this).getIsLogin()){
            new Handler().postDelayed(()->{
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            },2500);
        }else {
            user = findViewById(R.id.splash_user);
            cafe = findViewById(R.id.splash_cafe);
            user.setVisibility(View.VISIBLE);
            cafe.setVisibility(View.VISIBLE);

            onClicks();
        }
    }

    private void onClicks(){
        user.setOnClickListener(w->{
            Intent  i =new Intent(SplashActivity.this,LoginActivity.class);
            i.putExtra("which","user");
            startActivity(i);
            SplashActivity.this.finish();
        });
        cafe.setOnClickListener(w->{
            Intent  i =new Intent(SplashActivity.this,LoginActivity.class);
            i.putExtra("which","cafe");
            startActivity(i);
            SplashActivity.this.finish();
        });
    }
}
