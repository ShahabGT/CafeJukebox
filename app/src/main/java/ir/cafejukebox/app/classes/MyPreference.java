package ir.cafejukebox.app.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreference {

    private static MyPreference instance=null;
    private SharedPreferences sp;
    private MyPreference(Context context){
        sp = context.getSharedPreferences("CafeJukebox", 0);
    }

    public static MyPreference getInstance(Context context){
        if(instance==null)
            instance = new MyPreference(context);

        return instance;
    }

    public void setIsLogin(){
        sp.edit().putBoolean("islogin",true).apply();
    }
    public boolean getIsLogin(){
        return sp.getBoolean("islogin",false);
    }

    public void setNumber(String number){
        sp.edit().putString("number",number).apply();
    }
    public String getNumber(){
        return sp.getString("number","");
    }

    public void setFbToken(String fbToken){
        sp.edit().putString("fbToken",fbToken).apply();
    }
    public String getFbToken(){
        return sp.getString("fbToken","");
    }
}
