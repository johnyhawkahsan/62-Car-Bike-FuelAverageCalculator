package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleListFragment extends Fragment {

    // callback method implemented by MainActivity
    public interface VehicleListFragmentListener{
        // called when add button is pressed
        void onAddVehicle();
    }

    // used to inform the MainActivity when a item is selected or added
    private VehicleListFragmentListener listener;

    private static final String TAG = VehicleListFragment.class.getSimpleName();
    private TextView emptyTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.vehicle_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewVehicle);
        //recyclerView.setLayoutManager(new GridLayoutManager(this));
        // carBikeListAdapter = new CarBikeListAdapter // constructor for adapter
        //recyclerView.setAdapter(carBikeListAdapter);

        emptyTextView = view.findViewById(R.id.tv__empty);

/*
        // get all vehicles list using view model
        if (carVehicleList.size() > 0) {
            emptyTextView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
        }
*/


        floatingActionButton = view.findViewById(R.id.fabAddVehicle);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AddEditVehicleFragment via MainActivity
                listener.onAddVehicle();
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
