package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.demoapp.BMICalcUtil.BMI_CATEGORY_HEALTHY;
import static com.example.demoapp.BMICalcUtil.BMI_CATEGORY_OBESE;
import static com.example.demoapp.BMICalcUtil.BMI_CATEGORY_OVERWEIGHT;
import static com.example.demoapp.BMICalcUtil.BMI_CATEGORY_UNDERWEIGHT;

public class BmiCalcActivity extends AppCompatActivity {

    EditText height,weight,age;
    Button calculate;
    CardView resultCard;
    TextView bmiValue, bmiCategory,bmiTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calc);

        height = findViewById(R.id.bmiHeight);
        weight = findViewById(R.id.bmiWeight);
        age= findViewById(R.id.bmiAge);

        calculate = findViewById(R.id.calcBMI);
        resultCard = findViewById(R.id.activity_main_resultcard);
        bmiValue = findViewById(R.id.activity_main_bmi);
        bmiCategory = findViewById(R.id.activity_main_category);
        bmiTitle = findViewById(R.id.title1);

        resultCard.setVisibility(View.GONE);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(age.length()==0||height.length()==0||weight.length()==0){
                    Toast.makeText(BmiCalcActivity.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                }else{
                    double heightInCms = Double.parseDouble(height.getText().toString());
                    double weightInKgs = Double.parseDouble(weight.getText().toString());
                    double uAge = Double.parseDouble(age.getText().toString());

                    double bmi=BMICalcUtil.getInstance().calculateBMIMetric(heightInCms,weightInKgs);
                    showResult(bmi);
                }
            }
        });




    }

    private void showResult(double bmi) {
       resultCard.setVisibility(View.VISIBLE);

       bmiValue.setText(String.format("%2f",bmi));

       String bmiCategoryUser=BMICalcUtil.getInstance().classifyBMI(bmi);
       bmiCategory.setText(bmiCategoryUser);

       switch (bmiCategoryUser){
           case BMI_CATEGORY_UNDERWEIGHT:
               resultCard.setCardBackgroundColor(Color.YELLOW);
               bmiTitle.setTextColor(Color.BLACK);
               bmiValue.setTextColor(Color.BLACK);
               bmiCategory.setTextColor(Color.BLACK);
               break;
           case BMI_CATEGORY_HEALTHY:
               resultCard.setCardBackgroundColor(Color.GREEN);
               bmiTitle.setTextColor(Color.WHITE);
               bmiValue.setTextColor(Color.WHITE);
               bmiCategory.setTextColor(Color.WHITE);
               break;
           case BMI_CATEGORY_OVERWEIGHT:
               resultCard.setCardBackgroundColor(Color.LTGRAY);
               bmiTitle.setTextColor(Color.BLACK);
               bmiValue.setTextColor(Color.BLACK);
               bmiCategory.setTextColor(Color.BLACK);
               break;
           case BMI_CATEGORY_OBESE:
               resultCard.setCardBackgroundColor(Color.RED);
               bmiTitle.setTextColor(Color.WHITE);
               bmiValue.setTextColor(Color.WHITE);
               bmiCategory.setTextColor(Color.WHITE);
               break;
       }


    }

}