package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.DateTypeConverter;

import java.io.Serializable;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;


// === NOTE: EngineOilDao model class only contains @Entity(tableName so it means foreignKeys = @ForeignKey(entity = Vehicle.class etc is not compulsory, only @ColumnInfo(name = "foreignVehicleID") but need to confirm that
//For tutorial, visit = https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
@Entity(tableName = "fuel_table",
        foreignKeys = @ForeignKey(entity = Vehicle.class, //The entity that contains the foreign key.
                parentColumns = "vehicleID", // The column(s) of the parent entity object that contains the key.
                childColumns = "foreignVehicleID", //The column(s) of the current entity, which is the child, that specified the parent key.
                onDelete = CASCADE), //onDelete = CASCADE tells if child row will be deleted, we’d like to delete also all of it repositories.
        indices=@Index(value="foreignVehicleID"))
public class Fuel implements Serializable {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fuelID")
    private int fuelID;

    // This column stores foreign key as foreignVehicleID. We can also use @ForeignKey here but we used it on start
    @ColumnInfo(name = "foreignVehicleID")
    private int foreignVehicleID; // i.e; 2

    @Nullable
    @ColumnInfo(name = "fuelDate")
    @TypeConverters({DateTypeConverter.class})
    private Date fuelDate; // i.e; 7 october 2020

    @Nullable
    @ColumnInfo(name = "perLitrePrice")
    private Double perLitrePrice; // i.e; 105 rs

    @Nullable
    @ColumnInfo(name = "fuelQuantityLitres")
    private Double fuelQuantityLitres; // i.e; 5 litre

    @Nullable
    @ColumnInfo(name = "totalFuelPrice")
    private Double totalFuelPrice; // i.e; 5 litre x 105 rs = 525 rs (Need to add x button to calculate this as well)


    @Nullable
    @ColumnInfo(name = "averageCalculationMethod")
    private String averageCalculationMethod; // Tank full to full calculation or empty to empty calculation

    @Nullable
    @ColumnInfo(name = "startingKm")
    private Double startingKm; // i.e; previous reading 1000 km

    @Nullable
    @ColumnInfo(name = "currentKm")
    private Double currentKm; // // i.e; current reading 1172 km

    @Nullable
    @ColumnInfo(name = "distanceCovered")
    private Double distanceCovered; // i.e; current - previous  = 1172 - 1000 = 172km

    @Nullable
    @ColumnInfo(name = "calculatedAverage")
    private Double calculatedAverage; // Total distance covered in km divided by total petrol in litres i.e; 172km/ 5 ltr = 43 km per litre average

    @Nullable
    @ColumnInfo(name = "coverableDistance")
    private Double coverableDistance; // coverableDistance = fuel capacity(litre) x fuel average (km/litre) i.e; 10 litre capacity x 12 km/litre = 120 km

    @Nullable
    @ColumnInfo(name = "nextFuelFill")
    private Double nextFuelFill; // nextFuelFill = coverableDistance + currentKm

    @Nullable
    @ColumnInfo(name = "totalFuelCapacity")
    private int totalFuelCapacity; // same as vehicleFuelCapacity in "vehicle" i.e; 40 litre


    @Nullable
    @ColumnInfo(name = "tankFull")
    private Boolean tankFull;

    @Nullable
    public Boolean getTankFull() {
        return tankFull;
    }

    public void setTankFull(@Nullable Boolean tankFull) {
        this.tankFull = tankFull;
    }



    public int getTotalFuelCapacity() {
        return totalFuelCapacity;
    }

    public void setTotalFuelCapacity(int totalFuelCapacity) {
        this.totalFuelCapacity = totalFuelCapacity;
    }

    @Nullable
    public Double getCoverableDistance() {
        return coverableDistance;
    }

    public void setCoverableDistance(@Nullable Double coverableDistance) {
        this.coverableDistance = coverableDistance;
    }

    @Nullable
    public Double getNextFuelFill() {
        return nextFuelFill;
    }

    public void setNextFuelFill(@Nullable Double nextFuelFill) {
        this.nextFuelFill = nextFuelFill;
    }




    public int getFuelID() {
        return fuelID;
    }

    public void setFuelID(int fuelID) {
        this.fuelID = fuelID;
    }

    public int getForeignVehicleID() {
        return foreignVehicleID;
    }

    public void setForeignVehicleID(int foreignVehicleID) {
        this.foreignVehicleID = foreignVehicleID;
    }

    @Nullable
    public Date getFuelDate() {
        return fuelDate;
    }

    public void setFuelDate(@Nullable Date fuelDate) {
        this.fuelDate = fuelDate;
    }

    @Nullable
    public Double getPerLitrePrice() {
        return perLitrePrice;
    }

    public void setPerLitrePrice(@Nullable Double perLitrePrice) {
        this.perLitrePrice = perLitrePrice;
    }

    @Nullable
    public Double getFuelQuantityLitres() {
        return fuelQuantityLitres;
    }

    public void setFuelQuantityLitres(@Nullable Double fuelQuantityLitres) {
        this.fuelQuantityLitres = fuelQuantityLitres;
    }

    @Nullable
    public Double getTotalFuelPrice() {
        return totalFuelPrice;
    }

    public void setTotalFuelPrice(@Nullable Double totalFuelPrice) {
        this.totalFuelPrice = totalFuelPrice;
    }

    @Nullable
    public String getAverageCalculationMethod() {
        return averageCalculationMethod;
    }

    public void setAverageCalculationMethod(@Nullable String averageCalculationMethod) {
        this.averageCalculationMethod = averageCalculationMethod;
    }

    @Nullable
    public Double getStartingKm() {
        return startingKm;
    }

    public void setStartingKm(@Nullable Double startingKm) {
        this.startingKm = startingKm;
    }

    @Nullable
    public Double getCurrentKm() {
        return currentKm;
    }

    public void setCurrentKm(@Nullable Double currentKm) {
        this.currentKm = currentKm;
    }

    @Nullable
    public Double getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(@Nullable Double distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    @Nullable
    public Double getCalculatedAverage() {
        return calculatedAverage;
    }

    public void setCalculatedAverage(@Nullable Double calculatedAverage) {
        this.calculatedAverage = calculatedAverage;
    }
}
