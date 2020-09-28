package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView emptyTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewVehicle);
        //recyclerView.setLayoutManager(new GridLayoutManager(this));
        // carBikeListAdapter = new CarBikeListAdapter // constructor for adapter
        //recyclerView.setAdapter(carBikeListAdapter);

        emptyTextView = findViewById(R.id.tv__empty);

/*
        // get all vehicles list using view model
        if (carVehicleList.size() > 0) {
            emptyTextView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
        }
*/


        floatingActionButton = findViewById(R.id.fabAddVehicle);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(MainActivity.this, AddEditVehicleActivity.class);
                // startActivityForResult(intent, RC_CREATE_VEHICLE);
            }
        });


    }
}