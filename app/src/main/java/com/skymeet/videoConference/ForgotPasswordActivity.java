package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button resetPasswordBtn;
    EditText emailBox;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Password Recovery");
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        emailBox = findViewById(R.id.emailBox);

        auth = FirebaseAuth.getInstance();



        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = emailBox.getText().toString().trim();

        if (email.isEmpty()){
            emailBox.setError("Email is required");
            emailBox.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailBox.setError("Please provide a valid email!");
            emailBox.requestFocus();
        } else {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setIcon(R.drawable.ic_baseline_email_24)
                                .setTitle("Check your Email")
                                .setMessage("A Link has been send to your email for resetting the password." +
                                        " Kindly reset it & revert back to Sign-In Screen.")
                                .setPositiveButton("Reset Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = getPackageManager().
                                                getLaunchIntentForPackage("com.google.android.gm");
                                        startActivity(intent);
                                        Toast.makeText(ForgotPasswordActivity.this,
                                                "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                        Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}