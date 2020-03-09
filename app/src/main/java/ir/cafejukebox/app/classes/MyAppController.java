package ir.cafejukebox.app.classes;

import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.multidex.MultiDexApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;

import ir.cafejukebox.app.R;

public class MyAppController extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Fresco.initialize(this);
        FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs
        );
        FontRequestEmojiCompatConfig config = new FontRequestEmojiCompatConfig(this,fontRequest);
        config.setReplaceAll(true);
        EmojiCompat.init(config);

    }
}
