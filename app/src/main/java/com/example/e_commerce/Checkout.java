package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.anychart.anychart.ValueDataEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Button btn_checkout;
    private TextView total ;

    RecyclerView recyclerView;
    ProductRvAdapterCheckout productRvAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private int orderID ;
    ArrayList<ProductRvModel> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(getApplicationContext());

        setContentView(R.layout.activity_checkout);

        total = (TextView) findViewById(R.id.total);
        btn_checkout = findViewById(R.id.btn_checkout);

        fillRCV();

        total.setText(String.valueOf(StaticVariables.total));
        productRvAdapter.setOnItemClickListener(new ProductRvAdapterCheckout.OnItemClickListener() {
            @Override
            public void addItemClick(int position) {
                for(int i=0 ; i<StaticVariables.products.size(); i++){
                    if(StaticVariables.products.get(i).getProductID().equals(items.get(i).getID())){
                        StaticVariables.products.get(i).quantity++;
                        StaticVariables.total+=StaticVariables.products.get(i).price;
                        break;
                    }
                }
                total.setText(String.valueOf(StaticVariables.total));

            }

            @Override
            public void removeItemClick(int position) {

                for(int i=0 ; i<StaticVariables.products.size(); i++){
                    if(StaticVariables.products.get(i).getProductID().equals(items.get(position).getID())){
                        StaticVariables.products.get(i).quantity--;
                        StaticVariables.total-=StaticVariables.products.get(i).price;
                        if(StaticVariables.products.get(i).quantity == 0){
                            StaticVariables.products.remove(i);
                            items.remove(position);
                            productRvAdapter.notifyItemRemoved(position);
                            productRvAdapter.notifyItemRangeChanged(position, items.size());
                        }

                        break;
                    }
                }
                total.setText(String.valueOf(StaticVariables.total));

            }


        });


        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ShippingAddress.class));

                finish();
            }

        });
    }

    private void fillRCV () {
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        for(int i=0 ; i < StaticVariables.products.size() ; i++){
                            if(StaticVariables.products.get(i).productID.equals(dataSnapshot1.getKey())){
                                items.add(new ProductRvModel(dataSnapshot1.child("image").getValue(String.class)
                                        , dataSnapshot1.child("price").getValue(String.class),
                                        dataSnapshot1.child("proName").getValue(String.class),
                                        String.valueOf(dataSnapshot1.getKey())));
                            }
                        }
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recyclerView = findViewById(R.id.recyclerView3);
        productRvAdapter = new ProductRvAdapterCheckout(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(productRvAdapter);


    }


}