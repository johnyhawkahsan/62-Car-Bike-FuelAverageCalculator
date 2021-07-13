package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.EngineOil;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.EngineOilViewModel;
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

public class AddEditEngineOilFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = AddEditEngineOilFragment.class.getSimpleName();

    // constructor for edit
    public static AddEditEngineOilFragment newInstance(EngineOil engineOil) {
        AddEditEngineOilFragment addEditEngineOilFragment = new AddEditEngineOilFragment();
        Bundle args = new Bundle();
        args.putSerializable("engineOil", engineOil);
        addEditEngineOilFragment.setArguments(args);
        return addEditEngineOilFragment;
    }

    // constructor for add new
    public static AddEditEngineOilFragment newInstance(int foreignVehicleID) {
        AddEditEngineOilFragment addEditEngineOilFragment = new AddEditEngineOilFragment();
        Bundle args = new Bundle();
        args.putInt("foreignVehicleID", foreignVehicleID);
        addEditEngineOilFragment.setArguments(args);
        return addEditEngineOilFragment;
    }


    private boolean addingNewEngineOil = true; // adding (true) or editing (false)
    private int foreignVehicleID = 0;
    private int engineOilID = 0;
    private EngineOil engineOil;


    private EngineOilViewModel engineOilViewModel;

    private TextView tv_title_engineOil;
    private Button btn_setEngineOilDate;
    private TextView tv_input_engineOil_date;
    private TextInputEditText tin_eoil_description;
    private TextInputEditText tin_eoil_quantityLitres;
    private TextInputEditText tin_eoil_totalPrice;
    private TextInputEditText tin_eoil_interval;
    private TextInputEditText tin_eoil_currentMileage;
    private TextInputEditText tin_eoil_previousMileage;
    private TextInputEditText tin_eoil_totalDistance;
    private TextView tv_eoil_nextOilChangeAt;
    private ImageButton btn_eoil_calculateDistance;
    private Button btn_calculateNextOilChange;

    private FloatingActionButton fabSaveEngineOilData;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Date engineOilDate;
    private String engineOilDateString = "";

    private Double lastEngineOilInterval;
    private Double currentMileage;
    private Double previousMileage;
    private Double totalDistance;
    private Double nextUpcomingMileage;

    private List<TextInputEditText> textInputValidationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_edit_engine_oil, container, false);

        tv_title_engineOil = view.findViewById(R.id.tv_title_engineOil);
        btn_setEngineOilDate = view.findViewById(R.id.btn_setEngineOilDate);
        tv_input_engineOil_date = view.findViewById(R.id.tv_input_engineOil_date);
        tin_eoil_description = view.findViewById(R.id.tin_eoil_description);
        tin_eoil_quantityLitres = view.findViewById(R.id.tin_eoil_quantityLitres);
        tin_eoil_totalPrice = view.findViewById(R.id.tin_eoil_totalPrice);
        tin_eoil_interval = view.findViewById(R.id.tin_eoil_interval);
        tin_eoil_currentMileage = view.findViewById(R.id.tin_eoil_currentMileage);
        tin_eoil_previousMileage = view.findViewById(R.id.tin_eoil_previousMileage);
        tin_eoil_totalDistance = view.findViewById(R.id.tin_eoil_totalDistance);
        tv_eoil_nextOilChangeAt = view.findViewById(R.id.tv_eoil_nextOilChangeAt);
        btn_eoil_calculateDistance = view.findViewById(R.id.btn_eoil_calculateDistance);
        btn_calculateNextOilChange = view.findViewById(R.id.btn_calculateNextOilChange);
        fabSaveEngineOilData = view.findViewById(R.id.fabSaveEngineOilData);
        fabSaveEngineOilData.hide(); // hide save button

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        textInputValidationList = new ArrayList<>();

        // get Bundle of arguments then extract the contact's Uri
        Bundle arguments = getArguments();

        // if we are adding new Vehicle, there should be no data in getArguments to assign to medID
        if (arguments != null) {

            // ================== Adding new engineOil mode ==================
            if (arguments.get("foreignVehicleID") != null && arguments.get("engineOilID") == null) {
                foreignVehicleID = arguments.getInt("foreignVehicleID");
                addingNewEngineOil = true;
                Log.d(TAG, "Adding New EngineOil mode = received foreignVehicleID = " + foreignVehicleID);
                tv_title_engineOil.setText("Add EngineOil Record");
            }

            // ================== Editing engineOil mode ==================
            else if (arguments.get("engineOil") != null) {
                engineOil = (EngineOil) arguments.getSerializable("engineOil");
                foreignVehicleID = engineOil.getForeignVehicleID();
                engineOilID = engineOil.getEngineOilID();
                addingNewEngineOil = false;
                Log.d(TAG, "Editing mode = received foreignVehicleID = " + foreignVehicleID + ", engineOilID = " + engineOilID);
                tv_title_engineOil.setText("Editing EngineOil Record");
            }

            engineOilViewModel = new EngineOilViewModel(getActivity().getApplication(), foreignVehicleID);
        }

        // if editing engineOil, need to get current engineOil data and populate in our view
        if (!addingNewEngineOil) {
            Log.d(TAG, "onCreateView: editing mode");

            //populate data received from Arguments
            populateEngineOilData(engineOil); // populate received data into edit text
            monitorTextChange(); // monitor touch on edit text and when touched, enable save button


        } else if (addingNewEngineOil) {
            Log.d(TAG, "onCreateView: addingNew mode");
            engineOilDate = AppUtils.getCurrentDateTime();
            engineOilDateString = AppUtils.getFormattedDateString(engineOilDate);
            tv_input_engineOil_date.setText(engineOilDateString);
        }

        // set date
        btn_setEngineOilDate.setOnClickListener(v -> {
            datePickerDialog.show();
        });


        // retrieve lastEngineOilInterval from shared preferences and display in tin_eoil_interval
        lastEngineOilInterval = AppUtils.getLastEngineOilInterval(AppUtils.lastEngineOilInterval, getActivity());
        if (lastEngineOilInterval != null && addingNewEngineOil) { // and not in editing mode
            String lastEngineOilIntervalString = AppUtils.removeTrailingZero(lastEngineOilInterval.toString());
            lastEngineOilInterval = Double.parseDouble(lastEngineOilIntervalString);
            tin_eoil_interval.setText(String.valueOf(lastEngineOilInterval));
        }


        // button to calculate (minus) current distance with previous distance
        btn_eoil_calculateDistance.setOnClickListener(v -> {
            if (!tin_eoil_previousMileage.getText().toString().trim().isEmpty() && !tin_eoil_currentMileage.getText().toString().trim().isEmpty()) {
                currentMileage = Double.parseDouble(tin_eoil_currentMileage.getText().toString());
                previousMileage = Double.parseDouble(tin_eoil_previousMileage.getText().toString());
                totalDistance = currentMileage - previousMileage;
                tin_eoil_totalDistance.setText(String.valueOf(totalDistance));
            }
        });

        // button to calculate next oil change mileage i.e; 10,3000km + 3000km interval = 10,6000km
        btn_calculateNextOilChange.setOnClickListener(v -> {
            fabSaveEngineOilData.show(); // show when calculate button is pressed
            hideKeyboard();
            if (!tin_eoil_currentMileage.getText().toString().trim().isEmpty() && !tin_eoil_interval.getText().toString().trim().isEmpty()) {
                Double intervalEntered = Double.parseDouble(tin_eoil_interval.getText().toString().trim());
                nextUpcomingMileage = (currentMileage + intervalEntered);
                nextUpcomingMileage = AppUtils.roundDouble(nextUpcomingMileage, 2); // round to 2 decimal places
                tv_eoil_nextOilChangeAt.setText(String.valueOf(nextUpcomingMileage));
                // store nextUpcomingMileage in SharedPreferences

            }
        });


        fabSaveEngineOilData.setOnClickListener(saveButtonClicked);

        tin_eoil_currentMileage.addTextChangedListener(twCurrentKm);
        tin_eoil_previousMileage.addTextChangedListener(twPreviousKm);

        return view;
    }


    public TextWatcher twCurrentKm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable currentKmString) {

            if (!tin_eoil_currentMileage.getText().toString().trim().isEmpty() && !tin_eoil_previousMileage.getText().toString().trim().isEmpty()) {
                currentMileage = Double.parseDouble(currentKmString.toString());
                previousMileage = Double.parseDouble(tin_eoil_previousMileage.getText().toString());
                totalDistance = currentMileage - previousMileage;
                totalDistance = AppUtils.roundDouble(totalDistance, 2);
                tin_eoil_totalDistance.setText(AppUtils.removeTrailingZero(String.valueOf(totalDistance)));
            } else {
                tin_eoil_totalDistance.setText("");
            }
        }
    };


    public TextWatcher twPreviousKm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable previousKmString) {

            if (!tin_eoil_currentMileage.getText().toString().trim().isEmpty() && !tin_eoil_previousMileage.getText().toString().trim().isEmpty()) {
                previousMileage = Double.parseDouble(previousKmString.toString());
                currentMileage  = Double.parseDouble(tin_eoil_currentMileage.getText().toString());
                totalDistance = currentMileage - previousMileage;
                totalDistance = AppUtils.roundDouble(totalDistance, 2);
                tin_eoil_totalDistance.setText(AppUtils.removeTrailingZero(String.valueOf(totalDistance)));
            } else {
                tin_eoil_totalDistance.setText("");
            }
        }
    };



    // check for touch event on editText to enable save button
    private void monitorTextChange() {

        List<TextInputEditText> textInputEditTextList = new ArrayList<>();
        textInputEditTextList.add(tin_eoil_description); // first line is selected automatically
        textInputEditTextList.add(tin_eoil_quantityLitres);
        textInputEditTextList.add(tin_eoil_totalPrice);
        textInputEditTextList.add(tin_eoil_interval);
        textInputEditTextList.add(tin_eoil_currentMileage);
        textInputEditTextList.add(tin_eoil_previousMileage);
        textInputEditTextList.add(tin_eoil_totalDistance);

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
                    fabSaveEngineOilData.show();
                }
            });
        }

        // tv_input_fuel_date is a TextView so we need to add this seperately
        tv_input_engineOil_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fabSaveEngineOilData.show();
            }
        });
    }


    // display fuel data inside TextInputEditText
    private void populateEngineOilData(EngineOil engineOil) {

        // populate data and also remove trailing zero if any
        engineOilDate = engineOil.getEoil_Date();
        tv_input_engineOil_date.setText(AppUtils.getFormattedDateString(engineOilDate));
        tin_eoil_description.setText(engineOil.getEoil_description());
        tin_eoil_quantityLitres.setText(AppUtils.removeTrailingZero(engineOil.getEoil_quantityLitres().toString()));
        tin_eoil_totalPrice.setText(AppUtils.removeTrailingZero(engineOil.getEoil_Price().toString()));
        tin_eoil_interval.setText(AppUtils.removeTrailingZero(engineOil.getEoil_interval().toString()));
        tin_eoil_currentMileage.setText(AppUtils.removeTrailingZero(engineOil.getEoil_currentMileage().toString()));
        tin_eoil_previousMileage.setText(AppUtils.removeTrailingZero(engineOil.getEoil_previousMileage().toString()));
        tin_eoil_totalDistance.setText(AppUtils.removeTrailingZero(engineOil.getEoil_totalDistance().toString()));
        tv_eoil_nextOilChangeAt.setText(AppUtils.removeTrailingZero(engineOil.getNextOilChangeAt().toString()));
        tin_eoil_totalDistance.setText(AppUtils.removeTrailingZero(engineOil.getEoil_totalDistance().toString()));
    }


    // When Save button is clicked
    private final View.OnClickListener saveButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // store eoil_interval to shared preferences
            if (!tin_eoil_interval.getText().toString().trim().isEmpty()) {
                lastEngineOilInterval = Double.parseDouble(tin_eoil_interval.getText().toString());
                AppUtils.saveLastEngineOilIntervalSharedPreference(AppUtils.lastEngineOilInterval, lastEngineOilInterval, getActivity());
            }

            EngineOil engineOil = new EngineOil();
            engineOil.setForeignVehicleID(foreignVehicleID); // foreign key is available in all cases
            if (!addingNewEngineOil) {
                engineOil.setEngineOilID(engineOilID); // engineOil id is only set if we are editing current engineOil data with already available engineOil id
            }

            // if all fields are not filled
            if (checkForEmptyList()) {

                Log.d(TAG, "onClick: any of the parameters is empty");
                AppUtils.showMessage(getActivity(), "Please fill all the details!");

            } else { //If no field is left empty and everything is filled

                String eoil_description = tin_eoil_description.getText().toString();
                Double eoil_quantityLitres = Double.valueOf(tin_eoil_quantityLitres.getText().toString());
                Double eoil_Price = Double.valueOf(tin_eoil_totalPrice.getText().toString());
                Double eoil_interval = Double.valueOf(tin_eoil_interval.getText().toString());
                Double eoil_currentMileage = Double.valueOf(tin_eoil_currentMileage.getText().toString());
                Double eoil_previousMileage = Double.valueOf(tin_eoil_previousMileage.getText().toString());
                Double eoil_totalDistance = Double.valueOf(tin_eoil_totalDistance.getText().toString());
                Double eoil_nextOilChangeAt = Double.valueOf(tv_eoil_nextOilChangeAt.getText().toString());

                // set data fields for engineOil
                if (engineOilDate == null) { // means no default date is set
                    if (!addingNewEngineOil){ // set date to current date in adding new mode only, in editing mode, we want previous date to be displayed
                        engineOilDate = AppUtils.getCurrentDateTime(); // set current date automatically
                        tv_input_engineOil_date.setText(AppUtils.getFormattedDateString(engineOilDate));
                    }
                }
                engineOil.setEoil_Date(engineOilDate);
                engineOil.setEoil_description(eoil_description);
                engineOil.setEoil_quantityLitres(eoil_quantityLitres);
                engineOil.setEoil_Price(eoil_Price);
                engineOil.setEoil_interval(eoil_interval);
                engineOil.setEoil_currentMileage(eoil_currentMileage);
                engineOil.setEoil_previousMileage(eoil_previousMileage);
                engineOil.setEoil_totalDistance(eoil_totalDistance);
                engineOil.setNextOilChangeAt(eoil_nextOilChangeAt);


                startStoringData(engineOil);
                Log.d(TAG, "onClick: start storing data");

                getDialog().dismiss();
                getParentFragmentManager().popBackStack(); // close detail fragment when editing is complete
            }


        }
    };

    // method to store engineOil data to DB
    private void startStoringData(EngineOil engineOil) {
        // if we are adding new  record
        if (addingNewEngineOil) {
            engineOilViewModel.insertEngineOil(engineOil);
            Log.d(TAG, "addingNewEngineOil: ");
        } else { // if we are editing existing vehicle record
            Log.d(TAG, "updateVehicle ");
            engineOilViewModel.updateEngineOil(engineOil);
        }
    }


    // method to check for all edit text
    private Boolean checkForEmptyList() {
        Boolean emptyListAvailable = tin_eoil_description.getText().toString().trim().isEmpty() ||
                tin_eoil_quantityLitres.getText().toString().trim().isEmpty() ||
                tin_eoil_totalPrice.getText().toString().trim().isEmpty() ||
                tin_eoil_interval.getText().toString().trim().isEmpty() ||
                tin_eoil_currentMileage.getText().toString().trim().isEmpty() ||
                tin_eoil_previousMileage.getText().toString().trim().isEmpty() ||
                tin_eoil_totalDistance.getText().toString().trim().isEmpty() ||
                tv_eoil_nextOilChangeAt.getText().toString().trim().isEmpty();

        if (emptyListAvailable) {
            validationList(); // set error messages
        }
        Log.d(TAG, "checkForEmptyList: emptyListAvailable = " + emptyListAvailable);
        return emptyListAvailable;
    }


    // iterate through all items in the list and look for empty items
    private void validationList() {

        textInputValidationList.add(tin_eoil_description);
        textInputValidationList.add(tin_eoil_quantityLitres);
        textInputValidationList.add(tin_eoil_totalPrice);
        textInputValidationList.add(tin_eoil_interval);
        textInputValidationList.add(tin_eoil_currentMileage);
        textInputValidationList.add(tin_eoil_previousMileage);
        textInputValidationList.add(tin_eoil_totalDistance);

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

    // hide keyboard once "calculate" button is pressed
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        //compositeDisposable.dispose(); // dispose disposable
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        engineOilDate = calendar.getTime();

        engineOilDateString = AppUtils.getFormattedDateString(engineOilDate);
        Log.d(TAG, "onDateSet: engineOilDateString = " + engineOilDateString + " , from original engineOilDate = " + engineOilDate);
        tv_input_engineOil_date.setText(engineOilDateString);

    }

}





