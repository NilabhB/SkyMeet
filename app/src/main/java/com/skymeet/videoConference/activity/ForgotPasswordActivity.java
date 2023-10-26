package com.skymeet.videoConference.activity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.skymeet.videoConference.R;
import com.skymeet.videoConference.databinding.ActivityForgotPasswordBinding;
import com.skymeet.videoConference.databinding.ActivityMainBinding;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);


        YoYo.with(Techniques.Swing).duration(1200).repeat(0).playOn(binding.skymeetLogo);
        YoYo.with(Techniques.Hinge).duration(3000).repeat(0).playOn(binding.idSkyMeet);


        auth = FirebaseAuth.getInstance();



        binding.resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = binding.emailBox.getText().toString().trim();

        if (email.isEmpty()){
            binding.emailBox.setError("Email is required");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.resetPasswordBtn);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailBox.setError("Please provide a valid email!");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn( binding.resetPasswordBtn);
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