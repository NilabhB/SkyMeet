package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    EditText emailBox, passwordBox;
    Button signInBtn;
    TextView createAcTextView;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");

        auth = FirebaseAuth.getInstance();

        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        signInBtn = findViewById(R.id.SignInBtn);
        createAcTextView = findViewById(R.id.createAcTextView);



        createAcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String email, password;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    emailBox.setError("Email cannot be empty");
                    emailBox.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passwordBox.setError("Password cannot be empty");
                    passwordBox.requestFocus();
                } else {
                    dialog.show();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()) {
                                // Log.d(TAG, "signInWithEmail:success");
                                // FirebaseUser user = auth.getCurrentUser();
                                // updateUI(user);
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                Toast.makeText(SignInActivity.this, "logged in!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignInActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
    private void reload() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
    }



    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Enter Guest Mode")
                .setMessage("Do you want enter as a Guest?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SignInActivity.this, GuestActivity.class));
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        finish();
                    }
                }).show();

        }
    }
