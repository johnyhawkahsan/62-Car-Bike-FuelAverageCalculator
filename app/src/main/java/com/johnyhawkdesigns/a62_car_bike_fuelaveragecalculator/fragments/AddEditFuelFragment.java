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
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.VehicleViewModel;
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
    public static AddEditFuelFragment editInstance(Fuel fuel) {
        AddEditFuelFragment addEditFuelFragment = new AddEditFuelFragment();
        Bundle args = new Bundle();
        args.putSerializable("fuel", fuel);
        addEditFuelFragment.setArguments(args);
        return addEditFuelFragment;
    }

    // constructor for add new
    public static AddEditFuelFragment newInstance(int foreignVehicleID, int vehicleFuelCapacity) {
        AddEditFuelFragment addEditFuelFragment = new AddEditFuelFragment();
        Bundle args = new Bundle();
        args.putInt("foreignVehicleID", foreignVehicleID);
        args.putInt("vehicleFuelCapacity", vehicleFuelCapacity);
        addEditFuelFragment.setArguments(args);
        return addEditFuelFragment;
    }


    private boolean addingNewFuel = true; // adding (true) or editing (false)
    private int foreignVehicleID = 0;


    private int fuelID = 0;
    private Fuel fuel;


    private FuelViewModel fuelViewModel;
    private VehicleViewModel vehicleViewModel;
    //private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView tv_title_fuel;
    private Button btn_setFuelDate;
    private TextView tv_input_fuel_date;
    private TextInputEditText tin_perLitrePrice;
    private TextInputEditText tin_fuelQuantityLitres;
    private TextInputEditText tin_totalFuelPrice;
    private TextInputEditText tin_currentKm;
    private TextInputEditText tin_startingKm;
    private TextInputEditText tin_distanceCovered;
    private TextView tv_calculatedAverage;
    private TextView tv_coverableDistance;
    private TextView tv_nextFuelFill;
    private ImageButton btn_calculateFuelPrice;
    private ImageButton btn_calculateDistance;
    private Button btn_calculateAverage;

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
    private int vehicleFuelCapacity = 0;
    private Double coverableDistance;
    private Double nextFuelFill;

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
        tv_calculatedAverage = view.findViewById(R.id.tv_calculatedAverage);
        tv_coverableDistance = view.findViewById(R.id.tv_coverableDistance);
        tv_nextFuelFill = view.findViewById(R.id.tv_nextFuelFill);
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
                vehicleFuelCapacity = arguments.getInt("vehicleFuelCapacity");
                addingNewFuel = true;
                Log.d(TAG, "Adding New Fuel mode = received foreignVehicleID = " + foreignVehicleID);
                tv_title_fuel.setText("Adding New Fuel Record");
            }

            // ================== Editing fuel mode ==================
            else if (arguments.get("fuel") != null) {
                fuel = (Fuel) arguments.getSerializable("fuel");
                foreignVehicleID = fuel.getForeignVehicleID();
                fuelID = fuel.getFuelID();
                addingNewFuel = false;
                Log.d(TAG, "Editing mode = received foreignVehicleID = " + foreignVehicleID + ", fuelID = " + fuelID);
                tv_title_fuel.setText("Editing Fuel Record");
            }

            fuelViewModel = new FuelViewModel(getActivity().getApplication(), foreignVehicleID);
            vehicleViewModel = new VehicleViewModel(getActivity().getApplication());
        }

        // if editing fuel, need to get current fuel data and populate in our view
        if (!addingNewFuel) {
            Log.d(TAG, "onCreateView: editing mode");

            //populate data received from Arguments
            populateFuelData(fuel); // populate received data into edit text
            fabSaveFuelData.hide(); // hide save button
            monitorTextChange(); // monitor touch on edit text and when touched, enable save button

/*          // very strange problem with RXJava - when populated data here, app crashed and said object is already disposed in FuelDetailsFragment. Maybe it's because I'm using same method I used in FuelDetailsFragment
            Disposable disposable = fuelViewModel.getFuelByID(foreignVehicleID, fuelID)
                    .subscribeOn(Schedulers.io())
                    .subscribe(fuel -> {
                        populateFuelData(fuel);
                    });
            compositeDisposable.add(disposable);
*/

        } else if (addingNewFuel) {
            Log.d(TAG, "onCreateView: addingNewFuel mode");
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
        if (lastPerLitrePrice != null && addingNewFuel) { // and not in editing mode
            String lastPerLitrePriceString = AppUtils.removeTrailingZero(lastPerLitrePrice.toString());
            lastPerLitrePrice =  Double.parseDouble(lastPerLitrePriceString);
            tin_perLitrePrice.setText(String.valueOf(lastPerLitrePrice));
        }

        // button to calculate (multiply) fuel price with fuel quantity
        btn_calculateFuelPrice.setOnClickListener(v -> {
            if (!tin_perLitrePrice.getText().toString().trim().isEmpty() && !tin_totalFuelPrice.getText().toString().trim().isEmpty()) {
                perLitrePrice = Double.parseDouble(tin_perLitrePrice.getText().toString());
                totalFuelPrice = Double.parseDouble(tin_totalFuelPrice.getText().toString());
                fuelQuantityLitres = (totalFuelPrice / perLitrePrice);
                fuelQuantityLitres = AppUtils.roundDouble(fuelQuantityLitres, 2); // round to 2 decimal places
                tin_fuelQuantityLitres.setText(AppUtils.removeTrailingZero(String.valueOf(fuelQuantityLitres)));
            }

            // store perLitrePrice to shared preference
            if (!tin_perLitrePrice.getText().toString().trim().isEmpty()) {
                perLitrePrice = Double.parseDouble(tin_perLitrePrice.getText().toString());
                AppUtils.savePetrolPriceSharedPreference(AppUtils.perLitrePriceStr, perLitrePrice, getActivity());
            }
        });


/*
        // button to calculate (minus) current distance with previous distance
        btn_calculateDistance.setOnClickListener(v -> {
            if (!tin_currentKm.getText().toString().trim().isEmpty() && !tin_startingKm.getText().toString().trim().isEmpty()) {
                currentKm = Double.parseDouble(tin_currentKm.getText().toString());
                startingKm = Double.parseDouble(tin_startingKm.getText().toString());
                totalDistance = currentKm - startingKm;
                tin_distanceCovered.setText(AppUtils.removeTrailingZero(String.valueOf(totalDistance)));
            }
        });
*/


        // button to calculate average Total distance covered in km divided by total petrol in litres i.e; 172km/ 5 ltr = 43 km per litre average
        btn_calculateAverage.setOnClickListener(v -> {
            if (!tin_fuelQuantityLitres.getText().toString().trim().isEmpty() && !tin_distanceCovered.getText().toString().trim().isEmpty()) {
                Log.d(TAG, "onCreateView: totalDistance = " + totalDistance + ", fuelQuantityLitres = " + fuelQuantityLitres);

                // retrieving double values from input edit text again to avoid error caused in editing mode
                totalDistance = Double.parseDouble(tin_distanceCovered.getText().toString());
                fuelQuantityLitres = Double.parseDouble(tin_fuelQuantityLitres.getText().toString());

                calculatedAverage = (totalDistance / fuelQuantityLitres);
                calculatedAverage = AppUtils.roundDouble(calculatedAverage, 2); // round to 2 decimal places
                tv_calculatedAverage.setText(AppUtils.removeTrailingZero(String.valueOf(calculatedAverage)) + " km/l");

                coverableDistance = (vehicleFuelCapacity * calculatedAverage); // coverableDistance = fuel capacity(litre) x fuel average (km/litre) i.e; 10 litre capacity x 12 km/litre = 120 km
                tv_coverableDistance.setText(coverableDistance + " km");

                nextFuelFill = (coverableDistance + currentKm);  // nextFuelFill = coverableDistance + currentKm
                tv_nextFuelFill.setText(nextFuelFill + " km");
                // store calculatedAverage in SharedPreferences as last vehicle mileage

            }
        });


        fabSaveFuelData.setOnClickListener(saveButtonClicked);

        tin_perLitrePrice.addTextChangedListener(twPerLitrePrice);
        tin_fuelQuantityLitres.addTextChangedListener(twFuelQuantityLitres);
        //tin_totalFuelPrice.addTextChangedListener(twTotalFuelPrice);

        tin_currentKm.addTextChangedListener(twCurrentKm);
        tin_startingKm.addTextChangedListener(twStartingKm);
        // tin_distanceCovered;; // effect on this

        // tin_totalFuelPrice + tin_distanceCovered  effect on this tin_calculatedAverage

        return view;
    }



    public TextWatcher twPerLitrePrice = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable perLitrePriceString) {

            if (!tin_perLitrePrice.getText().toString().trim().isEmpty()  && !tin_fuelQuantityLitres.getText().toString().trim().isEmpty()) {
                perLitrePrice = Double.parseDouble(perLitrePriceString.toString()); // parse to double
                fuelQuantityLitres = Double.parseDouble(tin_fuelQuantityLitres.getText().toString());  // parse to double
                totalFuelPrice = fuelQuantityLitres * perLitrePrice; // multiply both
                totalFuelPrice = AppUtils.roundDouble(totalFuelPrice, 2);
                tin_totalFuelPrice.setText(AppUtils.removeTrailingZero(String.valueOf(totalFuelPrice)));
            } else {
                tin_totalFuelPrice.setText("");
            }


        }
    };

    public TextWatcher twFuelQuantityLitres = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable fuelQuantityLitresString) {

            if (!tin_perLitrePrice.getText().toString().trim().isEmpty()  && !tin_fuelQuantityLitres.getText().toString().trim().isEmpty() ) {
                perLitrePrice = Double.parseDouble(tin_perLitrePrice.getText().toString()); // parse to double
                fuelQuantityLitres = Double.parseDouble(fuelQuantityLitresString.toString());  // parse to double
                totalFuelPrice = fuelQuantityLitres * perLitrePrice; // multiply both
                totalFuelPrice = AppUtils.roundDouble(totalFuelPrice, 2);
                tin_totalFuelPrice.setText(AppUtils.removeTrailingZero(String.valueOf(totalFuelPrice)));
            } else {
                tin_totalFuelPrice.setText("");
            }
        }
    };





    public TextWatcher twCurrentKm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable currentKmString) {

            if (!tin_currentKm.getText().toString().trim().isEmpty() && !tin_startingKm.getText().toString().trim().isEmpty()) {
                currentKm = Double.parseDouble(currentKmString.toString());
                startingKm = Double.parseDouble(tin_startingKm.getText().toString());
                totalDistance = currentKm - startingKm;
                totalDistance = AppUtils.roundDouble(totalDistance, 2);
                tin_distanceCovered.setText(AppUtils.removeTrailingZero(String.valueOf(totalDistance)));
            } else {
                tin_distanceCovered.setText("");
            }


        }
    };

    public TextWatcher twStartingKm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable startingKmString) {

            if (!tin_currentKm.getText().toString().trim().isEmpty() && !tin_startingKm.getText().toString().trim().isEmpty()) {
                currentKm = Double.parseDouble(tin_currentKm.getText().toString());
                startingKm = Double.parseDouble(startingKmString.toString());
                totalDistance = currentKm - startingKm;
                totalDistance = AppUtils.roundDouble(totalDistance, 2);
                tin_distanceCovered.setText(AppUtils.removeTrailingZero(String.valueOf(totalDistance)));
            } else {
                tin_distanceCovered.setText("");
            }

        }
    };


    // display fuel data inside TextInputEditText
    private void populateFuelData(Fuel fuel) {

        // populate data and also remove trailing zero if any
        fuelDate = fuel.getFuelDate();
        tv_input_fuel_date.setText(AppUtils.getFormattedDateString(fuelDate));
        tin_perLitrePrice.setText(AppUtils.removeTrailingZero(fuel.getPerLitrePrice().toString()));
        tin_fuelQuantityLitres.setText(AppUtils.removeTrailingZero(fuel.getFuelQuantityLitres().toString()));
        tin_totalFuelPrice.setText(AppUtils.removeTrailingZero(fuel.getTotalFuelPrice().toString()));
        tin_currentKm.setText(AppUtils.removeTrailingZero(fuel.getCurrentKm().toString()));
        tin_startingKm.setText(AppUtils.removeTrailingZero(fuel.getStartingKm().toString()));
        tin_distanceCovered.setText(AppUtils.removeTrailingZero(fuel.getDistanceCovered().toString()));
        tv_calculatedAverage.setText(AppUtils.removeTrailingZero(fuel.getCalculatedAverage().toString()) + " km/l");

    }


    // When Save button is clicked
    private final View.OnClickListener saveButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //fuel = new Fuel();
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

                // means no default date is set
                if (fuelDate == null) {
                    if (!addingNewFuel){ // if we are in editing mode, we don't want current date to be set instead
                        fuelDate = AppUtils.getCurrentDateTime();
                        tv_input_fuel_date.setText(AppUtils.getFormattedDateString(fuelDate));
                    }
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
                tv_calculatedAverage.getText().toString().trim().isEmpty();

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

        for (TextInputEditText item : textInputEditTextList) {
            item.setEnabled(true);
        }
    }

    // check for touch event on editText to enable save button
    private void monitorTextChange() {
        List<TextInputEditText> textInputEditTextList = new ArrayList<>();
        textInputEditTextList.add(tin_perLitrePrice); // first line is selected automatically
        textInputEditTextList.add(tin_fuelQuantityLitres);
        textInputEditTextList.add(tin_totalFuelPrice);
        textInputEditTextList.add(tin_currentKm);
        textInputEditTextList.add(tin_startingKm);
        textInputEditTextList.add(tin_distanceCovered);

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

        // tv_input_fuel_date is a TextView so we need to add this seperately
        tv_input_fuel_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fabSaveFuelData.show();
            }
        });
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        fuelDate = calendar.getTime();
        Log.d(TAG, "onDateSet: fuelDate = " + fuelDate);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android Oreo 8 and above
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
        //compositeDisposable.dispose(); // dispose disposable
    }
}