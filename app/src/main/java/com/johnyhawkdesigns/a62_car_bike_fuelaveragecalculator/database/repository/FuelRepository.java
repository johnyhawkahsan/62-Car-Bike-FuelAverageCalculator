package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.repository;

import android.app.Application;
import android.util.Log;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.RoomDatabase;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.dao.FuelDao;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Fuel;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FuelRepository {

    private static final String TAG = FuelRepository.class.getSimpleName();

    private FuelDao mFuelDao;
    private LiveData<List<Fuel>> mAllFuel;

    // constructor for repository
    public FuelRepository(Application application, int vehicleID) {
        RoomDatabase db = RoomDatabase.getDBINSTANCE(application);
        mFuelDao = db.fuelDao(); // we receive Dao from RoomDatabase
        mAllFuel = mFuelDao.getAllFuelData(vehicleID);
    }

    // get all
    public LiveData<List<Fuel>> getAllFuels() {
        return mAllFuel;
    }

    // get single item using id
    public Maybe<Fuel> getFuelByID(int vehicleID, int fuelID) {
        return mFuelDao.getFuelByID(vehicleID, fuelID);
    }


    // insert new
    public void insertFuel(Fuel fuel) {
        mFuelDao.insertFuel(fuel)
                .subscribeOn(Schedulers.io()) // do task in background
                .subscribe(new Action() {
                               @Override
                               public void run() throws Exception {
                                   // success
                                   Log.d(TAG, "insertFuel(fuel): success");
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   // error
                                   Log.d(TAG, "insertFuel(fuel): error");
                               }
                           }
                );
    }


    // update
    public void updateVehicle(Fuel fuel) {

        mFuelDao.updateFuel(fuel)
                .subscribeOn(Schedulers.io()) // do task in background
                .subscribe(new Action() {
                               @Override
                               public void run() throws Exception {
                                   // success
                                   Log.d(TAG, "updateFuel(fuel): success");
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   // error
                                   Log.d(TAG, "updateFuel(fuel): error");
                               }
                           }
                );

    }


    // delete single fuel
    public void deleteFuelWithID(int vehicleID, int fuelID) {

        Observable.fromCallable(() -> {
            return false;
        })
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    mFuelDao.deleteFuelWithID(vehicleID, fuelID);
                }); // do task in background

    }



    // delete all fuel data related to vehicle
    public void deleteAllFuelData(int vehicleID) {

        Observable.fromCallable(() -> {
            return false;
        })
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    mFuelDao.deleteAllFuelData(vehicleID);
                }); // do task in background

    }



}
