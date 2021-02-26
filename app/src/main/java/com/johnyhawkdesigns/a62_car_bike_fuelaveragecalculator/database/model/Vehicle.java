package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vehicle_table")
public class Vehicle {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "vehicleID")
    private int vehicleID;

    @ColumnInfo(name = "vehicleType")
    private String vehicleType;

    @ColumnInfo(name = "vehicleMake")
    private String vehicleMake;

    @Nullable
    @ColumnInfo(name = "vehicleModel")
    private String vehicleModel;

    @Nullable
    @ColumnInfo(name = "vehicleFuelCapacity")
    private int vehicleFuelCapacity;



    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    @Nullable
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(@Nullable String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Nullable
    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(@Nullable String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    @Nullable
    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(@Nullable String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    @Nullable
    public int getVehicleFuelCapacity() {
        return vehicleFuelCapacity;
    }

    public void setVehicleFuelCapacity(@Nullable int vehicleFuelCapacity) {
        this.vehicleFuelCapacity = vehicleFuelCapacity;
    }

}
