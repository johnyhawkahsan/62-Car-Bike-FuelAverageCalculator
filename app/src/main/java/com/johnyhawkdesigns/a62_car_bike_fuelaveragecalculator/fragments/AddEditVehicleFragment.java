package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddEditVehicleFragment extends DialogFragment {

    private static final String TAG = AddEditVehicleFragment.class.getSimpleName();

    public static AddEditVehicleFragment newInstance(String title){
        AddEditVehicleFragment fragment = new AddEditVehicleFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    private TextInputEditText textInputMake;
    private TextInputEditText textInputModel;
    private RadioGroup radioButtonGroupVehicle;

    private String vehicleType;
    private String vehicleName;
    private String vehicleModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Add Vehicle");

        if (title != null){
            getDialog().setTitle(title);
        }


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.add_vehicle_fragment, container, false);

        textInputMake = view.findViewById(R.id.textInputMake);
        textInputModel = view.findViewById(R.id.textInputModel);
        radioButtonGroupVehicle = view.findViewById(R.id.radioButtonGroupVehicle);

        // Show soft keyboard automatically and request focus to field
        textInputMake.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        vehicleName = "Car";

        radioButtonGroupVehicle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton_car:
                        vehicleName = "Car";
                        Log.d(TAG, "onCheckedChanged: radioButton_car vehicleName = " + vehicleName);
                        break;
                    case R.id.radioButton_bike:
                        vehicleName = "Bike";
                        Log.d(TAG, "onCheckedChanged: radioButton_bike vehicleName = " + vehicleName);
                        break;
                }
            }
        });


        return view;
    }



    // Below code is used to fix small size issue of DialogFragment
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


    }
}
