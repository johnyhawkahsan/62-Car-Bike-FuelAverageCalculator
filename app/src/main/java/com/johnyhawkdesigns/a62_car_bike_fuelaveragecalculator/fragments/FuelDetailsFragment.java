package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.FuelViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

import java.time.LocalDate;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

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
    private TextView tv_distanceCovered;
    private TextView tv_calculatedAverage;

    private ImageButton btn_edit_fuel;
    private ImageButton btn_delete_fuel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fuel_details, container, false);

        tv_perLitrePrice = view.findViewById(R.id.tv_perLitrePrice);
        tv_fuelQuantityLitres = view.findViewById(R.id.tv_fuelQuantityLitres);
        tv_totalFuelPrice = view.findViewById(R.id.tv_totalFuelPrice);
        tv_distanceCovered = view.findViewById(R.id.tv_distanceCovered);
        tv_calculatedAverage = view.findViewById(R.id.tv_calculatedAverage);
        btn_edit_fuel = view.findViewById(R.id.btn_edit_fuel);
        btn_delete_fuel = view.findViewById(R.id.btn_delete_fuel);
        btn_edit_fuel.setOnClickListener(editButtonClicked);
        btn_delete_fuel.setOnClickListener(deleteButtonClicked);


        // get Bundle of arguments
        Bundle arguments = getArguments();

        if (arguments != null) {

            if (arguments.get("fuelID") != null && arguments.get("foreignVehicleID") != null) {
                foreignVehicleID = arguments.getInt("foreignVehicleID");
                fuelID = arguments.getInt("fuelID");
                Log.d(TAG, "Details mode = received foreignVehicleID = " + foreignVehicleID + ", fuelID = " + fuelID);
            }

            fuelViewModel = new FuelViewModel(getActivity().getApplication(), foreignVehicleID);

        }

        fuelViewModel.getFuelByID(foreignVehicleID, fuelID)
                .observe(getActivity(), fuel -> {
                    Log.d(TAG, "View Fuel Details : fuelID = " + fuelID);
                    populateFuelData(fuel);

                });



        return view;

    }


    private final View.OnClickListener editButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: editButtonClicked");
            // Launch AddEditFuelFragment

            /*
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            AddEditFuelFragment addEditFuelFragment = AddEditFuelFragment.newInstance(foreignVehicleID, fuelID); // launch
            addEditFuelFragment.show(fragmentManager, "add_edit_fuel_fragment");
            */
        }
    };



    private final View.OnClickListener deleteButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: deleteButtonClicked");

            // Build alert dialog for confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Are you sure??");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppUtils.showMessage(getActivity(), "Delete fuel record success");

                    // close dialog and close fragment
                    fuelViewModel.deleteFuelWithID(foreignVehicleID, fuelID);
                    getDialog().dismiss();
                    getParentFragmentManager().popBackStack();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog ad = builder.create();
            ad.show();


        }
    };


    // display fuel data inside TextViews
    private void populateFuelData(Fuel fuel) {
        tv_perLitrePrice.setText(fuel.getPerLitrePrice().toString());
        tv_fuelQuantityLitres.setText(fuel.getFuelQuantityLitres().toString());
        tv_totalFuelPrice.setText(fuel.getTotalFuelPrice().toString());
        tv_distanceCovered.setText(fuel.getDistanceCovered().toString());
        tv_calculatedAverage.setText(fuel.getCalculatedAverage().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remove observer here
    }
}