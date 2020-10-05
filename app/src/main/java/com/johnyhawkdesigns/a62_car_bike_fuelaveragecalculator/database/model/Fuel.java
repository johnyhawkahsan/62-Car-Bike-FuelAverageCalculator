package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model;

import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.DateTypeConverter;

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
public class Fuel {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fuelID")
    private int fuelID;

    // This column stores foreign key as foreignVehicleID. We can also use @ForeignKey here but we used it on start
    @ColumnInfo(name = "foreignVehicleID")
    private int foreignVehicleID;

    @Nullable
    @ColumnInfo(name = "fuelDate")
    @TypeConverters({DateTypeConverter.class})
    private Date fuelDate;

    @Nullable
    @ColumnInfo(name = "perLitrePrice")
    private String perLitrePrice;

    @Nullable
    @ColumnInfo(name = "totalFuelPrice")
    private String totalFuelPrice;

    @Nullable
    @ColumnInfo(name = "fuelQuantityLitres")
    private String fuelQuantityLitres;

    @Nullable
    @ColumnInfo(name = "averageCalculationMethod")
    private String averageCalculationMethod; // Tank full to full calculation or empty to empty calculation

    @Nullable
    @ColumnInfo(name = "distanceCovered")
    private String distanceCovered; // distance covered from previous tank full to current tank full

    @Nullable
    @ColumnInfo(name = "calculatedAverage")
    private String calculatedAverage; // Total distance in km divided by total petrol in litres i.e; 172km/ 4 ltr = 43 km per litre average


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
    public String getPerLitrePrice() {
        return perLitrePrice;
    }

    public void setPerLitrePrice(@Nullable String perLitrePrice) {
        this.perLitrePrice = perLitrePrice;
    }

    @Nullable
    public String getTotalFuelPrice() {
        return totalFuelPrice;
    }

    public void setTotalFuelPrice(@Nullable String totalFuelPrice) {
        this.totalFuelPrice = totalFuelPrice;
    }

    @Nullable
    public String getFuelQuantityLitres() {
        return fuelQuantityLitres;
    }

    public void setFuelQuantityLitres(@Nullable String fuelQuantityLitres) {
        this.fuelQuantityLitres = fuelQuantityLitres;
    }

    @Nullable
    public String getAverageCalculationMethod() {
        return averageCalculationMethod;
    }

    public void setAverageCalculationMethod(@Nullable String averageCalculationMethod) {
        this.averageCalculationMethod = averageCalculationMethod;
    }

    @Nullable
    public String getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(@Nullable String distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    @Nullable
    public String getCalculatedAverage() {
        return calculatedAverage;
    }

    public void setCalculatedAverage(@Nullable String calculatedAverage) {
        this.calculatedAverage = calculatedAverage;
    }
}
