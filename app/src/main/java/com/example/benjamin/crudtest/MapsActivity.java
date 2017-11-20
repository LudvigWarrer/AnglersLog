package com.example.benjamin.crudtest;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        float zoomLevel = 16.0f;
        LatLng fishLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(fishLocation).title("Fish here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fishLocation, zoomLevel));
    }
}


//    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
//    private GoogleMap mMap;
//    private GoogleApiClient mGoogleApiClient;
//    public static final String TAG = MapsActivity.class.getSimpleName();
//    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
//    private LocationRequest mLocationRequest;

//
//
//        // Create the LocationRequest object
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
//                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
//
////        ActivityCompat.requestPermissions(this,
////                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
////                REQUEST_CODE_FINE_GPS);
//
////        int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.class,
////                Manifest.permission.ACCESS_FINE
////                _LOCATION);
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }



//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        Log.i(TAG, "onConnected virker :)");
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (location == null){
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
////            Log.d(TAG, "Couldn't find any location :/");
//            Log.i(TAG, "this is the if");
//        } else {
//            handleNewLocation(location);
//            Log.i(TAG, "This is the else");
//        }
//        Log.i(TAG, "Og nu er vi ude igen");
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//    }
//
//
//
//    private void handleNewLocation(Location location){
//        float zoomLevel = 16.0f;
//        Log.d(TAG, location.toString());
//        Log.i(TAG, "Coulnd't handle new location");
//        double currentLatitude = location.getLatitude();
//        double currentLongitude = location.getLongitude();
//        Log.i(TAG, "Latitude is: " + currentLatitude + " and Longitude is: " + currentLongitude);
//        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
//        MarkerOptions options = new MarkerOptions().position(latLng).title("Jeg er her");
//        mMap.addMarker(options);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        handleNewLocation(location);
//    }
//
//}
