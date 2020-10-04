package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.adapter.VehicleListAdapter;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.VehicleViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleListFragment extends Fragment {


    // callback method implemented by MainActivity
    public interface VehicleListFragmentListener{
        // called when add button is pressed
        void onAddNewVehicle();
        void onVehicleSelected(int vehicleID);
    }

    // used to inform the MainActivity when a item is selected or added
    private VehicleListFragmentListener listener;

    private static final String TAG = VehicleListFragment.class.getSimpleName();

    private VehicleViewModel vehicleViewModel;
    private RecyclerView recyclerView;
    private VehicleListAdapter vehicleListAdapter;
    private TextView emptyTextView;
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.vehicle_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewVehicle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        vehicleListAdapter = new VehicleListAdapter(getActivity(), vehicle -> {
            Log.d(TAG, "onClick: vehicleID = " + vehicle.getVehicleID());
            // open petrol data of this vehicle from MainActivity
            listener.onVehicleSelected(vehicle.getVehicleID());
        });

        recyclerView.setAdapter(vehicleListAdapter);

        Application app = (getActivity().getApplication());

        //Both methods work- I don't know the difference between this and below ViewModelProvider method
        //vehicleViewModel = new VehicleViewModel(getActivity().getApplication());
        vehicleViewModel = new ViewModelProvider.AndroidViewModelFactory(app).create(VehicleViewModel.class);

        emptyTextView = view.findViewById(R.id.tv__empty);

        vehicleViewModel.getAllVehicles().observe(getActivity(), vehicles -> { // using lambda expression here

            vehicleListAdapter.setVehicleList(vehicles); // manually set list

            if (vehicles.size() > 0 ) {
                emptyTextView.setVisibility(View.GONE);
            } else {
                emptyTextView.setVisibility(View.VISIBLE);
            }


            Log.d(TAG, "onCreateView: size of list = " + vehicles.size());
            // Loop through all returned list items and display in logs
            for (int i = 0; i < vehicles.size(); i++ ){
                Log.d(TAG, "vehicleID = " + vehicles.get(i).getVehicleID() + ", vehicle type = " + vehicles.get(i).getVehicleType());
            }

        });


        floatingActionButton = view.findViewById(R.id.fabAddVehicle);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AddEditVehicleFragment via MainActivity
                listener.onAddNewVehicle();
            }
        });


        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (VehicleListFragmentListener) context; // need to initialize Interface method in onAttach()
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null; // need to decouple Interface method in onAttach()
    }
}
