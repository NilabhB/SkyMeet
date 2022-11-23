package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView skyMeetLogo, jitsiLogo;
    TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        skyMeetLogo = findViewById(R.id.SkyMeetLogo);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        jitsiLogo = findViewById(R.id.jitsiLogo);

        YoYo.with(Techniques.BounceInLeft).duration(600).playOn(skyMeetLogo);
        YoYo.with(Techniques.Shake).duration(600).playOn(welcomeTextView);
        YoYo.with(Techniques.Pulse).duration(600).playOn(jitsiLogo);


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