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
        float intbmi = intweight / (intheight * intheight);
        String mbmi = String.format("%.2f", intbmi);

        mbmidisplay.setText(mbmi);
        mgender.setText(gender);

        if (intbmi < 16) {
            mbmicateogory.setText("Severe Thinness");
            mbackground.setBackgroundColor(Color.RED);
            mimageview.setImageResource(R.drawable.cross2);
        } else if (intbmi >= 16 && intbmi < 17) {
            mbmicateogory.setText("Moderate Thinness");
            mbackground.setBackgroundColor(Color.RED);
            mimageview.setImageResource(R.drawable.warning2);
        } else if (intbmi >= 17 && intbmi < 18.5) {
            mbmicateogory.setText("Mild Thinness");
            mbackground.setBackgroundColor(Color.RED);
            mimageview.setImageResource(R.drawable.warning2);
        } else if (intbmi >= 18.5 && intbmi < 25) {
            mbmicateogory.setText("Normal");
            mbackground.setBackgroundColor(Color.GREEN);
            mimageview.setImageResource(R.drawable.ok1);
        } else if (intbmi >= 25 && intbmi < 30) {
            mbmicateogory.setText("Overweight");
            mbackground.setBackgroundColor(Color.YELLOW);
            mimageview.setImageResource(R.drawable.warning2);
        } else {
            mbmicateogory.setText("Obese Class I");
            mbackground.setBackgroundColor(Color.RED);
            mimageview.setImageResource(R.drawable.warning2);
        }

        mrecalculatbmi.setOnClickListener(v -> {
            Intent i = new Intent(BmiActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}


