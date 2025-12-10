package com.example.bmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

//1. SINGLETON PATTERN 
class SplashManager {
    private static final SplashManager instance = new SplashManager();

    private SplashManager() {}

    public static SplashManager getInstance() {
        return instance;
    }

    public void showMessage() {
        System.out.println("Splash screen displayed");
    }
}

//  2. COMMAND PATTERN 
interface Command {
    void execute();
}

class StartMainActivityCommand implements Command {
    private splash activity;

    public StartMainActivityCommand(splash activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}

class CommandInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
        }
    }
}

//  3. STRATEGY PATTERN
interface SplashStrategy {
    void execute(splash activity, CommandInvoker invoker);
}

class NormalSplashStrategy implements SplashStrategy {
    @Override
    public void execute(splash activity, CommandInvoker invoker) {
        invoker.setCommand(new StartMainActivityCommand(activity));
        new Handler().postDelayed(() -> invoker.executeCommand(), 3000);
    }
}

//  4. FACADE PATTERN 
class SplashFacade {
    private splash activity;

    public SplashFacade(splash activity) {
        this.activity = activity;
    }

    public void startSplash() {
        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        activity.getSupportActionBar().hide();

        SplashStrategy strategy = new NormalSplashStrategy();
        CommandInvoker invoker = new CommandInvoker();
        strategy.execute(activity, invoker);

        SplashManager.getInstance().showMessage();
    }
}

//  MAIN SPLASH ACTIVITY 
public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashFacade facade = new SplashFacade(this);
        facade.startSplash();
    }
}