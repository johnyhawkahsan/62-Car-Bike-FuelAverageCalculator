package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddEditVehicleFragment extends DialogFragment {

    private static final String TAG = AddEditVehicleFragment.class.getSimpleName();

    // constructor for edit
    public static AddEditVehicleFragment newInstance(int vehicleID) {
        AddEditVehicleFragment addEditVehicleFragment = new AddEditVehicleFragment();
        Bundle args = new Bundle();
        args.putInt("vehicleID", vehicleID);
        addEditVehicleFragment.setArguments(args);
        return addEditVehicleFragment;
    }

    // constructor for add new
    public static AddEditVehicleFragment newInstance() {
        AddEditVehicleFragment addEditVehicleFragment = new AddEditVehicleFragment();
        return addEditVehicleFragment;
    }

    private VehicleViewModel vehicleViewModel;
    private TextView textViewTitle;
    private TextInputEditText textInputMake;
    private TextInputEditText textInputModel;
    private TextInputEditText textInputFuelCapacity;
    private RadioGroup radioButtonGroupVehicle;
    private RadioButton radioButton_car;
    private RadioButton radioButton_bike;
    private FloatingActionButton fabSaveVehicle;

    private boolean addingNewVehicle = true; // adding (true) or editing (false)
    private int vehicleID = 0;

    private String vehicleType;
    private String vehicleMake = "";
    private String vehicleModel = "";
    private String vehicleFuelCapacity = "";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_edit_vehicle_fragment, container, false);

        textViewTitle = view.findViewById(R.id.text_title);
        textInputMake = view.findViewById(R.id.textInputMake);
        textInputModel = view.findViewById(R.id.textInputModel);
        textInputFuelCapacity = view.findViewById(R.id.textInputFuelCapacity);
        radioButtonGroupVehicle = view.findViewById(R.id.radioButtonGroupVehicle);
        radioButton_car = view.findViewById(R.id.radioButton_car);
        radioButton_bike = view.findViewById(R.id.radioButton_bike);
        fabSaveVehicle = view.findViewById(R.id.fabSaveVehicle);


        vehicleViewModel = new VehicleViewModel(getActivity().getApplication());

        // get Bundle of arguments then extract the contact's Uri
        Bundle arguments = getArguments();

        // if we are adding new Vehicle, there should be no data in getArguments to assign to medID
        if (arguments != null) {

            if (arguments.get("vehicleID") != null) {
                vehicleID = arguments.getInt("vehicleID");
            }

            addingNewVehicle = false;
            textViewTitle.setText("Editing Vehicle Record");

            Log.d(TAG, "Editing mode = received vehicleID = " + vehicleID + ", addingNewVehicle = false");


            // -====================== Need to add composite disposable here===============================//

            Disposable disposable =
            vehicleViewModel.getVehicle(vehicleID)
                    .subscribeOn(Schedulers.io())
                    //.map(vehicle1 -> {
                    //   return vehicle1 = vehicle;
                    //})
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(vehicle -> {

                        Log.d(TAG, "onCreateView: vehicle.getVehicleMake() = " + vehicle.getVehicleMake() + ", vehicle.getVehicleModel = " + vehicle.getVehicleModel());

                        vehicleType = vehicle.getVehicleType();
                        vehicleMake = vehicle.getVehicleMake();
                        vehicleModel = vehicle.getVehicleModel();


                        if (vehicle.getVehicleType().equals("car")) {
                            radioButton_car.setChecked(true);
                            toggleButtonBackground(radioButton_car, radioButton_bike);
                        } else {
                            radioButton_bike.setChecked(true);
                            toggleButtonBackground(radioButton_bike, radioButton_car);
                        }

                        // worked after adding composite disposable
                        textInputMake.setText(vehicle.getVehicleMake());
                        textInputModel.setText(vehicle.getVehicleModel());
                        textInputFuelCapacity.setText(String.valueOf(vehicle.getVehicleFuelCapacity()));

                    });



            compositeDisposable.add(disposable);


        } else { // adding new record

            Log.d(TAG, "Adding new record mode, addingNewVehicle = true");
            addingNewVehicle = true;
            vehicleType = "car"; // set default value for vehicleName for radioButton to work properly
            textViewTitle.setText("Adding New Vehicle");
            radioButton_car.setBackgroundResource(R.drawable.selected_state); // by default, we want car button to be selected
        }


        radioButtonGroupVehicle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_car:
                        vehicleType = "car";
                        toggleButtonBackground(radioButton_car, radioButton_bike);
                        Log.d(TAG, "onCheckedChanged: radioButton_car vehicleName = " + vehicleType);
                        break;
                    case R.id.radioButton_bike:
                        vehicleType = "bike";
                        toggleButtonBackground(radioButton_bike, radioButton_car);
                        Log.d(TAG, "onCheckedChanged: radioButton_bike vehicleName = " + vehicleType);
                        break;
                }
            }
        });


        fabSaveVehicle.setOnClickListener(saveButtonClicked);


        return view;
    }

    private void toggleButtonBackground(RadioButton activeButton, RadioButton nonActiveButton) {
        activeButton.setBackgroundResource(R.drawable.selected_state);
        nonActiveButton.setBackgroundResource(R.drawable.regular_state);
    }

    // When Save button is clicked
    private final View.OnClickListener saveButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Vehicle vehicle = new Vehicle();

            vehicleMake = textInputMake.getText().toString();
            vehicleModel = textInputModel.getText().toString();
            vehicleFuelCapacity = textInputFuelCapacity.getText().toString();

            if (vehicleType.isEmpty() || vehicleMake.trim().isEmpty() || vehicleModel.trim().isEmpty() || vehicleFuelCapacity.trim().isEmpty()) { // vehicleMake.length() == 0  old approach
                Log.d(TAG, "onClick: any of the parameters is empty");
                AppUtils.showMessage(getActivity(), "Please fill all details!");
            }
            else { //If no field is left empty and everything is filled

                vehicle.setVehicleType(vehicleType);
                vehicle.setVehicleMake(vehicleMake);
                vehicle.setVehicleModel(vehicleModel);
                vehicle.setVehicleFuelCapacity(Double.parseDouble(vehicleFuelCapacity));


                Log.d(TAG, "onClick: vehicleType = " + vehicleType + ", vehicleMake = " + vehicleMake + ", vehicleModel =" + vehicleModel);

                // if we are adding new vehicle record
                if (addingNewVehicle) {
                    Log.d(TAG, "onClick: addingNewVehicle");
                    vehicleViewModel.insertVehicle(vehicle);

                } else {
                    // if we are editing existing vehicle record
                    vehicle.setVehicleID(vehicleID);
                    vehicleViewModel.updateVehicle(vehicle);
                }


                getDialog().dismiss();
                getParentFragmentManager().popBackStack(); // close detail fragment when editing is complete
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose(); // dispose disposable
    }
}
