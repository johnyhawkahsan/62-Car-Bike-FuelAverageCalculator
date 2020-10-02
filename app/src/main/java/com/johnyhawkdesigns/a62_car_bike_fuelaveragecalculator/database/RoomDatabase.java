package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database;

import android.content.Context;
import android.util.Log;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.dao.VehicleDao;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;

import androidx.room.Database;
import androidx.room.Room;

@Database(entities = {Vehicle.class}, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static String DB_NAME = "vehicle-db";

    private static RoomDatabase DBINSTANCE;

    // This method will get DAO objects
    public abstract VehicleDao vehicleDao();

    //Singleton pattern method
    public static RoomDatabase getDBINSTANCE(Context context){
        if (DBINSTANCE == null){
            synchronized (RoomDatabase.class){
                if (DBINSTANCE == null){
                    DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class,
                            DB_NAME)
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return DBINSTANCE;
    }

}
