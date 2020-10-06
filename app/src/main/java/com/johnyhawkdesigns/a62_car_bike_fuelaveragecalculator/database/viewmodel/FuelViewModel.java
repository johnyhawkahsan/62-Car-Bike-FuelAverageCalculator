package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel;

import android.app.Application;
import android.util.Log;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.repository.FuelRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FuelViewModel extends AndroidViewModel {

    private FuelRepository fuelRepository;
    private LiveData<List<Fuel>> mAllFuels;

    public FuelViewModel(@NonNull Application application, int vehicleID) {
        super(application);
        fuelRepository = new FuelRepository(application, vehicleID);
        mAllFuels = fuelRepository.getAllFuels();
    }

    // get all
    public LiveData<List<Fuel>> getAllFuel() {
        return mAllFuels;
    }


    // get single item using id
    public LiveData<Fuel> getFuelByID(int vehicleID, int fuelID) {
        return fuelRepository.getFuelByID(vehicleID, fuelID);
    }


    // insert new
    public void insertFuel(Fuel fuel) {
        fuelRepository.insertFuel(fuel);
    }


    // update
    public void updateVehicle(Fuel fuel) {
        fuelRepository.updateVehicle(fuel);
    }


    // delete single fuel
    public void deleteFuelWithID(int vehicleID, int fuelID) {
        fuelRepository.deleteFuelWithID(vehicleID, fuelID);
    }


    // delete all fuel data related to vehicle
    public void deleteAllFuelData(int vehicleID) {
        fuelRepository.deleteAllFuelData(vehicleID);
    }
}
