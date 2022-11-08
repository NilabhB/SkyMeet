package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText codeBox;
    Button joinBtn, shareBtn;
    TextView welcomeUser, logoutText;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        codeBox = findViewById(R.id.codeBox);
        joinBtn = findViewById(R.id.joinBtn);
        //shareBtn = findViewById(R.id.shareBtn);
        welcomeUser = findViewById(R.id.welcomeUser); // not implemented yet
        logoutText = findViewById(R.id.logoutText);

       // welcomeUser.getText()

        URL serverURL = null;
        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setFeatureFlag("welcomepage.enabled", false)
                .build();

        JitsiMeet.setDefaultConferenceOptions(defaultOptions);




        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(codeBox.getText().toString())) {
                    codeBox.setError("Meeting Code cannot be empty");
                    codeBox.requestFocus();
                } else {
                    JitsiMeetConferenceOptions options
                            = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(codeBox.getText().toString())
                            .setFeatureFlag("welcomepage.enabled", false)
                            .setFeatureFlag("invite.enabled",false)
                            .build();
                    JitsiMeetActivity.launch(MainActivity.this, options);
                }
            }
        });

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Do you want to exit?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        finish();
                    }
                })
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

    }
}