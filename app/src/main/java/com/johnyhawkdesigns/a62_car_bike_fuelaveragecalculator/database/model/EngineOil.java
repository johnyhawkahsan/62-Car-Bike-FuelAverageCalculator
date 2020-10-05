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
    @ColumnInfo(name = "engineOilDate")
    @TypeConverters({DateTypeConverter.class})
    private Date engineOilDate;

    @Nullable
    @ColumnInfo(name = "lastOilChangeDistance")
    private String lastOilChangeDistance; // last engine oil at how many km?? i.e; 10,0000

    @Nullable
    @ColumnInfo(name = "intervalForOilChange")
    private String intervalForOilChange; // i.e; 3000 km

    @Nullable
    @ColumnInfo(name = "finalDistance")
    private String finalDistance; // what will be the final km?? i.e; 10,3000

    @Nullable
    @ColumnInfo(name = "distanceCovered")
    private String distanceCovered;  // finalDistance - startingDistance = distanceCovered now distance covered need to be equal to interval set

    @Nullable
    @ColumnInfo(name = "engineOilQuantity")
    private String engineOilQuantity; // i.e; 3 litre

    @Nullable
    @ColumnInfo(name = "engineOilPrice")
    private String engineOilPrice; // i.e; 2800 rs



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
    public Date getEngineOilDate() {
        return engineOilDate;
    }

    public void setEngineOilDate(@Nullable Date engineOilDate) {
        this.engineOilDate = engineOilDate;
    }

    @Nullable
    public String getLastOilChangeDistance() {
        return lastOilChangeDistance;
    }

    public void setLastOilChangeDistance(@Nullable String lastOilChangeDistance) {
        this.lastOilChangeDistance = lastOilChangeDistance;
    }

    @Nullable
    public String getIntervalForOilChange() {
        return intervalForOilChange;
    }

    public void setIntervalForOilChange(@Nullable String intervalForOilChange) {
        this.intervalForOilChange = intervalForOilChange;
    }

    @Nullable
    public String getFinalDistance() {
        return finalDistance;
    }

    public void setFinalDistance(@Nullable String finalDistance) {
        this.finalDistance = finalDistance;
    }

    @Nullable
    public String getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(@Nullable String distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    @Nullable
    public String getEngineOilQuantity() {
        return engineOilQuantity;
    }

    public void setEngineOilQuantity(@Nullable String engineOilQuantity) {
        this.engineOilQuantity = engineOilQuantity;
    }

    @Nullable
    public String getEngineOilPrice() {
        return engineOilPrice;
    }

    public void setEngineOilPrice(@Nullable String engineOilPrice) {
        this.engineOilPrice = engineOilPrice;
    }
}
