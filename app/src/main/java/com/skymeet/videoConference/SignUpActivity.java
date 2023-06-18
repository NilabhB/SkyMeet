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
import com.skymeet.videoConference.databinding.ActivitySignUpBinding;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;

    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating account...");

        auth= FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        YoYo.with(Techniques.Shake).duration(1500).repeat(0).playOn( binding.guestMode);
        YoYo.with(Techniques.FlipInY).duration(1500).repeat(3).playOn( binding.passwordEye);
        YoYo.with(Techniques.FlipInX).duration(1500).repeat(3).playOn( binding.passwordEye2);
        YoYo.with(Techniques.RotateInUpLeft).duration(1500).repeat(0).playOn( binding.skymeetLogo);
        YoYo.with(Techniques.Wobble).duration(1500).repeat(0).playOn( binding.idSkyMeet);


        binding.guestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(SignUpActivity.this, GuestActivity.class));
            }
        });

        binding.signInAcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        binding.SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String email, password, passwordVerify, name;
                email =  binding.emailBox.getText().toString().trim();
                password =  binding.passwordBox.getText().toString().trim();
                passwordVerify =  binding.passwordBox2.getText().toString().trim();
                name =  binding.userName.getText().toString().trim();

                User user = new User();
                user.setEmail(email);
                user.setName(name);


                if (TextUtils.isEmpty(name)) {
                    binding.userName.setError("Please enter your name!");
                    binding.userName.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
                } else if (TextUtils.isEmpty(email)) {
                    binding.emailBox.setError("Email cannot be empty!");
                    binding.emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailBox.setError("Please provide a valid email!");
                    binding.emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
                } else if (TextUtils.isEmpty(password)) {
                    binding.passwordBox.setError("Password cannot be empty!");
                    binding.passwordBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
                } else if (password.length() < 6) {
                    binding.passwordBox.setError("Min password length should be 6 characters!");
                    binding.passwordBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
                } else if (!password.equals(passwordVerify)) {
                    YoYo.with(Techniques.Wobble).duration(1000).repeat(0).playOn(binding.passwordEye);
                    YoYo.with(Techniques.FlipInY).duration(1500).repeat(2).playOn(binding.passwordEye);
                    YoYo.with(Techniques.Hinge).duration(1000).repeat(0).playOn(binding.passwordEye2);
                    binding.passwordBox2.setError("Password didn't match!");
                    binding.passwordBox2.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
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

    public void togglePasswordVisibility(View view, EditText passwordBox) {
        if (passwordBox.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
            passwordBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_24);
            passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void ShowHidePass(View view) {
        togglePasswordVisibility(view, binding.passwordBox);
    }

    public void ShowHidePass2(View view) {
        togglePasswordVisibility(view, binding.passwordBox2);
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