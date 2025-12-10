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

// ------------------------- Singleton Pattern -------------------------
class BMIManager {
    private static final BMIManager instance = new BMIManager();
    private float lastBmi;
    private String lastCategory;

    private BMIManager() {}

    public static BMIManager getInstance() {
        return instance;
    }

    public void saveResult(float bmi, String category) {
        this.lastBmi = bmi;
        this.lastCategory = category;
    }

    public float getLastBmi() { return lastBmi; }
    public String getLastCategory() { return lastCategory; }
}

// ------------------------- Strategy Pattern -------------------------
interface IStrategy {
    String getCategory();
    int getBackgroundColor();
    int getImageResource();
}

class SevereThinnessStrategy implements IStrategy {
    @Override public String getCategory() { return "Severe Thinness"; }
    @Override public int getBackgroundColor() { return Color.RED; }
    @Override public int getImageResource() { return R.drawable.cross2; }
}

class ModerateThinnessStrategy implements IStrategy {
    @Override public String getCategory() { return "Moderate Thinness"; }
    @Override public int getBackgroundColor() { return Color.RED; }
    @Override public int getImageResource() { return R.drawable.warning2; }
}

class MildThinnessStrategy implements IStrategy {
    @Override public String getCategory() { return "Mild Thinness"; }
    @Override public int getBackgroundColor() { return Color.RED; }
    @Override public int getImageResource() { return R.drawable.warning2; }
}

class NormalStrategy implements IStrategy {
    @Override public String getCategory() { return "Normal"; }
    @Override public int getBackgroundColor() { return Color.GREEN; }
    @Override public int getImageResource() { return R.drawable.ok1; }
}

class OverweightStrategy implements IStrategy {
    @Override public String getCategory() { return "Overweight"; }
    @Override public int getBackgroundColor() { return Color.YELLOW; }
    @Override public int getImageResource() { return R.drawable.warning2; }
}

class ObeseStrategy implements IStrategy {
    @Override public String getCategory() { return "Obese Class I"; }
    @Override public int getBackgroundColor() { return Color.RED; }
    @Override public int getImageResource() { return R.drawable.warning2; }
}

// -------------------------  Simple Factory Pattern -------------------------
class SimpleBmiFactory {
    public IStrategy createStrategy(float bmi) {
        IStrategy strategy = null;
        if (bmi < 16) {
            strategy = new SevereThinnessStrategy();
        } else if (bmi >= 16 && bmi < 17) {
            strategy = new ModerateThinnessStrategy();
        } else if (bmi >= 17 && bmi < 18.5) {
            strategy = new MildThinnessStrategy();
        } else if (bmi >= 18.5 && bmi < 25) {
            strategy = new NormalStrategy();
        } else if (bmi >= 25 && bmi < 30) {
            strategy = new OverweightStrategy();
        } else {
            strategy = new ObeseStrategy();
        }
        return strategy;
    }
}

// ------------------------- Facade Pattern -------------------------
class BMIResultFacade {
    private TextView categoryView;
    private RelativeLayout background;
    private ImageView imageView;

    public BMIResultFacade(TextView categoryView, RelativeLayout background, ImageView imageView) {
        this.categoryView = categoryView;
        this.background = background;
        this.imageView = imageView;
    }

    public void updateUI(IStrategy strategy) {
        categoryView.setText(strategy.getCategory());
        background.setBackgroundColor(strategy.getBackgroundColor());
        imageView.setImageResource(strategy.getImageResource());
    }
}

// ------------------------- Main Activity with all patterns -------------------------
public class BmiActivity extends AppCompatActivity {

    TextView mbmidisplay, mbmicateogory, mgender;
    ImageView mimageview;
    RelativeLayout mbackground;
    Button mrecalculatbmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">Result</font>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E1D1D")));
        }

        // View binding
        mbmidisplay = findViewById(R.id.bmidisplay);
        mbmicateogory = findViewById(R.id.bmicategory);
        mgender = findViewById(R.id.genderdisplay);
        mbackground = findViewById(R.id.contentlayout);
        mimageview = findViewById(R.id.imageview);
        mrecalculatbmi = findViewById(R.id.recalculatebmi);

        // Get data from intent
        Intent intent = getIntent();
        String height = intent.getStringExtra("height");
        String weight = intent.getStringExtra("weight");
        String gender = intent.getStringExtra("gender");

        float intheight = Float.parseFloat(height) / 100f;
        float intweight = Float.parseFloat(weight);
        float bmi = intweight / (intheight * intheight);

        String bmiText = String.format("%.2f", bmi);
        mbmidisplay.setText(bmiText);
        mgender.setText(gender);

        // 1. Factory creates correct strategy
        SimpleBmiFactory factory = new SimpleBmiFactory();
        IStrategy bmiStrategy = factory.createStrategy(bmi);

        // 2. Facade updates all UI in one call
        BMIResultFacade facade = new BMIResultFacade(mbmicateogory, mbackground, mimageview);
        facade.updateUI(bmiStrategy);

        // 3. Singleton saves result (can be used in other activities)
        BMIManager.getInstance().saveResult(bmi, bmiStrategy.getCategory());

        // Recalculate button
        mrecalculatbmi.setOnClickListener(v -> {
            Intent i = new Intent(BmiActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}