package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.VehicleViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.AddEditVehicleFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.VehicleDetailsFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.VehicleListFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

public class MainActivity extends AppCompatActivity 
            implements VehicleListFragment.VehicleListFragmentListener, VehicleDetailsFragment.VehicleDetailsFragmentListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    VehicleViewModel vehicleViewModel;

    private VehicleListFragment vehicleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vehicleViewModel = new VehicleViewModel(getApplication());

        vehicleListFragment = new VehicleListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.vehicleFragmentContainer, vehicleListFragment);
        transaction.commit();

    }

    @Override
    public void onAddNewVehicle() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditVehicleFragment addEditVehicleFragment = AddEditVehicleFragment.newInstance(); // here we are passing nothing in newInstance
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
        Log.d(TAG, "onVehicleDeleted: ");
    }

    @Override
    public void onEditVehicle(int vehicleID) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditVehicleFragment addEditVehicleFragment = AddEditVehicleFragment.newInstance(vehicleID);
        addEditVehicleFragment.show(fragmentManager, "add_edit_vehicle_fragment");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_delete_all:

                // Build alert dialog for confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppUtils.showMessage(getBaseContext(), "Delete all vehicles success");
                        vehicleViewModel.deleteAllVehicle();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}