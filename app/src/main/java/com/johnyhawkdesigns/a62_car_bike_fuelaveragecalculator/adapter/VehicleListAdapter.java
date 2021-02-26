package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder> {

    public interface VehicleClickListener {
        void onClick(Vehicle vehicle);
    }

    private static final String TAG = VehicleListAdapter.class.getSimpleName();
    private Context mContext;
    private final LayoutInflater mInflator;
    private List<Vehicle> mVehicles;
    private VehicleClickListener mVehicleClickListener; // object for interface

    // Constructor for Adapter
    public VehicleListAdapter(Context mContext, VehicleClickListener vehicleClickListener) {
        this.mContext = mContext;
        mInflator = LayoutInflater.from(mContext);
        mVehicleClickListener = vehicleClickListener;
    }

    // Instead of using List in a constructor, we use it here so we can use in MainActivity's observer.
    public void setVehicleList(List<Vehicle> vehicles){
        this.mVehicles = vehicles;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_make, tv_model, tv_fuelCapacity;
        private ImageView iv_iconVehicle;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_iconVehicle = itemView.findViewById(R.id.iv_iconVehicle);
            tv_make = itemView.findViewById(R.id.tv_make);
            tv_model = itemView.findViewById(R.id.tv_model);
            tv_fuelCapacity = itemView.findViewById(R.id.tv_fuelCapacity);

            itemView.setOnClickListener(v -> {
                Vehicle vehicle = mVehicles.get(getAdapterPosition()); // get vehicle at specific position
                mVehicleClickListener.onClick(vehicle);  // This interface method sends ID to MainActivity's Adapter constructor method
                Log.d(TAG, "onClick: clicked on item with id = " + vehicle.getVehicleID());
                Log.d(TAG, "onClick: clicked on item with getVehicleFuelCapacity = " + vehicle.getVehicleFuelCapacity());

            });

        }

    }




    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.recycler_item_vehicle, parent, false);
        return new VehicleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle currentVehicle = mVehicles.get(position);

        if (currentVehicle.getVehicleType().equals("car")) {
            holder.iv_iconVehicle.setImageResource(R.drawable.ic_car);
        } else if (currentVehicle.getVehicleType().equals("bike")) {
            holder.iv_iconVehicle.setImageResource(R.drawable.ic_bike);
        }

        holder.tv_make.setText(currentVehicle.getVehicleMake());
        holder.tv_model.setText(currentVehicle.getVehicleModel());
        holder.tv_fuelCapacity.setText(String.valueOf(currentVehicle.getVehicleFuelCapacity()));
    }

    @Override
    public int getItemCount() {
        if (mVehicles == null)
            return 0;
        else
            return mVehicles.size();
    }


}
