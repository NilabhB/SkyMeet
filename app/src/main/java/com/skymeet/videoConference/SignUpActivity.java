package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore database;
    DocumentReference reference;

    TextView signInAcTextView;
    EditText emailBox, passwordBox, userName;
    Button signUpBtn;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating account...");

        auth= FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        signInAcTextView = findViewById(R.id.signInAcTextView);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        userName = findViewById(R.id.userName);
        signUpBtn = findViewById(R.id.SignUpBtn);

        signInAcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String email, password, name;
                email = emailBox.getText().toString().trim();
                password = passwordBox.getText().toString().trim();
                name = userName.getText().toString().trim();

                User user = new User();
                user.setEmail(email);
                user.setName(name);


                if (TextUtils.isEmpty(name)) {
                    userName.setError("Please enter your name!");
                    userName.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    emailBox.setError("Email cannot be empty!");
                    emailBox.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailBox.setError("Please provide a valid email!");
                    emailBox.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passwordBox.setError("Password cannot be empty!");
                    passwordBox.requestFocus();
                } else if (password.length() < 6) {
                    passwordBox.setError("Min password length should be 6 characters!");
                    passwordBox.requestFocus();
                } else {
                    dialog.show();
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()) {
                               final FirebaseUser firebaseUser = auth.getCurrentUser();
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(SignUpActivity.this, "Email Verification Link Send", Toast.LENGTH_SHORT).show();
                                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                database.collection("Users").document(uid).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        new android.app.AlertDialog.Builder(SignUpActivity.this)
                                                .setTitle("Verify your Email")
                                                .setMessage("A link has been send to your email. Please verify now before logging in")
                                                .setNeutralButton("Later", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                                    }
                                                })
                                                .setPositiveButton("Verify Now", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                       // FirebaseAuth.getInstance().signOut();
                                                        Intent intent = getPackageManager().
                                                                getLaunchIntentForPackage("com.google.android.gm");
                                                        startActivity(intent);
                                                        Toast.makeText(SignUpActivity.this,
                                                                "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .show();
                                    }
                                });
                                Toast.makeText(SignUpActivity.this, "Account Created.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this,
                                        Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


            }
        });

    }
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Guest Mode")
                .setMessage("Do you want enter as a Guest?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SignUpActivity.this, GuestActivity.class));
                    }
                })
                .setNeutralButton("Exit App", new DialogInterface.OnClickListener() {
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
                })
                .show();

    }
}