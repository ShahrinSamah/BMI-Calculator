bmiactivity.java


package com.example.bmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class bmiactivity extends AppCompatActivity {


    android.widget.Button mrecalculatbmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        getActionBar().hide();
        mrecalculatbmi=findViewById(R.id.recalculatebmi);

        mrecalculatbmi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(bmiactivity.this,MainActivity.class);
                startActivity(intent);
                finish();


            }
        });;
    }
}