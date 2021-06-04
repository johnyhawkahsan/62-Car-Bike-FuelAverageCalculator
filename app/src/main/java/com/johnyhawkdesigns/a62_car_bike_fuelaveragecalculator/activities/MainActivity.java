package com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.R;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.model.Vehicle;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.database.viewmodel.VehicleViewModel;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.AddEditVehicleFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.VehicleDetailsFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.fragments.VehicleListFragment;
import com.johnyhawkdesigns.a62_car_bike_fuelaveragecalculator.util.AppUtils;

public class MainActivity extends AppCompatActivity 
            implements VehicleListFragment.VehicleListFragmentListener, VehicleDetailsFragment.VehicleDetailsFragmentListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    VehicleViewModel vehicleViewModel;
    private AdView mAdView;

    private VehicleListFragment vehicleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        // actual AdMob app id (To be added to manifest) = ca-app-pub-5736402802184272~3006012451

        // test AdMob adUnitId (To be added to adView) = ca-app-pub-3940256099942544/6300978111
        // actual AdMob adUnitId (To be added to adView) = ca-app-pub-5736402802184272/3524408273

        mAdView = findViewById(R.id.adView);
        // mAdView.setAdUnitId(); // we can also set adUnitID here programmatically
        // mAdView.setBannerSize(); // if setting ad id programmatically, we should set banner programmatically also

        // Create an ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        // if want to perform some task on ad activity, we need to implement adListener
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
                //Toast.makeText(MainActivity.this, "Ad loaded", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onAdLoaded: Ad loaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest); // reload the ad after error
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that covers the screen.
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        vehicleViewModel = new VehicleViewModel(getApplication());

        vehicleListFragment = new VehicleListFragment(); // load vehicleListFragment inside frame

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.vehicleFragmentContainer, vehicleListFragment); // add list fragment inside container
        transaction.commit();



    }

    @Override
    public void onAddNewVehicle() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditVehicleFragment addEditVehicleFragment = AddEditVehicleFragment.newInstance(); // here we are passing nothing in newInstance
        addEditVehicleFragment.show(fragmentManager, "add_edit_vehicle_fragment");

    }

    @Override
    public void onVehicleSelected(int vehicleID) {

        Log.d(TAG, "onVehicleSelected: id received inside MainActivity = " + vehicleID);

        VehicleDetailsFragment vehicleDetailsFragment = new VehicleDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("vehicleID", vehicleID);
        vehicleDetailsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.vehicleFragmentContainer, vehicleDetailsFragment);
        transaction.addToBackStack(null); // now if in detailFragment, we press back button, app does not close.
        transaction.commit();

    }


    @Override
    public void onVehicleDeleted() {
        Log.d(TAG, "onVehicleDeleted: ");
    }

    @Override
    public void onEditVehicle(int vehicleID) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditVehicleFragment addEditVehicleFragment = AddEditVehicleFragment.newInstance(vehicleID);
        addEditVehicleFragment.show(fragmentManager, "add_edit_vehicle_fragment");

    }


/*
    // Removed Delete all vehicle option from menu because (i) it appeared everywhere (ii) not required

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_delete_all:

                // Build alert dialog for confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppUtils.showMessage(getBaseContext(), "Delete all vehicles success");
                        vehicleViewModel.deleteAllVehicle();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }



}