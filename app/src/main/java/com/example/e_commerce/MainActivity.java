package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class MainActivity extends AppCompatActivity {

    private EditText mainActivity_email;
    private EditText mainActivity_password;
    private Button btn_signin;
    private TextView mainActivty_register, mainActivty_forgotPassword;
    private CheckBox mainActivity_remember;


    FirebaseAuth firebaseAuth;
    String email, password;

// Amazures
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity_email = (EditText) findViewById(R.id.mainActivity_email);
        mainActivity_password = (EditText) findViewById(R.id.mainActivity_password);
        mainActivty_register = (TextView) findViewById(R.id.mainActivity_register);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        mainActivity_remember = (CheckBox) findViewById(R.id.mainActivity_remember);
        mainActivty_forgotPassword = (TextView) findViewById(R.id.mainActivty_forgotPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        email = sharedpreferences.getString("Email", null);
        password = sharedpreferences.getString("Password", null);

        if(email!=null && password!=null){
            StaticVariables.email=email;
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                        Toast.makeText(MainActivity.this, "Welcome !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mainActivity_email.getText().toString();
                password = mainActivity_password.getText().toString();

                StaticVariables.email=email;

                OrderModel.helperArray.add(email);
                if(!IsEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Welcome !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    if(mainActivity_remember.isChecked()){
                        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Email", email);
                        editor.putString("Password", password);
                        editor.commit();
                    }
                }
            }
        });

        mainActivty_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        mainActivty_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder passwordRestDialog = new AlertDialog.Builder(v.getContext());
                passwordRestDialog.setTitle("Reset Password ?");
                passwordRestDialog.setMessage("Enter Your Email To Get A Reset Password Link ");
                passwordRestDialog.setView(resetEmail);


                passwordRestDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetEmail.getText().toString();

                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordRestDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                passwordRestDialog.create().show();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

    }

    public boolean IsEmpty(){
        if(mainActivity_password.getText().toString().isEmpty() || mainActivity_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}