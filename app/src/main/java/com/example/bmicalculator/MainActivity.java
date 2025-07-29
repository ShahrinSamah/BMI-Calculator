package com.example.bmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

        mseekbarforheight.setMax(250);
        mseekbarforheight.setProgress(currentprogress);
        mcurrentheight.setText(String.valueOf(currentprogress));

        mseekbarforheight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentprogress = progress;
                mcurrentheight.setText(String.valueOf(currentprogress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mincrementage.setOnClickListener(v -> {
            intage++;
            mcurrentage.setText(String.valueOf(intage));
        });

        mdecrementage.setOnClickListener(v -> {
            if (intage > 1) {
                intage--;
                mcurrentage.setText(String.valueOf(intage));
            }
        });

        mincrementweight.setOnClickListener(v -> {
            intweight++;
            mcurrentweight.setText(String.valueOf(intweight));
        });

        mdecrementweight.setOnClickListener(v -> {
            if (intweight > 1) {
                intweight--;
                mcurrentweight.setText(String.valueOf(intweight));
            }
        });

        mcalculatebmi.setOnClickListener(v -> {
            if (typeofuser.equals("0")) {
                Toast.makeText(getApplicationContext(), "Select Your Gender First", Toast.LENGTH_SHORT).show();
            } else if (currentprogress == 0) {
                Toast.makeText(getApplicationContext(), "Select Your Height First", Toast.LENGTH_SHORT).show();
            } else if (intage <= 0) {
                Toast.makeText(getApplicationContext(), "Age Is Incorrect", Toast.LENGTH_SHORT).show();
            } else if (intweight <= 0) {
                Toast.makeText(getApplicationContext(), "Weight Is Incorrect", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, bmiactivity.class);
                intent.putExtra("gender", typeofuser);
                intent.putExtra("height", String.valueOf(currentprogress));
                intent.putExtra("weight", String.valueOf(intweight));
                startActivity(intent);
            }
        });
    }
}