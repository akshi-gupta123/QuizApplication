package com.example.quizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText fullname,email_signup,mobile_no,university;
    ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String mobile = data.getStringExtra("mobile no");

        fullname = findViewById(R.id.fullname);
        email_signup = findViewById(R.id.email_signup);
        mobile_no = findViewById(R.id.mobile_no);
        image_view = findViewById(R.id.image_view);

        fullname.setText(fullName);
        email_signup.setText(email);
        mobile_no.setText(mobile);
        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, "Profile Image Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG,"onCreate"+fullName+" "+email+" "+mobile);

    }
}