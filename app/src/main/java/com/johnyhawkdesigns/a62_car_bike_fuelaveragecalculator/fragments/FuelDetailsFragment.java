package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.FuelViewModel;

import java.time.LocalDate;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FuelDetailsFragment extends DialogFragment {

    private static final String TAG = AddEditFuelFragment.class.getSimpleName();

    // constructor
    public static FuelDetailsFragment newInstance(int foreignVehicleID, int fuelID) {
        FuelDetailsFragment fuelDetailsFragment = new FuelDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("foreignVehicleID", foreignVehicleID);
        args.putInt("fuelID", fuelID);
        fuelDetailsFragment.setArguments(args);
        return fuelDetailsFragment;
    }


    private int foreignVehicleID = 0;
    private int fuelID = 0;
    private Fuel fuel;

    private FuelViewModel fuelViewModel;

    private TextView tv_perLitrePrice;
    private TextView tv_fuelQuantityLitres;
    private TextView tv_totalFuelPrice;
    private TextView tv_currentKm;
    private TextView tv_startingKm;
    private TextView tv_distanceCovered;
    private TextView tv_calculatedAverage;

    private ImageButton btn_edit_fuel;
    private ImageButton btn_delete_fuel;


    private Date fuelDate;
    private LocalDate localDate; // Note: This is Java 8 feature
    private String fuelDateString = "";
    private String fuelDateStringLocalDate = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fuel_details, container, false);

        tv_perLitrePrice = view.findViewById(R.id.tv_perLitrePrice);
        tv_fuelQuantityLitres = view.findViewById(R.id.tv_fuelQuantityLitres);
        tv_totalFuelPrice = view.findViewById(R.id.tv_totalFuelPrice);
        tv_currentKm = view.findViewById(R.id.tv_currentKm);
        tv_startingKm = view.findViewById(R.id.tv_startingKm);
        tv_distanceCovered = view.findViewById(R.id.tv_distanceCovered);
        tv_calculatedAverage = view.findViewById(R.id.tv_calculatedAverage);
        btn_edit_fuel = view.findViewById(R.id.btn_edit_fuel);
        btn_delete_fuel = view.findViewById(R.id.btn_delete_fuel);

        // get Bundle of arguments
        Bundle arguments = getArguments();

        if (arguments != null) {

            // ================== Fuel Details mode ==================
            if (arguments.get("fuelID") != null && arguments.get("foreignVehicleID") != null) {
                foreignVehicleID = arguments.getInt("foreignVehicleID");
                fuelID = arguments.getInt("fuelID");
                Log.d(TAG, "Details mode = received foreignVehicleID = " + foreignVehicleID + ", fuelID = " + fuelID);
            }

            fuelViewModel = new FuelViewModel(getActivity().getApplication(), foreignVehicleID);

            fuelViewModel.getFuelByID(foreignVehicleID, fuelID)
                    .observe(getActivity(), fuel -> {
                        Log.d(TAG, "View Fuel Details : fuelID = " + fuelID);
                        populateFuelData(fuel);

                    });
        }

        return view;

    }


    // display fuel data inside TextViews
    private void populateFuelData(Fuel fuel) {
        tv_perLitrePrice.setText(fuel.getPerLitrePrice().toString());
        tv_fuelQuantityLitres.setText(fuel.getFuelQuantityLitres().toString());
        tv_totalFuelPrice.setText(fuel.getTotalFuelPrice().toString());
        tv_currentKm.setText(fuel.getCurrentKm().toString());
        tv_startingKm.setText(fuel.getStartingKm().toString());
        tv_distanceCovered.setText(fuel.getDistanceCovered().toString());
        tv_calculatedAverage.setText(fuel.getCalculatedAverage().toString());
    }


}