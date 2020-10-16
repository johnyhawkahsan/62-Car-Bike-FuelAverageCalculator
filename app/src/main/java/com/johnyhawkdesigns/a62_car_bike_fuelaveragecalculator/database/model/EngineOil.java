package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.DateTypeConverter;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "engineOil_table")
public class EngineOil {

    @PrimaryKey
    @ColumnInfo(name = "engineOilID")
    private int engineOilID;

    // This column stores foreign key as foreignVehicleID. We can also use @ForeignKey here but we used it on start
    @ColumnInfo(name = "foreignVehicleID")
    private int foreignVehicleID;

    @Nullable
    @ColumnInfo(name = "eoil_Date")
    @TypeConverters({DateTypeConverter.class})
    private Date eoil_Date;

    @Nullable
    @ColumnInfo(name = "eoil_description")
    private String eoil_description; // i.e; ZIC 10-40 Mobil oil

    @Nullable
    @ColumnInfo(name = "eoil_quantityLitres")
    private Double eoil_quantityLitres; // i.e; 3 litre

    @Nullable
    @ColumnInfo(name = "eoil_Price")
    private Double eoil_Price; // i.e; 2800 rs

    @Nullable
    @ColumnInfo(name = "eoil_interval")
    private Double eoil_interval; // i.e; 3000 km

    @Nullable
    @ColumnInfo(name = "eoil_currentMileage")
    private Double eoil_currentMileage; // current km shown in meter?? i.e; 10,3000

    @Nullable
    @ColumnInfo(name = "eoil_previousMileage")
    private Double eoil_previousMileage; // last engine oil at how many km?? i.e; 10,0000


    @Nullable
    @ColumnInfo(name = "eoil_totalDistance")
    private Double eoil_totalDistance; // Total distance covered ?? i.e; eoil_currentMileage - eoil_previousMileage = 10,3000 - 10,0000 = 3000 km

    @Nullable
    @ColumnInfo(name = "nextOilChangeAt")
    private Double nextOilChangeAt;  // eoil_currentMileage + interval = 10,3000 + 3000 = 10,6000 km


    public int getEngineOilID() {
        return engineOilID;
    }

    public void setEngineOilID(int engineOilID) {
        this.engineOilID = engineOilID;
    }

    public int getForeignVehicleID() {
        return foreignVehicleID;
    }

    public void setForeignVehicleID(int foreignVehicleID) {
        this.foreignVehicleID = foreignVehicleID;
    }

    @Nullable
    public Date getEoil_Date() {
        return eoil_Date;
    }

    public void setEoil_Date(@Nullable Date eoil_Date) {
        this.eoil_Date = eoil_Date;
    }

    @Nullable
    public String getEoil_description() {
        return eoil_description;
    }

    public void setEoil_description(@Nullable String eoil_description) {
        this.eoil_description = eoil_description;
    }

    @Nullable
    public Double getEoil_quantityLitres() {
        return eoil_quantityLitres;
    }

    public void setEoil_quantityLitres(@Nullable Double eoil_quantityLitres) {
        this.eoil_quantityLitres = eoil_quantityLitres;
    }

    @Nullable
    public Double getEoil_Price() {
        return eoil_Price;
    }

    public void setEoil_Price(@Nullable Double eoil_Price) {
        this.eoil_Price = eoil_Price;
    }

    @Nullable
    public Double getEoil_interval() {
        return eoil_interval;
    }

    public void setEoil_interval(@Nullable Double eoil_interval) {
        this.eoil_interval = eoil_interval;
    }

    @Nullable
    public Double getEoil_currentMileage() {
        return eoil_currentMileage;
    }

    public void setEoil_currentMileage(@Nullable Double eoil_currentMileage) {
        this.eoil_currentMileage = eoil_currentMileage;
    }

    @Nullable
    public Double getEoil_previousMileage() {
        return eoil_previousMileage;
    }

    public void setEoil_previousMileage(@Nullable Double eoil_previousMileage) {
        this.eoil_previousMileage = eoil_previousMileage;
    }

    @Nullable
    public Double getEoil_totalDistance() {
        return eoil_totalDistance;
    }

    public void setEoil_totalDistance(@Nullable Double eoil_totalDistance) {
        this.eoil_totalDistance = eoil_totalDistance;
    }

    @Nullable
    public Double getNextOilChangeAt() {
        return nextOilChangeAt;
    }

    public void setNextOilChangeAt(@Nullable Double nextOilChangeAt) {
        this.nextOilChangeAt = nextOilChangeAt;
    }
}
