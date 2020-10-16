package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.EngineOil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EngineOilListAdapter extends RecyclerView.Adapter<EngineOilListAdapter.EngineOilViewHolder> {


    public interface EngineOilClickListener {
        void onClick(EngineOil engineOil);
    }


    private static final String TAG = EngineOilListAdapter.class.getSimpleName();
    private Context mContext;
    private final LayoutInflater mInflator;
    private List<EngineOil> mEngineOils;
    private EngineOilListAdapter.EngineOilClickListener mEngineOilClickListener; // object for interface

    // Constructor for Adapter
    public EngineOilListAdapter(Context mContext, EngineOilListAdapter.EngineOilClickListener engineOilClickListener) {
        this.mContext = mContext;
        mInflator = LayoutInflater.from(mContext);
        mEngineOilClickListener = engineOilClickListener;
    }

    // Instead of using List in a constructor, we use it here so we can use in MainActivity's observer.
    public void setEngineOilList(List<EngineOil> engineOilList){
        this.mEngineOils = engineOilList;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    public class EngineOilViewHolder extends RecyclerView.ViewHolder {

        private TextView engineOil_date, perLitrePrice, engineOilQuantityLitres, totalEngineOilPrice, distanceCovered, calculatedAverage ;

        public EngineOilViewHolder(@NonNull View itemView) {
            super(itemView);
            engineOil_date = itemView.findViewById(R.id.engineOil_date);
            perLitrePrice = itemView.findViewById(R.id.perLitrePrice);
            engineOilQuantityLitres = itemView.findViewById(R.id.engineOilQuantityLitres);
            totalEngineOilPrice = itemView.findViewById(R.id.totalEngineOilPrice);
            distanceCovered = itemView.findViewById(R.id.distanceCovered);
            calculatedAverage = itemView.findViewById(R.id.calculatedAverage);


            itemView.setOnClickListener(v -> {
                EngineOil engineOil = mEngineOils.get(getAdapterPosition()); // get item at specific position
                mEngineOilClickListener.onClick(engineOil);  // This interface method sends ID to Adapter constructor method
                Log.d(TAG, "onClick: clicked on item with id = " + engineOil.getEngineOilID());
            });

        }
    }




    @NonNull
    @Override
    public EngineOilListAdapter.EngineOilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.recycler_item_engineOil, parent, false);
        return new EngineOilListAdapter.EngineOilViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EngineOilListAdapter.EngineOilViewHolder holder, int position) {

        EngineOil currentEngineOil = mEngineOils.get(position);

        holder.engineOil_date.setText(AppUtils.getFormattedDateString(currentEngineOil.getEngineOilDate()));
        holder.perLitrePrice.setText(String.valueOf(currentEngineOil.getPerLitrePrice()));
        holder.engineOilQuantityLitres.setText(String.valueOf(currentEngineOil.getEngineOilQuantityLitres()));
        holder.totalEngineOilPrice.setText(String.valueOf(currentEngineOil.getTotalEngineOilPrice()));
        holder.distanceCovered.setText(String.valueOf(currentEngineOil.getDistanceCovered()));
        holder.calculatedAverage.setText(String.valueOf(currentEngineOil.getCalculatedAverage()));


    }

    @Override
    public int getItemCount() {
        if (mEngineOils == null)
            return 0;
        else
            return mEngineOils.size();
    }


}
