package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.dao;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface VehicleDao {

    @Query("SELECT * FROM vehicle_table ORDER BY vehicleID ASC")
    LiveData<List<Vehicle>> getAllVehicle();

    @Query("SELECT * FROM vehicle_table WHERE vehicleID = :vehicleID")
    //LiveData<Vehicle> getVehicle(int vehicleID); // LiveData also worked perfectly but not using here because of the problem caused by "delete" command in VehicleDetailFragment
    Maybe<Vehicle> getVehicle(int vehicleID); // using Maybe because Single caused an error, I think due to the possibility of multiple returned items


    @Insert
    // Below are 2 methods: 1 method is using simple void insert and converting to Completable inside Repository, 2nd method is using Completable here
    //void insert(Vehicle Vehicle); // converting to completable inside Repository class
    Completable insert(final Vehicle Vehicle);// currently, we must put final before user variable or you will get error when compile

    @Insert
    void insertAll(Vehicle... Vehicles);

    @Update
    void update(Vehicle Vehicle); // for update, I am using executor inside Repository

    @Delete
    //void delete(Vehicle Vehicle);
    void delete(final Vehicle Vehicle);

    @Query("DELETE FROM vehicle_table")
    //void deleteAllVehicle();
    void deleteAllVehicle();
}
