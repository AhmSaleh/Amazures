package com.example.e_commerce;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button button;
    TextView textView;
    FusedLocationProviderClient fusedLocationProviderClient;
    myLocationListener myLocationListener;
    LocationManager locationManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        context = getApplicationContext();
        button = (Button) findViewById(R.id.button1);
        textView = (TextView) findViewById(R.id.edittext1);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        myLocationListener = new myLocationListener(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    6000,0,myLocationListener);
        } catch (SecurityException ex){
            Toast.makeText(getApplicationContext(), "Not Allowed to access this location",
                    Toast.LENGTH_SHORT).show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(30.044441960, 31.235711600),
                8
        ));

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mMap.clear();
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addressListd;
                Location loc = null;

                try {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                } catch (SecurityException x){
                    Toast.makeText(getApplicationContext(), "Not Allowed to access this current location",
                            Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getApplicationContext(), String.valueOf(loc),
//                        Toast.LENGTH_SHORT).show();
                if(loc!=null){

                    Toast.makeText(getApplicationContext(), "loc.getLatitude()", Toast.LENGTH_SHORT).show();
                    LatLng myPosition = new LatLng(loc.getLatitude(),
                            loc.getLongitude());
                    try {

                        addressListd = coder.getFromLocation(myPosition.latitude,
                                myPosition.longitude, 1);

                        if(!addressListd.isEmpty()){

                            String address = "";
                            for(int i=0; i<= addressListd.get(0).getMaxAddressLineIndex(); i++)
                                address+= addressListd.get(0).getAddressLine(i)+", ";

                            mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location").snippet(address)).
                                    setDraggable(true);
                            textView.setText(address);
                        }

                    } catch (IOException e) {
                        mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location"));
                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));
                } else {
                    Toast.makeText(getApplicationContext(), "Please wait until your position is determined",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
//
//        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.044441960, 31.235711600)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(30.044441960, 31.235711600)));
    }
}