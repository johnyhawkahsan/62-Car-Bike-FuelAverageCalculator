package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.repository;

import android.app.Application;
import android.util.Log;


import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.RoomDatabase;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.dao.EngineOilDao;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.EngineOil;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EngineOilRepository {
    private static final String TAG = EngineOilRepository.class.getSimpleName();

    private EngineOilDao mEngineOilDao;
    private LiveData<List<EngineOil>> mAllEngineOil;

    // constructor for repository
    public EngineOilRepository(Application application, int vehicleID) {
        RoomDatabase db = RoomDatabase.getDBINSTANCE(application);
        mEngineOilDao = db.engineOilDao(); // we receive Dao from RoomDatabase
        mAllEngineOil = mEngineOilDao.getAllEngineOilData(vehicleID);
    }

    // get all
    public LiveData<List<EngineOil>> getAllEngineOils() {
        return mAllEngineOil;
    }

    // get single item using id
    public LiveData<EngineOil> getEngineOilByID(int vehicleID, int EngineOilID) {
        return mEngineOilDao.getEngineOilByID(vehicleID, EngineOilID);
    }


    // insert new
    public void insertEngineOil(EngineOil engineOil) {
        mEngineOilDao.insertEngineOil(engineOil)
                .subscribeOn(Schedulers.io()) // do task in background
                .subscribe(new Action() {
                               @Override
                               public void run() throws Exception {
                                   // success
                                   Log.d(TAG, "insertEngineOil(EngineOil): success");
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   // error
                                   Log.d(TAG, "insertEngineOil(EngineOil): error");
                               }
                           }
                );
    }


    // update
    public void updateEngineOil(EngineOil engineOilID) {

        mEngineOilDao.updateEngineOil(engineOilID)
                .subscribeOn(Schedulers.io()) // do task in background
                .subscribe(new Action() {
                               @Override
                               public void run() throws Exception {
                                   // success
                                   Log.d(TAG, "updateEngineOil(EngineOil): success");
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   // error
                                   Log.d(TAG, "updateEngineOil(EngineOil): error");
                               }
                           }
                );

    }


    // delete single EngineOil
    public void deleteEngineOilWithID(int vehicleID, int engineOilID) {

        Observable.fromCallable(() -> {
            return false;
        })
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    mEngineOilDao.deleteEngineOilWithID(vehicleID, engineOilID);
                }); // do task in background

    }



    // delete all EngineOil data related to vehicle
    public void deleteAllEngineOilData(int vehicleID) {

        Observable.fromCallable(() -> {
            return false;
        })
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    mEngineOilDao.deleteAllEngineOilData(vehicleID);
                }); // do task in background

    }


}
