package com.example.bmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

//Command Pattern
interface Command {
    void execute();
}

class IncrementAgeCommand implements Command {
    private MainActivity activity;
    public IncrementAgeCommand(MainActivity activity) { this.activity = activity; }
    @Override public void execute() { activity.incrementAge(); }
}

class DecrementAgeCommand implements Command {
    private MainActivity activity;
    public DecrementAgeCommand(MainActivity activity) { this.activity = activity; }
    @Override public void execute() { activity.decrementAge(); }
}

class IncrementWeightCommand implements Command {
    private MainActivity activity;
    public IncrementWeightCommand(MainActivity activity) { this.activity = activity; }
    @Override public void execute() { activity.incrementWeight(); }
}

class DecrementWeightCommand implements Command {
    private MainActivity activity;
    public DecrementWeightCommand(MainActivity activity) { this.activity = activity; }
    @Override public void execute() { activity.decrementWeight(); }
}

class CalculateBMICommand implements Command {
    private MainActivity activity;
    public CalculateBMICommand(MainActivity activity) { this.activity = activity; }
    @Override public void execute() { activity.calculateAndNavigate(); }
}

// Invoker — exactly like Waiter in teacher's example
class ButtonCommandInvoker {
    private Command command;
    public void setCommand(Command command) { this.command = command; }
    public void executeCommand() { if (command != null) command.execute(); }
}

//Stategy Pattern
interface GenderStrategy {
    String getGenderName();
}

class MaleStrategy implements GenderStrategy {
    @Override public String getGenderName() { return "Male"; }
}

class FemaleStrategy implements GenderStrategy {
    @Override public String getGenderName() { return "Female"; }
}

//Observer Pattern
interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

interface Observer {
    void update(int age, int weight, int height, String gender);
}

class UserInputData implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private int age = 22;
    private int weight = 55;
    private int height = 170;
    private String gender = "0";

    @Override public void registerObserver(Observer o) { if (o != null && !observers.contains(o)) observers.add(o); }
    @Override public void removeObserver(Observer o) { if (o != null) observers.remove(o); }
    @Override public void notifyObservers() {
        for (Observer o : observers) if (o != null) o.update(age, weight, height, gender);
    }

    public void setAge(int age) { this.age = age; notifyObservers(); }
    public void setWeight(int weight) { this.weight = weight; notifyObservers(); }
    public void setHeight(int height) { this.height = height; notifyObservers(); }
    public void setGender(String gender) { this.gender = gender; notifyObservers(); }

    public int getAge() { return age; }
    public int getWeight() { return weight; }
    public int getHeight() { return height; }
    public String getGender() { return gender; }
}

class UIObserver implements Observer {
    private MainActivity activity;
    public UIObserver(MainActivity activity) { this.activity = activity; }

    @Override
    public void update(int age, int weight, int height, String gender) {
        if (activity.mcurrentage != null) activity.mcurrentage.setText(String.valueOf(age));
        if (activity.mcurrentweight != null) activity.mcurrentweight.setText(String.valueOf(weight));
        if (activity.mcurrentheight != null) activity.mcurrentheight.setText(String.valueOf(height));
        activity.updateGenderBackground(gender);
    }
}

//Facade Pattern
class MainFacade {
    private UserInputData data;
    private GenderStrategy strategy;

    public MainFacade() {
        this.data = new UserInputData();
        this.strategy = null;
    }

    public void setGenderStrategy(GenderStrategy strategy) {
        this.strategy = strategy;
        if (strategy != null) data.setGender(strategy.getGenderName());
    }

    public UserInputData getData() { return data; }
    public String getGender() { return strategy != null ? strategy.getGenderName() : "0"; }

    public boolean validateAndLaunchBmiActivity(MainActivity activity) {
        if (getGender().equals("0")) {
            Toast.makeText(activity, "Select Your Gender First", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (data.getHeight() == 0) {
            Toast.makeText(activity, "Select Your Height First", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (data.getAge() <= 0) {
            Toast.makeText(activity, "Age Is Incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (data.getWeight() <= 0) {
            Toast.makeText(activity, "Weight Is Incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }

        Intent intent = new Intent(activity, BmiActivity.class);
        intent.putExtra("gender", getGender());
        intent.putExtra("height", String.valueOf(data.getHeight()));
        intent.putExtra("weight", String.valueOf(data.getWeight()));
        activity.startActivity(intent);
        return true;
    }
}

//Factory Pattern
class CommandFactory {
    private MainActivity activity;
    public CommandFactory(MainActivity activity) { this.activity = activity; }

    public Command createCommand(String type) {
        if (type == null) return null;
        return switch (type) {
            case "increment_age" -> new IncrementAgeCommand(activity);
            case "decrement_age" -> new DecrementAgeCommand(activity);
            case "increment_weight" -> new IncrementWeightCommand(activity);
            case "decrement_weight" -> new DecrementWeightCommand(activity);
            case "calculate" -> new CalculateBMICommand(activity);
            default -> null;
        };
    }
}

//Main Activity
public class MainActivity extends AppCompatActivity {

    // UI
    android.widget.Button mcalculatebmi;
    TextView mcurrentheight, mcurrentage, mcurrentweight;
    ImageView mincrementage, mincrementweight, mdecrementweight, mdecrementage;
    SeekBar mseekbarforheight;
    RelativeLayout mmale, mfemale;

    // Patterns
    private MainFacade facade;
    private ButtonCommandInvoker invoker;
    private CommandFactory commandFactory;
    private UIObserver uiObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initUI();
        initPatterns();

        uiObserver = new UIObserver(this);
        facade.getData().registerObserver(uiObserver);

        setupSeekBar();
        setupButtons();
    }

    private void initUI() {
        mcalculatebmi = findViewById(R.id.calculatebmi);
        mcurrentage = findViewById(R.id.currentage);
        mcurrentweight = findViewById(R.id.currentweight);
        mcurrentheight = findViewById(R.id.currentheight);
        mincrementage = findViewById(R.id.incrementage);
        mdecrementage = findViewById(R.id.decrementage);
        mincrementweight = findViewById(R.id.incrementweight);
        mdecrementweight = findViewById(R.id.decrementweight);
        mseekbarforheight = findViewById(R.id.seekbarforheight);
        mmale = findViewById(R.id.male);
        mfemale = findViewById(R.id.female);

        mseekbarforheight.setMax(250);
        mseekbarforheight.setProgress(170);
    }

    private void initPatterns() {
        facade = new MainFacade();
        invoker = new ButtonCommandInvoker();
        commandFactory = new CommandFactory(this);
        facade.getData().setHeight(170); // initial value
    }

    private void setupSeekBar() {
        mseekbarforheight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                facade.getData().setHeight(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupButtons() {
        mincrementage.setOnClickListener(v -> execute("increment_age"));
        mdecrementage.setOnClickListener(v -> execute("decrement_age"));
        mincrementweight.setOnClickListener(v -> execute("increment_weight"));
        mdecrementweight.setOnClickListener(v -> execute("decrement_weight"));

        mmale.setOnClickListener(v -> {
            facade.setGenderStrategy(new MaleStrategy());
            updateGenderBackground("Male");
        });

        mfemale.setOnClickListener(v -> {
            facade.setGenderStrategy(new FemaleStrategy());
            updateGenderBackground("Female");
        });

        mcalculatebmi.setOnClickListener(v -> execute("calculate"));
    }

    private void execute(String type) {
        Command cmd = commandFactory.createCommand(type);
        if (cmd != null) {
            invoker.setCommand(cmd);
            invoker.executeCommand();
        }
    }

    // Called by commands
    public void incrementAge() { facade.getData().setAge(facade.getData().getAge() + 1); }
    public void decrementAge() { int a = facade.getData().getAge(); if (a > 1) facade.getData().setAge(a - 1); }
    public void incrementWeight() { facade.getData().setWeight(facade.getData().getWeight() + 1); }
    public void decrementWeight() { int w = facade.getData().getWeight(); if (w > 1) facade.getData().setWeight(w - 1); }
    public void calculateAndNavigate() { facade.validateAndLaunchBmiActivity(this); }

    public void updateGenderBackground(String gender) {
        if ("Male".equals(gender)) {
            mmale.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemalefocus));
            mfemale.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemalenotfocus));
        } else if ("Female".equals(gender)) {
            mfemale.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemalefocus));
            mmale.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemalenotfocus));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (facade != null && uiObserver != null) {
            facade.getData().removeObserver(uiObserver);
        }
    }
}