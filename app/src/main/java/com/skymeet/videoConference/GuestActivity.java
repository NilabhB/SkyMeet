package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class GuestActivity extends AppCompatActivity {

    EditText codeBox;
    Button joinBtn;
    TextView backToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        getSupportActionBar().setTitle("Guest Mode");
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        codeBox = findViewById(R.id.codeBox);
        joinBtn = findViewById(R.id.joinBtn);
        backToSignIn = findViewById(R.id.backToSignIn);

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
                .setFeatureFlag("invite.enabled",false)
                .build();

        JitsiMeet.setDefaultConferenceOptions(defaultOptions);




        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (TextUtils.isEmpty(codeBox.getText().toString())) {
                    codeBox.setError("Meeting Code cannot be empty");
                    codeBox.requestFocus();
                } else {
                    JitsiMeetConferenceOptions options
                            = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(codeBox.getText().toString())
                            .setFeatureFlag("welcomepage.enabled", false)
                            .setFeatureFlag("invite.enabled",false)
                            .setFeatureFlag("fullscreen.enabled", true)
                            .build();
                    JitsiMeetActivity.launch(GuestActivity.this, options);
                }
            }
        });

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(GuestActivity.this, SignInActivity.class));
            }
        });
    }

    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

}