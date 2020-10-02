package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.VehicleViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

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

    private VehicleViewModel vehicleViewModel;
    private TextView textViewTitle;
    private TextInputEditText textInputMake;
    private TextInputEditText textInputModel;
    private RadioGroup radioButtonGroupVehicle;
    private FloatingActionButton fabSaveVehicle;

    private boolean addingNewVehicle = true; // adding (true) or editing (false)
    private int vehicleID = 0;

    private String vehicleType;
    private String vehicleMake = "";
    private String vehicleModel = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.add_vehicle_fragment, container, false);

        vehicleViewModel = new VehicleViewModel(getActivity().getApplication());

        textViewTitle = view.findViewById(R.id.text_title);
        textInputMake = view.findViewById(R.id.textInputMake);
        textInputModel = view.findViewById(R.id.textInputModel);
        radioButtonGroupVehicle = view.findViewById(R.id.radioButtonGroupVehicle);
        fabSaveVehicle = view.findViewById(R.id.fabSaveVehicle);


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Add Vehicle");
        if (title != null){
            textViewTitle.setText(title);
        }


        // Show soft keyboard automatically and request focus to field
        textInputMake.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        vehicleType = "car"; // set default value for vehicleName for radioButton to work properly

        radioButtonGroupVehicle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton_car:
                        vehicleType = "car";
                        Log.d(TAG, "onCheckedChanged: radioButton_car vehicleName = " + vehicleType);
                        break;
                    case R.id.radioButton_bike:
                        vehicleType = "bike";
                        Log.d(TAG, "onCheckedChanged: radioButton_bike vehicleName = " + vehicleType);
                        break;
                }
            }
        });

        fabSaveVehicle.setOnClickListener(saveButtonClicked);


        return view;
    }

    // When Save button is clicked
    private final View.OnClickListener saveButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            vehicleMake  = textInputMake.getText().toString();
            vehicleModel  = textInputModel.getText().toString();

            if (vehicleType.isEmpty() || vehicleMake.trim().isEmpty() || vehicleModel.trim().isEmpty() ){ // vehicleMake.length() == 0  old approach
                Log.d(TAG, "onClick: any of the parameters is empty");
                AppUtils.showMessage(getActivity(), "Please fill all details!");
            } else {
                Log.d(TAG, "onClick: vehicleType = "  + vehicleType +  ", vehicleMake = " + vehicleMake +  ", vehicleModel =" + vehicleModel);
                Log.d(TAG, "onClick: save data to database");

                // if we are adding new vehicle record
                if (addingNewVehicle){
                    Log.d(TAG, "onClick: addingNewVehicle");

                    Vehicle newVehicle = new Vehicle();
                    newVehicle.setVehicleType(vehicleType);
                    newVehicle.setVehicleMake(vehicleMake);
                    newVehicle.setVehicleModel(vehicleModel);
                    vehicleViewModel.insertVehicle(newVehicle);

                } else {
                    // if we are editing existing vehicle record

                }


                getDialog().dismiss();
            }

        }
    };



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
