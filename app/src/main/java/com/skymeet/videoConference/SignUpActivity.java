package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore database;

    TextView signInAcTextView, idSkyMeet;
    EditText emailBox, passwordBox, userName;
    Button signUpBtn;
    ProgressDialog dialog;
    ImageView guestMode, passwordEye, skymeetLogo;

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
        guestMode = findViewById(R.id.guestMode);
        passwordEye = findViewById(R.id.passwordEye);
        skymeetLogo = findViewById(R.id.skymeetLogo);
        idSkyMeet = findViewById(R.id.idSkyMeet);


        YoYo.with(Techniques.Shake).duration(1500).repeat(0).playOn(guestMode);
        YoYo.with(Techniques.StandUp).duration(1500).repeat(3).playOn(passwordEye);
        YoYo.with(Techniques.RotateInUpLeft).duration(1500).repeat(0).playOn(skymeetLogo);
        YoYo.with(Techniques.Wobble).duration(1500).repeat(0).playOn(idSkyMeet);


        guestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(SignUpActivity.this, GuestActivity.class));
            }
        });

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
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(signUpBtn);
                } else if (TextUtils.isEmpty(email)) {
                    emailBox.setError("Email cannot be empty!");
                    emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(signUpBtn);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailBox.setError("Please provide a valid email!");
                    emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(signUpBtn);
                } else if (TextUtils.isEmpty(password)) {
                    passwordBox.setError("Password cannot be empty!");
                    passwordBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(signUpBtn);
                } else if (password.length() < 6) {
                    passwordBox.setError("Min password length should be 6 characters!");
                    passwordBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(signUpBtn);
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
                                String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                user.setUserId(userId);
                                database.collection("Users").document(userId).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        new android.app.AlertDialog.Builder(SignUpActivity.this)
                                                .setIcon(R.drawable.ic_baseline_email_24)
                                                .setTitle("Verify your Email")
                                                .setMessage("A link has been sent to your email. Please verify now before logging in")
                                                .setNeutralButton("Later", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                                    }
                                                })
                                                .setPositiveButton("Verify Now", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
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

    public void ShowHidePass(View view) {
        if(passwordBox.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView)(view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
            passwordBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView)(view)).setImageResource(R.drawable.ic_baseline_visibility_24);
            passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
    
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_guest_30)
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