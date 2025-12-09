package com.example.bmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

//1. SINGLETON PATTERN
class SplashConfig {
    private static final SplashConfig instance = new SplashConfig();
    
    private int splashDelay = 3000;
    private boolean isFullscreen = true;
    private boolean hideActionBar = true;
    private long lastAccessTime;
    
    private SplashConfig() {
        this.lastAccessTime = System.currentTimeMillis();
    }
    
    public static SplashConfig getInstance() {
        return instance;
    }
    
    public int getSplashDelay() {
        return splashDelay;
    }
    
    public void setSplashDelay(int delay) {
        if (delay >= 1000 && delay <= 10000) {
            this.splashDelay = delay;
        } else {
            throw new IllegalArgumentException("Splash delay must be between 1000 and 10000 milliseconds");
        }
    }
    
    public boolean isFullscreen() {
        return isFullscreen;
    }
    
    public void setFullscreen(boolean fullscreen) {
        this.isFullscreen = fullscreen;
    }
    
    public boolean isHideActionBar() {
        return hideActionBar;
    }
    
    public void setHideActionBar(boolean hideActionBar) {
        this.hideActionBar = hideActionBar;
    }
    
    public long getLastAccessTime() {
        return lastAccessTime;
    }
    
    public void updateAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }
    
    public void logSplashShown() {
        updateAccessTime();
        System.out.println("Splash screen displayed at: " + lastAccessTime);
    }
}

// 2. COMMAND PATTERN
interface Command {
    void execute();
}

class StartMainActivityCommand implements Command {
    private splash activity;
    
    public StartMainActivityCommand(splash activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        this.activity = activity;
    }
    
    @Override
    public void execute() {
        try {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } catch (Exception e) {
            Toast.makeText(activity, "Navigation error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.err.println("Error navigating to MainActivity: " + e.getMessage());
        }
    }
}

class DelayedCommand implements Command {
    private Command command;
    private long delayMillis;
    private Handler handler;
    
    public DelayedCommand(Command command, long delayMillis) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        if (delayMillis < 0) {
            throw new IllegalArgumentException("Delay cannot be negative");
        }
        
        this.command = command;
        this.delayMillis = delayMillis;
        this.handler = new Handler();
    }
    
    @Override
    public void execute() {
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.execute();
                    } catch (Exception e) {
                        System.err.println("Error executing delayed command: " + e.getMessage());
                    }
                }
            }, delayMillis);
        } catch (Exception e) {
            System.err.println("Error scheduling delayed command, executing immediately: " + e.getMessage());
            command.execute();
        }
    }
    
    public void cancel() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}

class CommandInvoker {
    private Command command;
    
    public void setCommand(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        this.command = command;
    }
    
    public void executeCommand() {
        if (command != null) {
            try {
                command.execute();
            } catch (Exception e) {
                System.err.println("Error executing command: " + e.getMessage());
                throw new RuntimeException("Command execution failed", e);
            }
        } else {
            throw new IllegalStateException("No command set in invoker");
        }
    }
    
    public void cancelCommand() {
        if (command instanceof DelayedCommand) {
            ((DelayedCommand) command).cancel();
        }
    }
}

//3. STRATEGY PATTERN 
interface SplashStrategy {
    void execute(splash activity, CommandInvoker invoker);
}

class NormalSplashStrategy implements SplashStrategy {
    @Override
    public void execute(splash activity, CommandInvoker invoker) {
        try {
            if (activity == null) {
                throw new IllegalArgumentException("Activity cannot be null");
            }
            if (invoker == null) {
                throw new IllegalArgumentException("Invoker cannot be null");
            }
            
            int delay = SplashConfig.getInstance().getSplashDelay();
            
            Command navigationCommand = new StartMainActivityCommand(activity);
            Command delayedCommand = new DelayedCommand(navigationCommand, delay);
            
            invoker.setCommand(delayedCommand);
            invoker.executeCommand();
            
        } catch (Exception e) {
            Toast.makeText(activity, "Error in splash strategy: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            new StartMainActivityCommand(activity).execute();
        }
    }
}

class FastSplashStrategy implements SplashStrategy {
    @Override
    public void execute(splash activity, CommandInvoker invoker) {
        try {
            if (activity == null) {
                throw new IllegalArgumentException("Activity cannot be null");
            }
            if (invoker == null) {
                throw new IllegalArgumentException("Invoker cannot be null");
            }
            
            Command navigationCommand = new StartMainActivityCommand(activity);
            Command delayedCommand = new DelayedCommand(navigationCommand, 1000);
            
            invoker.setCommand(delayedCommand);
            invoker.executeCommand();
            
        } catch (Exception e) {
            Toast.makeText(activity, "Error in fast splash strategy: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            new StartMainActivityCommand(activity).execute();
        }
    }
}

class OnboardingSplashStrategy implements SplashStrategy {
    @Override
    public void execute(splash activity, CommandInvoker invoker) {
        try {
            if (activity == null) {
                throw new IllegalArgumentException("Activity cannot be null");
            }
            if (invoker == null) {
                throw new IllegalArgumentException("Invoker cannot be null");
            }
            
            int delay = SplashConfig.getInstance().getSplashDelay() + 1000;
            
            Command navigationCommand = new StartMainActivityCommand(activity);
            Command delayedCommand = new DelayedCommand(navigationCommand, delay);
            
            invoker.setCommand(delayedCommand);
            invoker.executeCommand();
            
        } catch (Exception e) {
            Toast.makeText(activity, "Error in onboarding strategy: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            new StartMainActivityCommand(activity).execute();
        }
    }
}

// 4. FACADE PATTERN
class SplashFacade {
    private splash activity;
    private CommandInvoker invoker;
    private SplashStrategy strategy;
    
    public SplashFacade(splash activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        this.activity = activity;
        this.invoker = new CommandInvoker();
    }
    
    public void startSplash() {
        try {
            configureScreenAppearance();
            selectStrategy();
            executeStrategy();
            logSplashActivity();
        } catch (Exception e) {
            Toast.makeText(activity, "Splash initialization error, navigating...", Toast.LENGTH_SHORT).show();
            try {
                new StartMainActivityCommand(activity).execute();
            } catch (Exception ex) {
                System.err.println("Critical error in splash: " + ex.getMessage());
            }
        }
    }
    
    private void configureScreenAppearance() {
        try {
            SplashConfig config = SplashConfig.getInstance();
            
            if (config.isFullscreen()) {
                if (activity.getWindow() != null) {
                    activity.getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                    );
                }
            }
            
            if (config.isHideActionBar()) {
                if (activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().hide();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error configuring screen appearance: " + e.getMessage());
        }
    }
    
    private void selectStrategy() {
        try {
            strategy = new NormalSplashStrategy();
        } catch (Exception e) {
            System.err.println("Error selecting strategy, using default: " + e.getMessage());
            strategy = new NormalSplashStrategy();
        }
    }
    
    private void executeStrategy() {
        try {
            if (strategy != null) {
                strategy.execute(activity, invoker);
            } else {
                throw new IllegalStateException("Strategy not initialized");
            }
        } catch (Exception e) {
            System.err.println("Error executing strategy: " + e.getMessage());
            new StartMainActivityCommand(activity).execute();
        }
    }
    
    private void logSplashActivity() {
        try {
            SplashConfig.getInstance().logSplashShown();
        } catch (Exception e) {
            System.err.println("Error logging splash activity: " + e.getMessage());
        }
    }
    
    public void cancelNavigation() {
        try {
            if (invoker != null) {
                invoker.cancelCommand();
            }
        } catch (Exception e) {
            System.err.println("Error canceling navigation: " + e.getMessage());
        }
    }
}

// MAIN SPLASH ACTIVITY 
public class splash extends AppCompatActivity {
    
    private SplashFacade splashFacade;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_splash);
            initializeSplash();
        } catch (Exception e) {
            Toast.makeText(this, "Critical error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            System.err.println("Critical error in splash onCreate: " + e.getMessage());
            navigateToMainImmediately();
        }
    }
    
    private void initializeSplash() {
        try {
            splashFacade = new SplashFacade(this);
            splashFacade.startSplash();
        } catch (Exception e) {
            System.err.println("Error initializing splash facade: " + e.getMessage());
            throw e;
        }
    }
    
    private void navigateToMainImmediately() {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            System.err.println("Emergency navigation failed: " + e.getMessage());
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        try {
            if (splashFacade != null) {
                splashFacade.cancelNavigation();
            }
        } catch (Exception e) {
            System.err.println("Error in onDestroy: " + e.getMessage());
        }
    }
    
    @Override
    public void onBackPressed() {
    }
}