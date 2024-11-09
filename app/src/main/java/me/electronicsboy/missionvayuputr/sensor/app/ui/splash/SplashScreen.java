package me.electronicsboy.missionvayuputr.sensor.app.ui.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import me.electronicsboy.missionvayuputr.sensor.app.R;
import me.electronicsboy.missionvayuputr.sensor.app.callbacks.IAfterSplashScreenLoadCallback;
import me.electronicsboy.missionvayuputr.sensor.app.ui.LoginActivity;
import me.electronicsboy.missionvayuputr.sensor.app.util.TempStorage;
import me.electronicsboy.missionvayuputr.sensor.app.util.TimeUtil;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (TempStorage.get("SPLASH_SCREEN_CALLBACK") != null) {
            ((IAfterSplashScreenLoadCallback) TempStorage.get("SPLASH_SCREEN_CALLBACK")).callback(this);
            finish();
        } else {
            TimeUtil.delayMs(2500);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}