package com.example.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

// Singleton for app-wide config
class AppConfig {
    private static AppConfig instance;
    private String appName = "BMI Calculator";

    private AppConfig() { }

    public static AppConfig getInstance() {
        if (instance == null) instance = new AppConfig();
        return instance;
    }

    public String getAppName() { return appName; }
}

// Facade for splash screen setup
class SplashFacade {
    private AppCompatActivity activity;

    public SplashFacade(AppCompatActivity activity) { this.activity = activity; }

    public void showSplash(int delayMillis, Class<?> nextActivity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (activity.getSupportActionBar() != null) activity.getSupportActionBar().hide();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(activity, nextActivity);
            activity.startActivity(intent);
            activity.finish();
        }, delayMillis);
    }
}

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Access Singleton
        AppConfig config = AppConfig.getInstance();

        // Facade handles splash setup
        SplashFacade facade = new SplashFacade(this);
        facade.showSplash(3000, MainActivity.class);
    }
}
