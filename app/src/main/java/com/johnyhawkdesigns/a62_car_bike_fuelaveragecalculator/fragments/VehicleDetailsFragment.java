package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.VehicleViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VehicleDetailsFragment extends Fragment {

    // callback method implemented by MainActivity
    public interface VehicleDetailsFragmentListener{
        void onVehicleDeleted();
        void onEditVehicle(int vehicleID);
    }
    private VehicleDetailsFragmentListener listener;

    private static final String TAG = VehicleDetailsFragment.class.getSimpleName();
    public int vehicleID;

    private ImageView detail_fragment_vehicle_icon;
    private TextView tv_vehicleID;
    private TextView tv_vehicle_make;
    private TextView tv_vehicle_model;
    private Button btn_mobilOil;
    private Button btn_fuel;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    VehicleViewModel vehicleViewModel;
    private Vehicle vehicle;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.vehicle_detail_fragment, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            vehicleID = arguments.getInt("vehicleID"); // receive vehicleID
            Log.d(TAG, "onCreateView: received vehicleID = " + vehicleID);
        }

        detail_fragment_vehicle_icon = view.findViewById(R.id.detail_fragment_vehicle_icon);
        tv_vehicleID = view.findViewById(R.id.tv_vehicleID);
        tv_vehicle_make = view.findViewById(R.id.tv_vehicle_make);
        tv_vehicle_model = view.findViewById(R.id.tv_vehicle_model);
        btn_fuel = view.findViewById(R.id.btn_fuel);
        btn_mobilOil = view.findViewById(R.id.btn_mobilOil);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fab = view.findViewById(R.id.fabAdd);
        fab.hide();


        vehicleViewModel = new VehicleViewModel(getActivity().getApplication());

        Disposable disposable = vehicleViewModel.getVehicle(vehicleID)
                .subscribeOn(Schedulers.io())
                //.map(vehicle1 -> {
                //   return vehicle1 = vehicle;
                //})
                .subscribe(vehicle -> {
                    this.vehicle = vehicle;

                    Log.d(TAG, "onCreateView: vehicle.getVehicleMake() = " + vehicle.getVehicleMake() + ", vehicle.getVehicleModel = " + vehicle.getVehicleModel());

                    if (vehicle.getVehicleType().equals("car")){
                        detail_fragment_vehicle_icon.setImageResource(R.drawable.ic_car);
                    } else {
                        detail_fragment_vehicle_icon.setImageResource(R.drawable.ic_bike);
                    }
                    tv_vehicleID.setText("VehicleID = " + String.valueOf(vehicle.getVehicleID()));
                    tv_vehicle_make.setText(vehicle.getVehicleMake());
                    tv_vehicle_model.setText(vehicle.getVehicleModel());
                });

        compositeDisposable.add(disposable);

        // This is simple LiveData<Vehicle> method which I used, but I stopped using it when "delete" method messed with the app and crashed the app once vehicle is delete
        // I believe this has to do with the lifecycle of LiveData
        //vehicleViewModel.getVehicle(vehicleID).observe(getActivity(), foundVehicle -> {
        //});





        btn_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.show();
                // need to populate recyclerView with fuel data
                toggleButtonBackground(btn_fuel, btn_mobilOil);

            }
        });


        btn_mobilOil.setOnClickListener(v -> {
            fab.show();
            // need to populate recyclerView with mobile oil data
            toggleButtonBackground(btn_mobilOil, btn_fuel);
        });


        return view;
    }

    private void toggleButtonBackground(Button activeButton, Button nonActiveButton) {
        activeButton.setBackgroundResource(R.drawable.selected_state);
        nonActiveButton.setBackgroundResource(R.drawable.regular_state);
    }


    // Fragment lifecycle method- set MedHistoryDetailFragmentListener when fragment attached
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (VehicleDetailsFragmentListener) context;
    }

    // Fragment lifecycle method- remove MedHistoryDetailFragmentListener when Fragment detached
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        compositeDisposable.dispose(); // dispose disposable
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_vehicle, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:

                Log.d(TAG, "onOptionsItemSelected: edit vehicle profile");

                listener.onEditVehicle(vehicleID);

                return true;

            case R.id.action_delete:

                Log.d(TAG, "onOptionsItemSelected: delete vehicle = " + vehicle.getVehicleMake());

                // Build alert dialog for confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppUtils.showMessage(getActivity(), "Delete vehicleID = " + vehicleID + " success");

                        vehicleViewModel.deleteVehicle(vehicle);
                        listener.onVehicleDeleted();
                        getParentFragmentManager().popBackStack();
                        //getFragmentManager().popBackStack(); // deprecated
                        //getActivity().onBackPressed(); // this also works
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
