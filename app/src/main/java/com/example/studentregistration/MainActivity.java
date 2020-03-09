package com.example.studentregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //the spinner for the branches

        Spinner branch_spinner = findViewById(R.id.branch_spinner);

        ArrayAdapter<String> branch_adapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.branch_array));
        branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch_spinner.setAdapter(branch_adapter);

            //the spinner for the Years
        Spinner years_spinner = findViewById(R.id.year_spinner);

        ArrayAdapter<String> years_adapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year_array));
        years_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years_spinner.setAdapter(years_adapter);


        //the spinner for the Semesters
        Spinner semester_spinner = findViewById(R.id.semseter_spinner);

        ArrayAdapter<String> semester_adapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.semester_array));
        semester_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester_spinner.setAdapter(semester_adapter);


    }
}
