package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.EngineOil;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.EngineOilViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.FuelViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EngineOilDetailsFragment extends DialogFragment {

    private static final String TAG = EngineOilDetailsFragment.class.getSimpleName();

    // constructor
    public static EngineOilDetailsFragment newInstance(int foreignVehicleID, int engineOilID) {
        EngineOilDetailsFragment engineOilDetailsFragment = new EngineOilDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("foreignVehicleID", foreignVehicleID);
        args.putInt("engineOilID", engineOilID);
        engineOilDetailsFragment.setArguments(args);
        return engineOilDetailsFragment;
    }


    private int foreignVehicleID = 0;
    private int engineOilID = 0;
    private EngineOil engineOil;

    private EngineOilViewModel engineOilViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView tv_eoil_date;
    private TextView tv_eoil_description;
    private TextView tv_eoilQuantityLitres;
    private TextView tv_totalEoilPrice;
    private TextView tv_interval;
    private TextView tv_current_mileage;
    private TextView tv_previous_mileage;
    private TextView tv_total_distance;
    private TextView tv_next_oil_change;


    private ImageButton btn_edit_eoil;
    private ImageButton btn_delete_eoil;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.engine_oil_details, container, false);


        tv_eoil_date = view.findViewById(R.id.tv_eoil_date);
        tv_eoil_description = view.findViewById(R.id.tv_eoil_description);
        tv_eoilQuantityLitres = view.findViewById(R.id.tv_eoilQuantityLitres);
        tv_totalEoilPrice = view.findViewById(R.id.tv_totalEoilPrice);
        tv_interval = view.findViewById(R.id.tv_interval);
        tv_total_distance = view.findViewById(R.id.tv_total_distance);
        tv_next_oil_change = view.findViewById(R.id.tv_next_oil_change);
        btn_edit_eoil = view.findViewById(R.id.btn_edit_eoil);
        btn_delete_eoil = view.findViewById(R.id.btn_delete_eoil);

        btn_edit_eoil.setOnClickListener(editButtonClicked);
        btn_delete_eoil.setOnClickListener(deleteButtonClicked);


        // get Bundle of arguments
        Bundle arguments = getArguments();

        if (arguments != null) {

            if (arguments.get("engineOilID") != null && arguments.get("foreignVehicleID") != null) {
                foreignVehicleID = arguments.getInt("foreignVehicleID");
                engineOilID = arguments.getInt("engineOilID");
                Log.d(TAG, "Details mode = received foreignVehicleID = " + foreignVehicleID + ", engineOilID = " + engineOilID);
            }

            engineOilViewModel = new EngineOilViewModel(getActivity().getApplication(), foreignVehicleID);
        }

        Disposable disposable = engineOilViewModel.getEngineOilByID(foreignVehicleID, engineOilID)
                .subscribeOn(Schedulers.io())
                .subscribe(engineOil -> {
                    this.engineOil = engineOil; // set global vehicle object to returned/searched vehicle object
                    Log.d(TAG, "View Fuel Details : fuelID = " + engineOilID);
                    populateEngineOilData(engineOil);
                });

        compositeDisposable.add(disposable);





        return view;
    }



    private final View.OnClickListener editButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d(TAG, "onClick: editButtonClicked");


            // Launch AddEditFuelFragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            AddEditEngineOilFragment addEditEngineOilFragment = AddEditEngineOilFragment.newInstance(engineOil); // launch
            addEditEngineOilFragment.show(fragmentManager, "add_edit_engine_oil_fragment");

            // close current fragment
            getDialog().dismiss();

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
                    engineOilViewModel.deleteEngineOilWithID(foreignVehicleID, engineOilID);
                    getDialog().dismiss();
                    getParentFragmentManager().popBackStack(); // remove previous fragment as well

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



    // display data inside TextViews
    private void populateEngineOilData(EngineOil engineOil) {

        tv_eoil_date.setText(AppUtils.getFormattedDateString(engineOil.getEoil_Date()));
        tv_eoil_description.setText(engineOil.getEoil_description());
        tv_eoilQuantityLitres.setText(AppUtils.removeTrailingZero(engineOil.getEoil_quantityLitres().toString()) + " l");
        tv_totalEoilPrice.setText("Rs:" + AppUtils.removeTrailingZero(engineOil.getEoil_Price().toString()));
        tv_interval.setText(AppUtils.removeTrailingZero(engineOil.getEoil_interval().toString()) + " km" );
        //tv_current_mileage.setText(AppUtils.removeTrailingZero(engineOil.getEoil_currentMileage().toString()) + " km" );
        //tv_previous_mileage.setText(AppUtils.removeTrailingZero(engineOil.getEoil_previousMileage().toString()) + " km" );
        tv_total_distance.setText(AppUtils.removeTrailingZero(engineOil.getEoil_totalDistance().toString()) + " km" );
        tv_next_oil_change.setText(AppUtils.removeTrailingZero(engineOil.getNextOilChangeAt().toString()) + " km" );
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
