package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel;

import android.app.Application;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.repository.VehicleRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class VehicleViewModel extends AndroidViewModel {


    private VehicleRepository vehicleRepository;
    private LiveData<List<Vehicle>> mAllVehicles;

    public VehicleViewModel(@NonNull Application application) {
        super(application);
        vehicleRepository = new VehicleRepository(application);
        mAllVehicles = vehicleRepository.getAllVehicles();
    }

    // get all vehicles
    public LiveData<List<Vehicle>> getAllVehicles(){
        return mAllVehicles;
    }


    // get single vehicle using id
    public LiveData<Vehicle> getVehicle(int vehicleID){
        return vehicleRepository.getVehicle(vehicleID);
    }

    // insert new vehicle
    public void insertVehicle(Vehicle vehicle){
        vehicleRepository.insertVehicle(vehicle);
    }

    // update vehicle
    public void updateVehicle(Vehicle vehicle){
        vehicleRepository.updateVehicle(vehicle);
    }

    // delete vehicle
    public void deleteVehicle(Vehicle vehicle){
        vehicleRepository.deleteVehicle(vehicle);
    }


}
