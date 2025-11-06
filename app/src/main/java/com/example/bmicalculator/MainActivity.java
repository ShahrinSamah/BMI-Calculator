package com.example.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// Observer interface
interface InputObserver {
    void onValueChanged(int value);
}

// Concrete observers for UI
class TextViewObserver implements InputObserver {
    private TextView textView;

    public TextViewObserver(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void onValueChanged(int value) {
        textView.setText(String.valueOf(value));
    }
}

// Subject class
class InputSubject {
    private int value;
    private InputObserver observer;

    public void attach(InputObserver observer) {
        this.observer = observer;
    }

    public void setValue(int value) {
        this.value = value;
        if (observer != null) observer.onValueChanged(value);
    }

    public int getValue() { return value; }
}

// Facade for validation and navigation
class InputHandlerFacade {
    private Context context;

    public InputHandlerFacade(Context context) {
        this.context = context;
    }

    public boolean validateInput(String gender, int height, int age, int weight) {
        if (gender.equals("0")) {
            Toast.makeText(context, "Select Your Gender First", Toast.LENGTH_SHORT).show();
            return false;
        } else if (height <= 0) {
            Toast.makeText(context, "Select Your Height First", Toast.LENGTH_SHORT).show();
            return false;
        } else if (age <= 0) {
            Toast.makeText(context, "Age Is Incorrect", Toast.LENGTH_SHORT).show();
            return false;
        } else if (weight <= 0) {
            Toast.makeText(context, "Weight Is Incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void navigateToBMI(String gender, int height, int weight) {
        Intent intent = new Intent(context, BmiActivity.class);
        intent.putExtra("gender", gender);
        intent.putExtra("height", String.valueOf(height));
        intent.putExtra("weight", String.valueOf(weight));
        context.startActivity(intent);
    }
}

public class MainActivity extends AppCompatActivity {

    android.widget.Button mcalculatebmi;
    TextView mcurrentheight, mcurrentage, mcurrentweight;
    ImageView mincrementage, mincrementweight, mdecrementweight, mdecrementage;
    SeekBar mseekbarforheight;
    RelativeLayout mmale, mfemale;

    int intweight = 55;
    int intage = 22;
    int currentprogress = 170;
    String typeofuser = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Bind UI
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

        // Observers
        InputSubject heightSubject = new InputSubject();
        heightSubject.attach(new TextViewObserver(mcurrentheight));
        InputSubject ageSubject = new InputSubject();
        ageSubject.attach(new TextViewObserver(mcurrentage));
        InputSubject weightSubject = new InputSubject();
        weightSubject.attach(new TextViewObserver(mcurrentweight));

        // Initial values
        heightSubject.setValue(currentprogress);
        ageSubject.setValue(intage);
        weightSubject.setValue(intweight);

        // Gender selection
        mmale.setOnClickListener(v -> {
            mmale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalefocus));
            mfemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalenotfocus));
            typeofuser = "Male";
        });

        mfemale.setOnClickListener(v -> {
            mfemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalefocus));
            mmale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalenotfocus));
            typeofuser = "Female";
        });

        // Height SeekBar
        mseekbarforheight.setMax(250);
        mseekbarforheight.setProgress(currentprogress);
        mseekbarforheight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { heightSubject.setValue(progress); }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Increment/decrement age
        mincrementage.setOnClickListener(v -> ageSubject.setValue(++intage));
        mdecrementage.setOnClickListener(v -> { if (intage > 1) ageSubject.setValue(--intage); });

        // Increment/decrement weight
        mincrementweight.setOnClickListener(v -> weightSubject.setValue(++intweight));
        mdecrementweight.setOnClickListener(v -> { if (intweight > 1) weightSubject.setValue(--intweight); });

        // Facade for validation and navigation
        InputHandlerFacade facade = new InputHandlerFacade(this);
        mcalculatebmi.setOnClickListener(v -> {
            if (facade.validateInput(typeofuser, heightSubject.getValue(), ageSubject.getValue(), weightSubject.getValue())) {
                facade.navigateToBMI(typeofuser, heightSubject.getValue(), weightSubject.getValue());
            }
        });
    }
}
