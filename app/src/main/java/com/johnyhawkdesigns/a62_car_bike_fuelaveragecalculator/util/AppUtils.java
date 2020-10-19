package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AppUtils {

    private static final String TAG = AppUtils.class.getSimpleName();
    public static final String PREFERENCES = "prefs";
    public static final String perLitrePriceStr = "perLitrePriceStr";
    public static final String lastEngineOilInterval = "lastEngineOilInterval";


    // Method to get current time
    public static Date getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        return currentDate;
    }


    // Method to convert Date to Text
    public static String getFormattedDateString(Date date) {

        try {
            //spf= new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            //SimpleDateFormat spf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
            String dateString = spf.format(date);
            Date newDate = spf.parse(dateString);
            return spf.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get Date with time set to 00:00:00 - https://stackoverflow.com/questions/5050170/how-do-i-get-a-date-without-time-in-java
    public static Date getDateWithoutTime(Date dateWithTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime = sdf.parse(sdf.format(dateWithTime));
        return dateWithoutTime;
    }

    // Toast message outline
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    // Hide soft keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    // store per litre price of petrol
    public static void savePetrolPriceSharedPreference(String perLitrePriceStr, Double perLitrePrice, Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Long perLitrePriceLong = Double.doubleToRawLongBits(perLitrePrice);

        if (preferences.contains(perLitrePriceStr) && preferences.getLong(perLitrePriceStr, 0) == perLitrePriceLong) { // if this petrol price is already stored in preferences
            Log.d(TAG, "petrol price: already stored");
        } else {
            // save last petrol price
            editor.putLong(perLitrePriceStr, perLitrePriceLong); // NOTE: https://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
            editor.apply();
            Log.d(TAG, "petrol price: petrol price saved, " + "perLitrePriceStr = " + perLitrePriceStr + "perLitrePrice = " + perLitrePrice + ", perLitrePriceLong = " + perLitrePriceLong);
        }
    }


    public static Double getPetrolPerLitrePrice(String perLitrePriceStr, Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        Long perLitrePriceLong = preferences.getLong(perLitrePriceStr, 0);
        Double perLitrePriceDouble = Double.longBitsToDouble(perLitrePriceLong);

        Log.d(TAG, "petrol price: getting last petrol price perLitrePriceDouble = " + perLitrePriceDouble);
        return perLitrePriceDouble;
    }


    public static Double getLastEngineOilInterval(String lastEngineOilInterval, Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        Long lastEngineOilIntervalLong = preferences.getLong(lastEngineOilInterval, 0);
        Double lastEngineOilIntervalDouble = Double.longBitsToDouble(lastEngineOilIntervalLong);

        Log.d(TAG, "lastEngineOilIntervalDouble: getting lastEngineOilIntervalDouble = " + lastEngineOilIntervalDouble);
        return lastEngineOilIntervalDouble;
    }


    // store per litre price of petrol
    public static void saveLastEngineOilIntervalSharedPreference(String lastEngineOilIntervalStr, Double lastEngineOilInterval, Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Long lastEngineOilIntervalLong = Double.doubleToRawLongBits(lastEngineOilInterval);

        if (preferences.contains(lastEngineOilIntervalStr) && preferences.getLong(lastEngineOilIntervalStr, 0) == lastEngineOilIntervalLong) { // if this is already stored in preferences
            Log.d(TAG, "lastEngineOilInterval: already stored");
        } else {
            // save lastEngineOilInterval
            editor.putLong(lastEngineOilIntervalStr, lastEngineOilIntervalLong); // NOTE: https://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
            editor.apply();
            Log.d(TAG, "lastEngineOilInterval: lastEngineOilInterval saved, " + "lastEngineOilIntervalStr = " + lastEngineOilIntervalStr + "lastEngineOilInterval = " + lastEngineOilInterval + ", lastEngineOilIntervalLong = " + lastEngineOilIntervalLong);
        }
    }



}
