package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String al;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private ProductRvAdapter productRvAdapter;
    private ImageView btn_logout;
    private ImageView btn_cart;

    List<String> sum = new ArrayList<>();

    ImageView camera ;
    ImageView voice ;
    EditText editText;
    int clickedIndex= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        btn_logout = (ImageView) findViewById(R.id.home_logout);
        editText = (EditText) findViewById(R.id.editText);
        btn_cart = (ImageView) findViewById(R.id.home_btn);
        camera = (ImageView) findViewById(R.id.camera);
        voice = (ImageView) findViewById(R.id.voice);

        OrderModel.helperArray.add("asd");

        ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.drawable.all_products, "All Categories"));
        item.add(new StaticRvModel(R.drawable.supermarket, "Supermarket"));
        item.add(new StaticRvModel(R.drawable.mobile, "Mobiles"));
        item.add(new StaticRvModel(R.drawable.fashion, "Fashion"));
        item.add(new StaticRvModel(R.drawable.tv, "TVs"));
        item.add(new StaticRvModel(R.drawable.toys, "Toys"));
        staticRCV(item);


        staticRvAdapter.setOnItemClickListener(new StaticRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                fillRCV(position);
            }
        });

        fillRCV(0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchByWord(editText.getText().toString());
            }
        });


        // // // // // // Scanner // / / / / / / // / / / / / / /
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(Home.this, Scanner.class);
                startActivity(i);
                finish();

            }
        });

        Bundle a = getIntent().getExtras();
        if(a!=null){
            String b = a.getString("qrCode");
            editText = (EditText) findViewById(R.id.editText);
            editText.setText(b);
            searchByID(b);
        }
        // // // // // // Scanner // / / / / / / // / / / / / / /

        // // // // // // Voice // / / / / / / // / / / / / / /

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToText();
//                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

            }
        });

        // // // // // // Voice // / / / / / / // / / / / / / /

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Checkout.class));
            }
        });


    }

    private void convertToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say anything to search for");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            //Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null ){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                    searchByWord(result.get(0));
                }
                break;
        }
    }


    private void staticRCV(ArrayList<StaticRvModel> item){
        recyclerView = findViewById(R.id.recyclerView1);
        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(staticRvAdapter);
    }

    private void dynamicRCV(ArrayList<ProductRvModel>items){
        recyclerView = findViewById(R.id.recyclerView2);
        productRvAdapter = new ProductRvAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(productRvAdapter);
    }

    private void productClick(ArrayList<ProductRvModel>items){
        productRvAdapter.setOnItemClickListener(new ProductRvAdapter.OnItemClickListener() {
            @Override
            public void addItemClick(int position) {

                boolean found = false;
                for(int i=0 ; i<StaticVariables.products.size() ; i++){
                    if(StaticVariables.products.get(i).productID == items.get(position).getID()){
                        StaticVariables.products.get(i).quantity++;
                        found = true;
                    }
                }
                if(!found){
                    StaticVariables.products.add(new Product(items.get(position).getID(), 1, Integer.parseInt(items.get(position).getPrice())));
                }

                StaticVariables.total+=Integer.parseInt(items.get(position).getPrice());

            }
        });
    }

    private void fillRCV (int index) {
        String[] categories = {"all", "supermarket", "MobileAndTablet", "fashion", "tv", "toys"};
        ArrayList<ProductRvModel> items = new ArrayList<>();

        if(index == 0){
            databaseReference.child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            items.add(new ProductRvModel(dataSnapshot1.child("image").getValue(String.class)
                                    , dataSnapshot1.child("price").getValue(String.class),
                                    dataSnapshot1.child("proName").getValue(String.class),
                                    String.valueOf(dataSnapshot1.getKey())));
                        }
                    dynamicRCV(items);
                    productClick(items);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            databaseReference.child("products").child(categories[index]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        items.add(new ProductRvModel(dataSnapshot.child("image").getValue(String.class)
                                , dataSnapshot.child("price").getValue(String.class),
                                dataSnapshot.child("proName").getValue(String.class),
                                String.valueOf(dataSnapshot.getKey())));
                    }

                    dynamicRCV(items);
                    productClick(items);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }


    private void searchByWord (String word) {

        ArrayList<ProductRvModel> items = new ArrayList<>();
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                        if(dataSnapshot2.child("proName").getValue(String.class).toLowerCase().indexOf(word.toLowerCase()) != -1){
                            items.add(new ProductRvModel(dataSnapshot2.child("image").getValue(String.class)
                                    , dataSnapshot2.child("price").getValue(String.class),
                                    dataSnapshot2.child("proName").getValue(String.class),
                                    String.valueOf(dataSnapshot2.getKey())));
                        }
                    }
                dynamicRCV(items);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        dynamicRCV(items);
    }


    private void searchByID (String word) {

        ArrayList<ProductRvModel> items = new ArrayList<>();
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        if(dataSnapshot1.getKey().equals(word)){

//                            Toast.makeText(getApplicationContext(), "dataSnapshot1.getKey()", Toast.LENGTH_SHORT).show();

                            items.add(new ProductRvModel(dataSnapshot1.child("image").getValue(String.class)
                                    , dataSnapshot1.child("price").getValue(String.class),
                                    dataSnapshot1.child("proName").getValue(String.class),
                                    String.valueOf(dataSnapshot1.getKey())));
                        }
                    }

                dynamicRCV(items);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        dynamicRCV(items);
    }
}