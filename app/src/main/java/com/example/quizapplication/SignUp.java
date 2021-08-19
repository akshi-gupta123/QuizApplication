package com.example.quizapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    Button button_signup;
    TextView loginHere;
    TextInputEditText email_signup;
    TextInputEditText password;
    TextInputEditText fullname;
    TextInputEditText university;
    TextInputEditText mobile_no;

    private void createUser() {
        String email = email_signup.getText().toString();
        String pass = password.getText().toString();
        String name = fullname.getText().toString();
        String mobileno = mobile_no.getText().toString();
        String organization = university.getText().toString();


        boolean check = validateinfo(name, email, pass,mobileno, organization);
        if (check == true) {
            Toast.makeText(getApplicationContext(), "Data is valid", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Sorry check your data again", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validateinfo(String name, String email,String pass, String mobileno, String organization) {
        if (name.length() <= 2) {
            fullname.requestFocus();
            fullname.setError("Enter full name");
            return false;
        } else if (TextUtils.isEmpty(email) ) {
            email_signup.requestFocus();
            email_signup.setError("Enter valid Email");
            return false;
//        } else if (!mobileno.matches("^[6-9]{9}$")) {
//            mobile_no.requestFocus();
//            mobile_no.setError("Enter number in correct format");
//            return false;

        } else if (organization.length() == 0) {
            university.requestFocus();
            university.setError("Enter your current university/organization");
            return false;
        } else {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Verification Email has been sent ", Toast.LENGTH_SHORT).show();
                                    saveData();
                                }
                                else {
                                    Log.d(TAG, "Email not sent");
                                }
                            }
                        });
                        Toast.makeText(SignUp.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, Dashboard.class));
                    } else {
                        Toast.makeText(SignUp.this, "Registration Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//status bar remove

        button_signup = findViewById(R.id.button_signup);
        loginHere = findViewById(R.id.loginHere);
        email_signup = findViewById(R.id.email_signup);
        password = findViewById(R.id.password);
        fullname = findViewById(R.id.fullname);
        mobile_no = findViewById(R.id.mobile_no);
        university = findViewById(R.id.university);


        button_signup.setOnClickListener(view -> {
            createUser();
        });
        loginHere.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, Login.class));
        });

    }
    private void saveData(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String uid = user.getUid();
        Map<String, Object> users = new HashMap<>();
        users.put("fullname", fullname);
        users.put("email", email_signup);
        users.put("mobile no", mobile_no);
        users.put("university/organization", university);
        users.put("userId", uid);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        database.child("users").child(uid).updateChildren(users).addOnSuccessListener(DatabaseReference -> Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(this, "Error in Uploading", Toast.LENGTH_SHORT).show());


    }

}