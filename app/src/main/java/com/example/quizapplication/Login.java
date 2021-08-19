package com.example.quizapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    Button button_login;
    TextView signupHere;
    TextInputEditText email_login;
    TextInputEditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//status bar remove

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        button_login=findViewById(R.id.button_login);
        signupHere=findViewById(R.id.signupHere);
        email_login=findViewById(R.id.email_login);
        pass=findViewById(R.id.pass);

        boolean emailVerified = user.isEmailVerified();
        button_login.setOnClickListener(view->{
            if(!emailVerified) {
                Toast.makeText(Login.this,"Verify your email",Toast.LENGTH_SHORT).show();
            }
            else{
                loginUser();
            }
        });
        signupHere.setOnClickListener(view->{
            startActivity(new Intent(Login.this,SignUp.class));
        });
    }

    private void loginUser(){
        String email = email_login.getText().toString();
        String password = pass.getText().toString();

        if(TextUtils.isEmpty(email)){
            email_login.setError("Email cannot be empty");
            email_login.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            pass.setError("Password cannot be empty");
            pass.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this,"User Login Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this,Dashboard.class));
                    }else{
                        Toast.makeText(Login.this,"Registration Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
