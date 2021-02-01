package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.FuelViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddEditFuelFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
    public static AddEditFuelFragment newInstance(int foreignVehicleID) {
        AddEditFuelFragment addEditFuelFragment = new AddEditFuelFragment();
        Bundle args = new Bundle();
        args.putInt("foreignVehicleID", foreignVehicleID);
        addEditFuelFragment.setArguments(args);
        return addEditFuelFragment;
    }


    private boolean addingNewFuel = true; // adding (true) or editing (false)
    private int foreignVehicleID = 0;
    private int fuelID = 0;
    private Fuel fuel;


    private FuelViewModel fuelViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
    private String fuelDateString = "";
    private String fuelDateStringLocalDate = "";


    private Double perLitrePrice;
    private Double fuelQuantityLitres;
    private Double totalFuelPrice;
    private Double currentKm;
    private Double startingKm;
    private Double totalDistance;
    private Double calculatedAverage;

    private List<TextInputEditText> textInputValidationList;

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
        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


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
            else if (arguments.get("fuelID") != null && arguments.get("foreignVehicleID") != null) {
                foreignVehicleID = arguments.getInt("foreignVehicleID");
                fuelID = arguments.getInt("fuelID");
                addingNewFuel = false;
                Log.d(TAG, "Editing mode = received foreignVehicleID = " + foreignVehicleID + ", fuelID = " + fuelID);
                tv_title_fuel.setText("Editing Fuel Record");
            }

            fuelViewModel = new FuelViewModel(getActivity().getApplication(), foreignVehicleID);
        }

        // if editing fuel, need to get current fuel data and populate in our view
        if (!addingNewFuel) {
            Log.d(TAG, "onCreateView: editing mode");


            Disposable disposable = fuelViewModel.getFuelByID(foreignVehicleID, fuelID)
                    .subscribeOn(Schedulers.io())
                    .subscribe(storedFuel -> {
                        //this.fuel = fuel; // set global vehicle object to returned/searched vehicle object
                        Log.d(TAG, "editing mode : storedFuel.getCalculatedAverage = " + storedFuel.getCalculatedAverage());
                        populateFuelData(storedFuel);
                        fabSaveFuelData.hide();
                    });

            compositeDisposable.add(disposable);

            monitorTextChange(); // monitor touch on edit text and when touched, enable save button


        } else if (addingNewFuel) {
            Log.d(TAG, "onCreateView: addingNewFuel");
            fuelDate = AppUtils.getCurrentDateTime();
            fuelDateString = AppUtils.getFormattedDateString(fuelDate);
            Log.d(TAG, "onDateSet: fuelDateString = " + fuelDateString + " , from original fuelDate = " + fuelDate);
            tv_input_fuel_date.setText(fuelDateString);
        }

        // set date
        btn_setFuelDate.setOnClickListener(v -> {
            datePickerDialog.show();
        });

        // get data from shared preferences
        Double lastPerLitrePrice = AppUtils.getPetrolPerLitrePrice(AppUtils.perLitrePriceStr, getActivity());
        if (lastPerLitrePrice != null && !addingNewFuel) { // and not in editing mode
            tin_perLitrePrice.setText(String.valueOf(lastPerLitrePrice));
        }

        // button to calculate (multiply) fuel price with fuel quantity
        btn_calculateFuelPrice.setOnClickListener(v -> {
            if (!tin_perLitrePrice.getText().toString().trim().isEmpty() && !tin_fuelQuantityLitres.getText().toString().trim().isEmpty()) {
                perLitrePrice = Double.parseDouble(tin_perLitrePrice.getText().toString());
                fuelQuantityLitres = Double.parseDouble(tin_fuelQuantityLitres.getText().toString());
                totalFuelPrice = fuelQuantityLitres * perLitrePrice;
                tin_totalFuelPrice.setText(String.valueOf(totalFuelPrice));


            } else if (!tin_perLitrePrice.getText().toString().trim().isEmpty() && !tin_totalFuelPrice.getText().toString().trim().isEmpty()) {
                perLitrePrice = Double.parseDouble(tin_perLitrePrice.getText().toString());
                totalFuelPrice = Double.parseDouble(tin_totalFuelPrice.getText().toString());
                fuelQuantityLitres = (totalFuelPrice / perLitrePrice);
                fuelQuantityLitres = AppUtils.roundDouble(fuelQuantityLitres, 2); // round to 2 decimal places
                tin_fuelQuantityLitres.setText(String.valueOf(fuelQuantityLitres));
            }

            // store perLitrePrice to shared preferences
            if (!tin_perLitrePrice.getText().toString().trim().isEmpty()) {
                perLitrePrice = Double.parseDouble(tin_perLitrePrice.getText().toString());
                AppUtils.savePetrolPriceSharedPreference(AppUtils.perLitrePriceStr, perLitrePrice, getActivity());
            }
        });

        // button to calculate (minus) current distance with previous distance
        btn_calculateDistance.setOnClickListener(v -> {
            if (!tin_currentKm.getText().toString().trim().isEmpty() && !tin_startingKm.getText().toString().trim().isEmpty()) {
                currentKm = Double.parseDouble(tin_currentKm.getText().toString());
                startingKm = Double.parseDouble(tin_startingKm.getText().toString());
                totalDistance = currentKm - startingKm;
                tin_distanceCovered.setText(String.valueOf(totalDistance));
            }
        });

        // button to calculate average Total distance covered in km divided by total petrol in litres i.e; 172km/ 5 ltr = 43 km per litre average
        btn_calculateAverage.setOnClickListener(v -> {
            if (!tin_fuelQuantityLitres.getText().toString().trim().isEmpty() && !tin_distanceCovered.getText().toString().trim().isEmpty()) {
                Log.d(TAG, "onCreateView: totalDistance = " + totalDistance + ", fuelQuantityLitres = " + fuelQuantityLitres);

                // retrieving double values from input edit text again to avoid error caused in editing mode
                totalDistance = Double.parseDouble(tin_distanceCovered.getText().toString());
                fuelQuantityLitres = Double.parseDouble(tin_fuelQuantityLitres.getText().toString());

                calculatedAverage = (totalDistance / fuelQuantityLitres);
                calculatedAverage = AppUtils.roundDouble(calculatedAverage, 2); // round to 2 decimal places
                tin_calculatedAverage.setText(String.valueOf(calculatedAverage));
                // store calculatedAverage in SharedPreferences as last vehicle mileage

            }
        });


        fabSaveFuelData.setOnClickListener(saveButtonClicked);


        return view;
    }


    // display fuel data inside TextInputEditText
    private void populateFuelData(Fuel fuel) {
        String perLitrePrice = fuel.getPerLitrePrice().toString();
        String fuelQuantityLitres = fuel.getFuelQuantityLitres().toString();


        Log.d(TAG, "perLitrePrice: " + perLitrePrice);
        Log.d(TAG, "fuelQuantityLitres: " + fuelQuantityLitres);

        //tin_perLitrePrice.setText("123");
        //tin_fuelQuantityLitres.setText(fuelQuantityLitres);

/*
        tin_totalFuelPrice.setText(fuel.getTotalFuelPrice().toString());
        tin_currentKm.setText(fuel.getCurrentKm().toString());
        tin_startingKm.setText(fuel.getStartingKm().toString());
        tin_distanceCovered.setText(fuel.getDistanceCovered().toString());
        tin_calculatedAverage.setText(fuel.getCalculatedAverage().toString());
        */

    }


    // When Save button is clicked
    private final View.OnClickListener saveButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            fuel = new Fuel();
            fuel.setForeignVehicleID(foreignVehicleID); // foreign key is available in all cases
            if (!addingNewFuel) {
                fuel.setFuelID(fuelID); // fuel id is only set if we are editing current fuel data with already available fuel id
            }

            // if all fields are not filled
            if (checkForEmptyList()) {

                Log.d(TAG, "onClick: any of the parameters is empty");
                AppUtils.showMessage(getActivity(), "Please fill all the details!");

            } else { //If no field is left empty and everything is filled

                Double perLitrePrice = Double.valueOf(tin_perLitrePrice.getText().toString());
                Double fuelQuantityLitres = Double.valueOf(tin_fuelQuantityLitres.getText().toString());
                Double totalFuelPrice = Double.valueOf(tin_totalFuelPrice.getText().toString());
                Double startingKm = Double.valueOf(tin_startingKm.getText().toString());
                Double currentKm = Double.valueOf(tin_currentKm.getText().toString());
                Double distanceCovered = Double.valueOf(tin_distanceCovered.getText().toString());
                Double calculatedAverage = Double.valueOf(tin_calculatedAverage.getText().toString());

                // set data fields for fuel
                if (fuelDate == null) { // means no default date is set
                    fuelDate = AppUtils.getCurrentDateTime();
                    tv_input_fuel_date.setText(AppUtils.getFormattedDateString(fuelDate));
                }

                fuel.setFuelDate(fuelDate);
                fuel.setPerLitrePrice(perLitrePrice);
                fuel.setFuelQuantityLitres(fuelQuantityLitres);
                fuel.setTotalFuelPrice(totalFuelPrice);
                fuel.setStartingKm(startingKm);
                fuel.setCurrentKm(currentKm);
                fuel.setDistanceCovered(distanceCovered);
                fuel.setCalculatedAverage(calculatedAverage);


                startStoringData(fuel);
                Log.d(TAG, "onClick: start storing data");

                getDialog().dismiss();
                getParentFragmentManager().popBackStack(); // close detail fragment when editing is complete
            }


        }
    };


    // method to store fuel data to DB
    private void startStoringData(Fuel fuel) {
        // if we are adding new  record
        if (addingNewFuel) {
            fuelViewModel.insertFuel(fuel);
        } else { // if we are editing existing vehicle record
            fuelViewModel.updateVehicle(fuel);
        }
    }


    // method to check for all edit text
    private Boolean checkForEmptyList() {
        Boolean emptyListAvailable = tin_perLitrePrice.getText().toString().trim().isEmpty() ||
                tin_fuelQuantityLitres.getText().toString().trim().isEmpty() ||
                tin_totalFuelPrice.getText().toString().trim().isEmpty() ||
                tin_currentKm.getText().toString().trim().isEmpty() ||
                tin_startingKm.getText().toString().trim().isEmpty() ||
                tin_distanceCovered.getText().toString().trim().isEmpty() ||
                tin_calculatedAverage.getText().toString().trim().isEmpty();

        if (emptyListAvailable) {
            validationList(); // set error messages
        }
        Log.d(TAG, "checkForEmptyList: emptyListAvailable = " + emptyListAvailable);
        return emptyListAvailable;
    }


    // iterate through all items in the list and look for empty items
    private void validationList() {
        textInputValidationList = new ArrayList<>();
        textInputValidationList.add(tin_perLitrePrice);
        textInputValidationList.add(tin_fuelQuantityLitres);
        textInputValidationList.add(tin_currentKm);
        textInputValidationList.add(tin_startingKm);
        textInputValidationList.add(tin_distanceCovered);
        textInputValidationList.add(tin_calculatedAverage);

        for (int i = 0; i < textInputValidationList.size(); i++) {

            Log.d(TAG, "textInputValidationList.size(): = " + textInputValidationList.size());
            String string = textInputValidationList.get(i).getText().toString().trim();

            if (string.isEmpty()) {
                textInputValidationList.get(i).setError("EMPTY");
            } else {
                textInputValidationList.get(i).setError(null);
            }

        }
    }


    // disable all TextInputEditText for fuel detail mode
    private void disableAllTextInputEditText() {
        List<TextInputEditText> textInputEditTextList = new ArrayList<>();
        textInputEditTextList.add(tin_perLitrePrice);
        textInputEditTextList.add(tin_fuelQuantityLitres);
        textInputEditTextList.add(tin_totalFuelPrice);
        textInputEditTextList.add(tin_currentKm);
        textInputEditTextList.add(tin_startingKm);
        textInputEditTextList.add(tin_distanceCovered);
        textInputEditTextList.add(tin_calculatedAverage);

        for (TextInputEditText item : textInputEditTextList) {
            item.setEnabled(false);
        }
    }

    // enable all TextInputEditText for fuel editing mode
    private void enableAllTextInputEditText() {
        List<TextInputEditText> textInputEditTextList = new ArrayList<>();
        textInputEditTextList.add(tin_perLitrePrice);
        textInputEditTextList.add(tin_fuelQuantityLitres);
        textInputEditTextList.add(tin_totalFuelPrice);
        textInputEditTextList.add(tin_currentKm);
        textInputEditTextList.add(tin_startingKm);
        textInputEditTextList.add(tin_distanceCovered);
        textInputEditTextList.add(tin_calculatedAverage);

        for (TextInputEditText item : textInputEditTextList) {
            item.setEnabled(true);
        }
    }

    // check for touch event on editText to enable save button
    private void monitorTextChange() {
        List<TextInputEditText> textInputEditTextList = new ArrayList<>();
        //textInputEditTextList.add(tin_perLitrePrice); // first line is selected automatically
        textInputEditTextList.add(tin_fuelQuantityLitres);
        textInputEditTextList.add(tin_totalFuelPrice);
        textInputEditTextList.add(tin_currentKm);
        textInputEditTextList.add(tin_startingKm);
        textInputEditTextList.add(tin_distanceCovered);
        textInputEditTextList.add(tin_calculatedAverage);

        for (TextInputEditText item : textInputEditTextList) {


            item.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d(TAG, "afterTextChanged: ");
                    fabSaveFuelData.show();
                }
            });


        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose(); // dispose disposable
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        fuelDate = calendar.getTime();
        Log.d(TAG, "onDateSet: fuelDate = " + fuelDate);


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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeDisposable.dispose(); // dispose disposable
    }
}