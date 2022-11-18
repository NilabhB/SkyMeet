package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.os.Bundle;
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

    TextView welcomeUser;
    FirebaseFirestore database;
    DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        welcomeUser = findViewById(R.id.welcomeUserInfo);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseFirestore.getInstance();
        assert firebaseUser != null;
        reference = database.collection("Users").document(firebaseUser.getUid());
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            welcomeUser.setText(documentSnapshot.getString("name") + ",");
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
    }
}