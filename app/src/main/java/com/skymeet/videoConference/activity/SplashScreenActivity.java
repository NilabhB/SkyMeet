package com.skymeet.videoConference.activity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.skymeet.videoConference.databinding.ActivitySplashScreenBinding;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        YoYo.with(Techniques.BounceInLeft).duration(600).playOn(binding.SkyMeetLogo);
        YoYo.with(Techniques.Shake).duration(600).playOn(binding.welcomeTextView);
        YoYo.with(Techniques.Pulse).duration(600).playOn(binding.jitsiLogo);


        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(650);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
        }; thread.start();
    }
}