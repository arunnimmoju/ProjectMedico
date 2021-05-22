package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class updateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        String[] gender = new String[] {"Male","Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, gender);
        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.updateGender);
        editTextFilledExposedDropdown.setAdapter(adapter);

        String[] bloodPressure = new String[] {"Low","High","Normal"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, bloodPressure);
        AutoCompleteTextView editTextFilledExposedDropdown1 = findViewById(R.id.updateBp);
        editTextFilledExposedDropdown1.setAdapter(adapter1);




    }
}