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

public class BmiActivity extends AppCompatActivity {

    Button mrecalculatbmi;
    TextView mbmidisplay, mbmicateogory, mgender;
    ImageView mimageview;
    RelativeLayout mbackground;

    //FACADE PATTERN (Inner Class)
    private static class BmiFacade {
        private final float height;
        private final float weight;
        private final String gender;

        BmiFacade(String height, String weight, String gender) {
            this.height = Float.parseFloat(height) / 100f;
            this.weight = Float.parseFloat(weight);
            this.gender = gender;
        }

        float calculateBmi() {
            return weight / (height * height);
        }

        BmiCategoryStrategy getCategoryStrategy(float bmi) {
            if (bmi < 16) return new SevereThinnessStrategy();
            else if (bmi < 17) return new ModerateThinnessStrategy();
            else if (bmi < 18.5) return new MildThinnessStrategy();
            else if (bmi < 25) return new NormalStrategy();
            else if (bmi < 30) return new OverweightStrategy();
            else return new ObeseStrategy();
        }
    }


    //STRATEGY PATTERN (Inner Interface + Classes)

    private interface BmiCategoryStrategy {
        String getCategory();
        int getBackgroundColor();
        int getImageResource();
    }

    private static class SevereThinnessStrategy implements BmiCategoryStrategy {
        public String getCategory() { return "Severe Thinness"; }
        public int getBackgroundColor() { return Color.RED; }
        public int getImageResource() { return R.drawable.cross2; }
    }

    private static class ModerateThinnessStrategy implements BmiCategoryStrategy {
        public String getCategory() { return "Moderate Thinness"; }
        public int getBackgroundColor() { return Color.RED; }
        public int getImageResource() { return R.drawable.warning2; }
    }

    private static class MildThinnessStrategy implements BmiCategoryStrategy {
        public String getCategory() { return "Mild Thinness"; }
        public int getBackgroundColor() { return Color.RED; }
        public int getImageResource() { return R.drawable.warning2; }
    }

    private static class NormalStrategy implements BmiCategoryStrategy {
        public String getCategory() { return "Normal"; }
        public int getBackgroundColor() { return Color.GREEN; }
        public int getImageResource() { return R.drawable.ok1; }
    }

    private static class OverweightStrategy implements BmiCategoryStrategy {
        public String getCategory() { return "Overweight"; }
        public int getBackgroundColor() { return Color.YELLOW; }
        public int getImageResource() { return R.drawable.warning2; }
    }

    private static class ObeseStrategy implements BmiCategoryStrategy {
        public String getCategory() { return "Obese Class I"; }
        public int getBackgroundColor() { return Color.RED; }
        public int getImageResource() { return R.drawable.warning2; }
    }



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

        BmiFacade bmiFacade = new BmiFacade(height, weight, gender);

        float bmiValue = bmiFacade.calculateBmi();
        String bmiText = String.format("%.2f", bmiValue);

        BmiCategoryStrategy strategy = bmiFacade.getCategoryStrategy(bmiValue);

        mbmidisplay.setText(bmiText);
        mgender.setText(gender);
        mbmicateogory.setText(strategy.getCategory());
        mbackground.setBackgroundColor(strategy.getBackgroundColor());
        mimageview.setImageResource(strategy.getImageResource());

        mrecalculatbmi.setOnClickListener(v -> {
            Intent i = new Intent(BmiActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}