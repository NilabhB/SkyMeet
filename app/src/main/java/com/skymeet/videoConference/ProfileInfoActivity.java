package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileInfoActivity extends AppCompatActivity {

    TextView welcomeUser, upiId, shareApp, rateApp;
    FirebaseFirestore database;
    DocumentReference reference;
    ImageView facebook, instagram,linkedin, github, gpay, copytxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        welcomeUser = findViewById(R.id.welcomeUserInfo);
        facebook = findViewById(R.id.facebook);
        linkedin = findViewById(R.id.linkedin);
        instagram = findViewById(R.id.instagram);
        gpay = findViewById(R.id.gpay);
        copytxt = findViewById(R.id.copytxt);
        github = findViewById(R.id.github);
        upiId = findViewById(R.id.upiID);
        shareApp = findViewById(R.id.shareApp);
        rateApp = findViewById(R.id.rateNow);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseFirestore.getInstance();
        assert firebaseUser != null;
        reference = database.collection("Users").document(firebaseUser.getUid());
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            welcomeUser.setText("Hi "+ documentSnapshot.getString("name") + ",");
                        } else {
                            Toast.makeText(ProfileInfoActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileInfoActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
//                Profile/Page opens up on chrome, not on the app
                String YourPageURL = "https://www.facebook.com/nilabhnayan.borthakur/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://www.linkedin.com/in/nilabh-borthakur/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);

            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://www.instagram.com/nilabh_borthakur/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);

            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://github.com/NilabhB";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });

        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent intent = getPackageManager().
                        getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user");
                startActivity(intent);
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Hey,\n" +"I urge you to check this new amazing conferencing app, SkyMeet\n\n"
                            + "Android: https://play.google.com/store/apps/details?id=com.skymeet.videoConference\n\n"
                            + "iOS: An apple a day keeps a doctor away but visiting a lady doctor everyday could be your spouse someday.";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share Code via"));


            }
        });

        rateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://play.google.com/store/apps/details?id=com.skymeet.videoConference";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });
    }

    public void onCopyClick(View view) {
        view.startAnimation(buttonClick);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", upiId.getText().toString().trim());
        if (clipboard == null || clip == null) return;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "UPI ID copied!", Toast.LENGTH_SHORT).show();
    }


    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
}