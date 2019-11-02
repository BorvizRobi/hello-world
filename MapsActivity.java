package com.example.gpstracker;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager mlocationManager;
    TextView tvLatitude;
    TextView tvLongitude;
    public static String tvLongi;
    public static String tvLati;
    private boolean firstCall = true;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CheckPermission();
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }


    protected void onPause() {
        super.onPause();
        mlocationManager.removeUpdates(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    public void getLocation() {
        try {
            mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 1f, this);
//           mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 1f, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public String getLocationName(Location location){
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }else return "error";
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLocationChanged(@org.jetbrains.annotations.NotNull Location location) {
        LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
        if(firstCall){
            marker = mMap.addMarker(new MarkerOptions().position(mylocation).title("You are in " + getLocationName(location)));
            marker.showInfoWindow();
            firstCall = false;
        }else {
            mMap.addPolyline(new PolylineOptions()
                    .add(marker.getPosition(), mylocation)
                    .width(5)
                    .color(Color.BLUE));
            marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(mylocation).title("You are in " + getLocationName(location)));
            marker.showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17f));

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MapsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
