package com.example.bmicalculator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

// Observer Interface
interface BMIObserver {
    void update(float bmi, String category);
}

// Concrete Observers
class BMITextViewObserver implements BMIObserver {
    private TextView bmiDisplay, categoryDisplay, genderDisplay;
    private String gender;

    public BMITextViewObserver(TextView bmiDisplay, TextView categoryDisplay, TextView genderDisplay, String gender) {
        this.bmiDisplay = bmiDisplay;
        this.categoryDisplay = categoryDisplay;
        this.genderDisplay = genderDisplay;
        this.gender = gender;
    }

    @Override
    public void update(float bmi, String category) {
        bmiDisplay.setText(String.format("%.2f", bmi));
        categoryDisplay.setText(category);
        genderDisplay.setText(gender);
    }
}

class BMIUIObserver implements BMIObserver {
    private ImageView imageView;
    private RelativeLayout background;

    public BMIUIObserver(ImageView imageView, RelativeLayout background) {
        this.imageView = imageView;
        this.background = background;
    }

    @Override
    public void update(float bmi, String category) {
        // Update UI based on category
        switch (category) {
            case "Severe Thinness":
            case "Moderate Thinness":
            case "Mild Thinness":
                background.setBackgroundColor(Color.RED);
                imageView.setImageResource(R.drawable.warning2);
                break;
            case "Normal":
                background.setBackgroundColor(Color.GREEN);
                imageView.setImageResource(R.drawable.ok1);
                break;
            case "Overweight":
            case "Obese Class I":
                background.setBackgroundColor(Color.RED);
                imageView.setImageResource(R.drawable.warning2);
                break;
        }
    }
}

// Subject Class
class BMISubject {
    private List<BMIObserver> observers = new ArrayList<>();
    private float bmi;
    private String category;

    public void attach(BMIObserver observer) {
        observers.add(observer);
    }

    public void setBMI(float bmi, String category) {
        this.bmi = bmi;
        this.category = category;
        notifyObservers();
    }

    private void notifyObservers() {
        for (BMIObserver observer : observers) {
            observer.update(bmi, category);
        }
    }
}

public class BmiActivity extends AppCompatActivity {

    Button mrecalculatbmi;
    TextView mbmidisplay, mbmicateogory, mgender;
    ImageView mimageview;
    RelativeLayout mbackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">Result</font>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E1D1D")));
        }

        mbmidisplay = findViewById(R.id.bmidisplay);
        mbmicateogory = findViewById(R.id.bmicategory);
        mgender = findViewById(R.id.genderdisplay);
        mbackground = findViewById(R.id.contentlayout);
        mimageview = findViewById(R.id.imageview);
        mrecalculatbmi = findViewById(R.id.recalculatebmi);

        Intent intent = getIntent();
        String height = intent.getStringExtra("height");
        String weight = intent.getStringExtra("weight");
        String gender = intent.getStringExtra("gender");

        float intheight = Float.parseFloat(height) / 100f;
        float intweight = Float.parseFloat(weight);

        BMICalculator calculator = BMICalculator.getInstance();
        float intbmi = calculator.calculateBMI(intheight * 100, intweight);
        String category = calculator.getBMICategory(intbmi);

        // Observer pattern setup
        BMISubject bmiSubject = new BMISubject();
        bmiSubject.attach(new BMITextViewObserver(mbmidisplay, mbmicateogory, mgender, gender));
        bmiSubject.attach(new BMIUIObserver(mimageview, mbackground));

        bmiSubject.setBMI(intbmi, category); // Notifies observers to update UI

        mrecalculatbmi.setOnClickListener(v -> {
            Intent i = new Intent(BmiActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}
