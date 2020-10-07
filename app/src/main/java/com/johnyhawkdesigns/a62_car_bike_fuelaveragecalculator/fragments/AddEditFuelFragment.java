package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.FuelViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.VehicleViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddEditFuelFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = AddEditFuelFragment.class.getSimpleName();

    // constructor for edit
    public static AddEditFuelFragment newInstance(int foreignVehicleID, int fuelID) {
        AddEditFuelFragment addEditFuelFragment = new AddEditFuelFragment();
        Bundle args = new Bundle();
        args.putInt("foreignVehicleID", foreignVehicleID);
        args.putInt("fuelID", fuelID);
        addEditFuelFragment.setArguments(args);
        return addEditFuelFragment;
    }

    // constructor for add new
    public static AddEditFuelFragment newInstance(int fuelID) {
        AddEditFuelFragment addEditFuelFragment = new AddEditFuelFragment();
        Bundle args = new Bundle();
        args.putInt("fuelID", fuelID);
        addEditFuelFragment.setArguments(args);
        return addEditFuelFragment;
    }


    private boolean addingNewFuel = true; // adding (true) or editing (false)
    private int foreignVehicleID = 0;
    private int fuelID = 0;


    private FuelViewModel fuelViewModel;

    private TextView tv_title_fuel;
    private Button btn_setFuelDate;
    private TextView tv_input_fuel_date;
    private TextInputEditText tin_perLitrePrice;
    private TextInputEditText tin_fuelQuantityLitres;
    private TextInputEditText tin_totalFuelPrice;
    private TextInputEditText tin_currentKm;
    private TextInputEditText tin_startingKm;
    private TextInputEditText tin_distanceCovered;
    private TextInputEditText tin_calculatedAverage;
    private ImageButton btn_calculateFuelPrice;
    private ImageButton btn_calculateDistance;
    private ImageButton btn_calculateAverage;

    private FloatingActionButton fabSaveFuelData;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Date fuelDate;
    private LocalDate localDate; // Note: This is Java 8 feature
    String fuelDateString = "";
    String fuelDateStringLocalDate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_edit_fuel, container, false);

        tv_title_fuel = view.findViewById(R.id.tv_title_fuel);
        btn_setFuelDate = view.findViewById(R.id.btn_setFuelDate);
        tv_input_fuel_date = view.findViewById(R.id.tv_input_fuel_date);
        tin_perLitrePrice = view.findViewById(R.id.tin_perLitrePrice);
        tin_fuelQuantityLitres = view.findViewById(R.id.tin_fuelQuantityLitres);
        btn_calculateFuelPrice = view.findViewById(R.id.btn_calculateFuelPrice);
        tin_totalFuelPrice = view.findViewById(R.id.tin_totalFuelPrice);
        tin_currentKm = view.findViewById(R.id.tin_currentKm);
        tin_startingKm = view.findViewById(R.id.tin_startingKm);
        btn_calculateDistance = view.findViewById(R.id.btn_calculateDistance);
        tin_distanceCovered = view.findViewById(R.id.tin_distanceCovered);
        btn_calculateAverage = view.findViewById(R.id.btn_calculateAverage);
        tin_calculatedAverage = view.findViewById(R.id.tin_calculatedAverage);
        fabSaveFuelData = view.findViewById(R.id.fabSaveFuelData);

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );

        // get Bundle of arguments then extract the contact's Uri
        Bundle arguments = getArguments();

        // if we are adding new Vehicle, there should be no data in getArguments to assign to medID
        if (arguments != null) {

            // ================== Adding new fuel mode ==================
            if (arguments.get("foreignVehicleID") != null && arguments.get("fuelID") == null) {
                foreignVehicleID = arguments.getInt("foreignVehicleID");
                addingNewFuel = true;
                Log.d(TAG, "Adding New Fuel mode = received foreignVehicleID = " + foreignVehicleID);
                tv_title_fuel.setText("Adding New Fuel Record");

            }

            // ================== Editing fuel mode ==================
            if (arguments.get("fuelID") != null && arguments.get("foreignVehicleID") != null) {
                foreignVehicleID = arguments.getInt("foreignVehicleID");
                fuelID = arguments.getInt("fuelID");
                addingNewFuel = false;
                Log.d(TAG, "Editing mode = received foreignVehicleID = " + foreignVehicleID + ", fuelID = " + fuelID);
                tv_title_fuel.setText("Editing Fuel Record");

            }

            fuelViewModel = new FuelViewModel(getActivity().getApplication(), foreignVehicleID);

            // if editing fuel
            if (!addingNewFuel){
                fuelViewModel.getFuelByID(foreignVehicleID, fuelID)
                .observe(getActivity(), fuel -> {
                    Log.d(TAG, "onCreateView: fuel.getCalculatedAverage() = " + fuel.getCalculatedAverage());
                    // need to setup edit text views with this data
                });

            }

            // set date
            btn_setFuelDate.setOnClickListener(v -> {
                datePickerDialog.show();
            });


            // button to calculate (multiply) fuel price with fuel quantity
            btn_calculateFuelPrice.setOnClickListener(v -> {
               if (!tin_perLitrePrice.getText().toString().trim().isEmpty() && !tin_fuelQuantityLitres.getText().toString().trim().isEmpty()) {
                    int perLitrePrice = Integer.parseInt(tin_perLitrePrice.getText().toString());
                    int fuelQuantityLitres = Integer.parseInt(tin_fuelQuantityLitres.getText().toString());
                    int totalFuelPrice = fuelQuantityLitres * perLitrePrice;
                   tin_totalFuelPrice.setText(String.valueOf(totalFuelPrice));
               }
            });



            fabSaveFuelData.setOnClickListener(saveButtonClicked);
        }

        return view;
    }



    // When Save button is clicked
    private final View.OnClickListener saveButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Fuel fuel = new Fuel();
            fuel.setForeignVehicleID(foreignVehicleID); // foreign key is available in all cases
            if (!addingNewFuel){
                fuel.setFuelID(fuelID); // fuel id is only set if we are editing current fuel data with already available fuel id
            }


            if ( tin_perLitrePrice.getText().toString().trim().isEmpty() ||
                    tin_fuelQuantityLitres.getText().toString().trim().isEmpty() ||
                    tin_totalFuelPrice.getText().toString().trim().isEmpty() ||
                    tin_currentKm.getText().toString().trim().isEmpty() ||
                    tin_startingKm.getText().toString().trim().isEmpty() ||
                    tin_distanceCovered.getText().toString().trim().isEmpty() ||
                    tin_calculatedAverage.getText().toString().trim().isEmpty()  ) {

                Log.d(TAG, "onClick: any of the parameters is empty");
                AppUtils.showMessage(getActivity(), "Please fill all the details!");

            } else { //If no field is left empty and everything is filled

                startStoringData(fuel);
                Log.d(TAG, "onClick: start storing data");

                getDialog().dismiss();
                getParentFragmentManager().popBackStack(); // close detail fragment when editing is complete
            }



        }
    };


    private void startStoringData(Fuel fuel) {

        // if we are adding new  record
        if (addingNewFuel) {
            fuel.setForeignVehicleID(foreignVehicleID);


            fuelViewModel.insertFuel(fuel);

        } else { // if we are editing existing vehicle record

            fuel.setForeignVehicleID(foreignVehicleID);
            fuel.setFuelID(fuelID);
            fuelViewModel.updateVehicle(fuel);
        }
    }


    // Below code is used to fix small size issue of DialogFragment
    @Override
    public void onResume () {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        //compositeDisposable.dispose(); // dispose disposable
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        fuelDate = calendar.getTime();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android Oreo 8
            localDate = getLocalDate();
            fuelDateStringLocalDate = String.valueOf(localDate);
            Log.d(TAG, "onDateSet: fuelDateStringLocalDate = " + fuelDateStringLocalDate + " , from original localDate = " + localDate);
            tv_input_fuel_date.setText(fuelDateStringLocalDate);
        } else {
            fuelDateString = AppUtils.getFormattedDateString(fuelDate);
            Log.d(TAG, "onDateSet: fuelDateString = " + fuelDateString + " , from original fuelDate = " + fuelDate);
            tv_input_fuel_date.setText(fuelDateString);
        }


    }

    // Java 8 specific - requires minimum Android Oreo 8
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }
}