package com.johnyhawkdesigns.a62_car_bike_EngineOilaveragecalculator.database.dao;


import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.EngineOil;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;

public interface EngineOilDao {
    @Query("SELECT * FROM engineOil_table WHERE foreignVehicleID == :foreignVehicleID ORDER BY engineOilID DESC")
    LiveData<List<EngineOil>> getAllEngineOilData(int foreignVehicleID);

    @Query("SELECT * FROM engineOil_table WHERE foreignVehicleID == :foreignVehicleID AND engineOilID == :engineOilID")
    //Maybe<EngineOil> getEngineOilByID(int foreignVehicleID, int engineOilID); // using Maybe because Single caused an error, I think due to the possibility of multiple returned items
    LiveData<EngineOil> getEngineOilByID(int foreignVehicleID, int engineOilID); // LiveData also worked perfectly but not using here because of the problem caused by "delete" command in VehicleDetailFragment


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(final EngineOil engineOil);// currently, we must put final before user variable or you will get error when compile

    @Insert
    void insertAll(EngineOil... engineOils);

    @Update
    void update(EngineOil engineOil); // for update, I am using executor inside Repository

    @Delete
    void delete(final EngineOil engineOil);

    @Query("DELETE FROM engineOil_table WHERE foreignVehicleID = :foreignVehicleID")
    void deleteAllEngineOilData(int foreignVehicleID);

    @Query("DELETE FROM engineOil_table WHERE foreignVehicleID = :foreignVehicleID AND engineOilID = :engineOilID")
    void deleteEngineOilWithID(int foreignVehicleID, int engineOilID);
}
