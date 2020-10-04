package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.RoomDatabase;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.dao.VehicleDao;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VehicleRepository {

    private VehicleDao mVehicleDao;
    private LiveData<List<Vehicle>> mAllVehicles;

    // This executor method is used to bypass Async method which is complicated - Read answer here https://stackoverflow.com/questions/46462128/android-room-livedata-callback-of-update-insert
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    // constructor for repository
    public VehicleRepository (Application application){
        RoomDatabase db = RoomDatabase.getDBINSTANCE(application);
        mVehicleDao = db.vehicleDao(); // we receive vehicleDao from childRoomDatabase
        mAllVehicles = mVehicleDao.getAllVehicle();
    }

    // get all vehicles
    public LiveData<List<Vehicle>> getAllVehicles(){
        return mAllVehicles;
    }

    // get single vehicle using id
    public Maybe<Vehicle> getVehicle(int vehicleID){
        return mVehicleDao.getVehicle(vehicleID);
    }

    // insert new vehicle
    public void insertVehicle(Vehicle vehicle){

        //mVehicleDao.insert(vehicle);

/*
        // if inside DAO we are using simple void insert, we can convert it into Completable here using below method .fromAction
        Completable.fromAction(() -> mVehicleDao.insert(vehicle)
                .subscribeOn(Schedulers.io())
                .subscribe());
*/


        mVehicleDao.insert(vehicle)
                .subscribeOn(Schedulers.io()) // do task in background
                .subscribe(new Action() {
                               @Override
                               public void run() throws Exception {
                                   // success
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   // error
                               }
                           }
                );



    }

    // update vehicle
    public void updateVehicle(Vehicle vehicle){

        // =========I am using this executor method here to test it's functionality==========
        // Read answer here https://stackoverflow.com/questions/46462128/android-room-livedata-callback-of-update-insert
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mVehicleDao.update(vehicle);
            }
        });

    }

    // delete vehicle
    public void deleteVehicle(Vehicle vehicle){
        new deleteVehicleAsyncTask(mVehicleDao, vehicle).execute();

    }

    // using old AsyncTask to delete this item
    private static class deleteVehicleAsyncTask extends AsyncTask<Void, Void, Void> {
        private VehicleDao vehicleDao;
        private Vehicle vehicle;
        deleteVehicleAsyncTask(VehicleDao vehicleDao, Vehicle vehicle) {
            this.vehicleDao = vehicleDao;
            this.vehicle = vehicle;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            vehicleDao.delete(vehicle);
            return null;
        }
    }


    // delete all vehicle
    public void deleteAllVehicle(){
        Observable.fromCallable(() -> {
            return false;
        })
         .subscribeOn(Schedulers.io())
        .subscribe(aBoolean -> {
            mVehicleDao.deleteAllVehicle();
        }); // do task in background
    }


}
