package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.anychart.anychart.ValueDataEntry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ShippingAddress extends AppCompatActivity implements OnMapReadyCallback {


    Button button;
    Button confirm_btn;
    TextView textView;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap mMap;
    myLocationListener myLocationListener;
    LocationManager locationManager;
    List<Address> addressListd;
    Location loc = null;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    int orderID;
    String address = "";



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(30.044441960, 31.235711600),
                8
        ));

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //databaseReference.child("orderes").child()




                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", StaticVariables.email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.post("https://ordeconfirmationemail.herokuapp.com/")
                        .addJSONObjectBody(jsonObject)
                        .setTag("test")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                            }
                            @Override
                            public void onError(ANError error) {

                            }
                        });

                databaseReference.child("orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderID = Integer.parseInt(snapshot.child("totalOrders").getValue().toString());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH)+1;

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        databaseReference.child("orders").child("totalOrders").setValue(String.valueOf(++orderID));
                        databaseReference.child("orders").child(String.valueOf(orderID)).child("email").setValue(StaticVariables.email);
                        databaseReference.child("orders").child(String.valueOf(orderID)).child("month").setValue(String.valueOf(month));
                        databaseReference.child("orders").child(String.valueOf(orderID)).child("total").setValue(String.valueOf(StaticVariables.total));
                        databaseReference.child("orders").child(String.valueOf(orderID)).child("address").setValue(String.valueOf(address));

                        for(int i=0 ; i < StaticVariables.products.size() ; i++){
                            databaseReference.child("orders").child(String.valueOf(orderID)).child("products").child(String.valueOf(StaticVariables.products.get(i).productID))
                                    .setValue(String.valueOf(StaticVariables.products.get(i).quantity));
                        }

                    }
                }, 2000);

                databaseReference.child("orders").addValueEventListener(new ValueEventListener() {
                    int order = 1;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        StaticVariables.dataEntries.clear();
//                        StaticVariables.dataEntries.notify();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            if(dataSnapshot.child("email").getValue(String.class)!=null){
                                if(dataSnapshot.child("email").getValue(String.class).equals(StaticVariables.email)){
                                    int b;
                                    try {
                                        b = Integer.parseInt(dataSnapshot.child("total").getValue(String.class));
                                    } catch (NumberFormatException e) {
                                        b=1;
                                    }
                                    StaticVariables.dataEntries.add(new ValueDataEntry("Order " + String.valueOf(++order) ,
                                            b));
                                }

                            }
                        }
                        if(StaticVariables.dataEntries.size()==0)
                            StaticVariables.dataEntries.add(new ValueDataEntry("Order 1", 1));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                startActivity(new Intent(getApplicationContext(), Chart.class));
                finish();
            }


        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Geocoder coder = new Geocoder(getApplicationContext());

                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                } catch (SecurityException x){
                    Toast.makeText(getApplicationContext(), "Not Allowed to access this current location",
                            Toast.LENGTH_SHORT).show();
                }
                getLocation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(loc!=null){
                            LatLng myPosition = new LatLng(loc.getLatitude(),
                                    loc.getLongitude());
                            try {
                                addressListd = coder.getFromLocation(myPosition.latitude,
                                        myPosition.longitude, 1);

                                if(!addressListd.isEmpty()){
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
                }, 5000);

            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addressList;
                try {
                    addressList = coder.getFromLocation(marker.getPosition().latitude,
                            marker.getPosition().longitude, 1);

                    if(!addressList.isEmpty()){
                        address = "";

                        for(int i=0 ; i <= addressList.get(0).getMaxAddressLineIndex() ; i++){
                            address+= addressList.get(0).getAddressLine(i)+", ";
                        }

                        textView.setText(address);
                    }
                    else {

                        Toast.makeText(getApplicationContext(), "No Address for this Location",
                                Toast.LENGTH_SHORT).show();
                        textView.setText("");
                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Can't get the address, Check your network.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        button = (Button) findViewById(R.id.btn_checkout);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        textView = (EditText) findViewById(R.id.register_name2);
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    loc = location;
                    try {
                        Geocoder geocoder = new Geocoder(ShippingAddress.this,
                                Locale.getDefault());

                        List<Address> address = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
//                        textView.setText(String.valueOf(address.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}