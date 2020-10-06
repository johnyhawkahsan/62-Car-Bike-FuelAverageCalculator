package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.dao;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface FuelDao {
    @Query("SELECT * FROM fuel_table WHERE foreignVehicleID == :foreignVehicleID ORDER BY fuelID DESC")
    LiveData<List<Fuel>> getAllFuelData(int foreignVehicleID);

    @Query("SELECT * FROM fuel_table WHERE foreignVehicleID == :foreignVehicleID AND fuelID == :fuelID")
    //Maybe<Fuel> getFuelByID(int foreignVehicleID, int fuelID); // using Maybe because Single caused an error, I think due to the possibility of multiple returned items
    LiveData<Fuel> getFuelByID(int foreignVehicleID, int fuelID); // LiveData also worked perfectly but not using here because of the problem caused by "delete" command in VehicleDetailFragment


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFuel(final Fuel fuel);// currently, we must put final before user variable or you will get error when compile

    @Insert
    void insertAll(Fuel... fuels);

    @Update
    Completable updateFuel(final Fuel fuel);

    @Delete
    void deleteFuel(final Fuel fuel);

    @Query("DELETE FROM fuel_table WHERE foreignVehicleID = :foreignVehicleID")
    void deleteAllFuelData(int foreignVehicleID);

    @Query("DELETE FROM fuel_table WHERE foreignVehicleID = :foreignVehicleID AND fuelID = :fuelID")
    void deleteFuelWithID(int foreignVehicleID, int fuelID);

}
