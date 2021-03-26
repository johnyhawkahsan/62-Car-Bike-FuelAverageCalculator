package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.EngineOil;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

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

        private TextView eoil_Date, eoil_description, eoil_Price, nextOilChangeAt ;

        public EngineOilViewHolder(@NonNull View itemView) {
            super(itemView);
            eoil_Date = itemView.findViewById(R.id.eoil_Date);
            eoil_description = itemView.findViewById(R.id.eoil_description);
            eoil_Price = itemView.findViewById(R.id.eoil_Price);
            nextOilChangeAt = itemView.findViewById(R.id.nextOilChangeAt);


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
        View itemView = mInflator.inflate(R.layout.recycler_item_engine_oil, parent, false);
        return new EngineOilListAdapter.EngineOilViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EngineOilListAdapter.EngineOilViewHolder holder, int position) {

        EngineOil currentEngineOil = mEngineOils.get(position);

        holder.eoil_Date.setText(AppUtils.getFormattedDateString(currentEngineOil.getEoil_Date()));
        holder.eoil_description.setText(String.valueOf(currentEngineOil.getEoil_description()));
        holder.eoil_Price.setText("Rs:" + AppUtils.removeTrailingZero(String.valueOf(currentEngineOil.getEoil_Price())));
        holder.nextOilChangeAt.setText(AppUtils.removeTrailingZero(String.valueOf(currentEngineOil.getNextOilChangeAt())) + " km");

    }

    @Override
    public int getItemCount() {
        if (mEngineOils == null)
            return 0;
        else
            return mEngineOils.size();
    }


}
