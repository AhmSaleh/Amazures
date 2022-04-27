package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText et_age;
    private EditText register_name;
    private EditText register_username;
    private EditText register_email;
    private EditText register_password;


    String name, username, email, password, age;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button btn = findViewById(R.id.register_btn);

        et_age = findViewById(R.id.et_age);
        et_age.setInputType(InputType.TYPE_NULL);
        et_age.setTextIsSelectable(true);
        register_name = (EditText) findViewById(R.id.register_name);
        register_username = (EditText) findViewById(R.id.register_username);
        register_email = (EditText) findViewById(R.id.register_email);
        register_password = (EditText) findViewById(R.id.register_password);

        firebaseAuth = FirebaseAuth.getInstance();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = register_name.getText().toString();
                username = register_name.getText().toString();
                email = register_email.getText().toString();
                password = register_password.getText().toString();
                age = et_age.getText().toString();

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                if(!IsEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserHelperClass userHelperClass = new UserHelperClass(name, username, email, password, age);
                                reference.child(username).setValue(userHelperClass);
                                Toast.makeText(Register.this, "User Created!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Register.this, "Error!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });



        //**** Calender
        et_age.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDatePickerDialog();
                return false;
            }
        });

    }
    public void showDatePickerDialog (){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // write the code of calender here to database
    et_age.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
    }
    //**** Calender

    public boolean IsEmpty(){

        if(et_age.getText().toString().isEmpty() || register_name.getText().toString().isEmpty() || register_username.getText().toString().isEmpty() ||
                register_email.getText().toString().isEmpty() || register_password.getText().toString().isEmpty() ){
            Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }


}