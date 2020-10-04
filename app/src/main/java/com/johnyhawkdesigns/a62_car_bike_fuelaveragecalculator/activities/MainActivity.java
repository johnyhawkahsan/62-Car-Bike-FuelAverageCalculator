package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.AddEditVehicleFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.VehicleDetailsFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.VehicleListFragment;

public class MainActivity extends AppCompatActivity 
            implements VehicleListFragment.VehicleListFragmentListener, VehicleDetailsFragment.VehicleDetailsFragmentListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private VehicleListFragment vehicleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vehicleListFragment = new VehicleListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.vehicleFragmentContainer, vehicleListFragment);
        transaction.commit();

    }

    @Override
    public void onAddNewVehicle() {
        Log.d(TAG, "onAddVehicle: FAB Add button Clicked");

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditVehicleFragment addEditVehicleFragment = AddEditVehicleFragment.newInstance("Add Vehicle");
        addEditVehicleFragment.show(fragmentManager, "add_edit_vehicle_fragment");

    }

    @Override
    public void onVehicleSelected(int vehicleID) {

        Log.d(TAG, "onVehicleSelected: id received inside MainActivity = " + vehicleID);

        VehicleDetailsFragment vehicleDetailsFragment = new VehicleDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("vehicleID", vehicleID);
        vehicleDetailsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.vehicleFragmentContainer, vehicleDetailsFragment);
        transaction.addToBackStack(null); // now if in detailFragment, we press back button, app does not close.
        transaction.commit();

    }


    @Override
    public void onVehicleDeleted() {

    }

    @Override
    public void onEditVehicle(int vehicleID) {

    }
}