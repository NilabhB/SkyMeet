package com.skymeet.videoConference;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button resetPassword;
    EditText emailBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Password Recovery");

        resetPassword = findViewById(R.id.resetPasswordBtn);
        emailBox = findViewById(R.id.emailBox);


        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ForgotPasswordActivity.this)
                        .setTitle("Check your Email")
                        .setMessage("A Link has been send to your email for resetting the password." +
                                " Kindly reset it & revert back to Sign-In Screen.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ForgotPasswordActivity.this, SignInActivity.class));
                            }
                        }).show();
            }
        });
    }
}