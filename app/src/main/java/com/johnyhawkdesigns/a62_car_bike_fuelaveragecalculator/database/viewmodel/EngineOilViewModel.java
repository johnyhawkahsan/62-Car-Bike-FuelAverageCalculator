package com.johnyhawkdesigns.a62_car_bike_EngineOilaveragecalculator.database.viewmodel;

import android.app.Application;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.EngineOil;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.repository.EngineOilRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EngineOilViewModel extends AndroidViewModel {



    private EngineOilRepository engineOilRepository;
    private LiveData<List<EngineOil>> mAllEngineOils;

    public EngineOilViewModel(@NonNull Application application, int vehicleID) {
        super(application);
        engineOilRepository = new EngineOilRepository(application, vehicleID);
        mAllEngineOils = engineOilRepository.getAllEngineOils();
    }

    // get all
    public LiveData<List<EngineOil>> getAllEngineOil() {
        return mAllEngineOils;
    }


    // get single item using id
    public LiveData<EngineOil> getEngineOilByID(int vehicleID, int engineOilID) {
        return engineOilRepository.getEngineOilByID(vehicleID, engineOilID);
    }


    // insert new
    public void insertEngineOil(EngineOil engineOil) {
        engineOilRepository.insertEngineOil(engineOil);
    }


    // update
    public void updateVehicle(EngineOil engineOil) {
        engineOilRepository.updateEngineOil(engineOil);
    }


    // delete single EngineOil
    public void deleteEngineOilWithID(int vehicleID, int engineOilID) {
        engineOilRepository.deleteEngineOilWithID(vehicleID, engineOilID);
    }


    // delete all EngineOil data related to vehicle
    public void deleteAllEngineOilData(int vehicleID) {
        engineOilRepository.deleteAllEngineOilData(vehicleID);
    }

}
