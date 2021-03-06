package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.FuelViewHolder>  {

    public interface FuelClickListener {
        void onClick(Fuel fuel);
    }


    private static final String TAG = FuelListAdapter.class.getSimpleName();
    private Context mContext;
    private final LayoutInflater mInflator;
    private List<Fuel> mFuels;
    private FuelListAdapter.FuelClickListener mFuelClickListener; // object for interface

    // Constructor for Adapter
    public FuelListAdapter(Context mContext, FuelListAdapter.FuelClickListener fuelClickListener) {
        this.mContext = mContext;
        mInflator = LayoutInflater.from(mContext);
        mFuelClickListener = fuelClickListener;
    }

    // Instead of using List in a constructor, we use it here so we can use in MainActivity's observer.
    public void setFuelList(List<Fuel> fuelList){
        this.mFuels = fuelList;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    public class FuelViewHolder extends RecyclerView.ViewHolder {

        private TextView fuel_date, totalFuelPrice, distanceCovered, calculatedAverage ;

        public FuelViewHolder(@NonNull View itemView) {
            super(itemView);
            fuel_date = itemView.findViewById(R.id.fuel_date);
            totalFuelPrice = itemView.findViewById(R.id.totalFuelPrice);
            distanceCovered = itemView.findViewById(R.id.distanceCovered);
            calculatedAverage = itemView.findViewById(R.id.calculatedAverage);


            itemView.setOnClickListener(v -> {
                Fuel fuel = mFuels.get(getAdapterPosition()); // get item at specific position
                mFuelClickListener.onClick(fuel);  // This interface method sends ID to Adapter constructor method
                Log.d(TAG, "onClick: clicked on item with id = " + fuel.getFuelID());
            });

        }
    }




    @NonNull
    @Override
    public FuelListAdapter.FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.recycler_item_fuel, parent, false);
        return new FuelListAdapter.FuelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelListAdapter.FuelViewHolder holder, int position) {

        Fuel currentFuel = mFuels.get(position);

        holder.fuel_date.setText(AppUtils.getFormattedDateString(currentFuel.getFuelDate()));
        holder.totalFuelPrice.setText("Rs:" + AppUtils.removeTrailingZero(String.valueOf(currentFuel.getTotalFuelPrice())));
        holder.distanceCovered.setText(AppUtils.removeTrailingZero(String.valueOf(currentFuel.getDistanceCovered())) + " km");
        holder.calculatedAverage.setText(AppUtils.removeTrailingZero(String.valueOf(currentFuel.getCalculatedAverage())) + " km/l");


    }

    @Override
    public int getItemCount() {
        if (mFuels == null)
            return 0;
        else
            return mFuels.size();
    }


}
