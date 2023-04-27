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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skymeet.videoConference.databinding.ActivitySignInBinding;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressDialog dialog;
   ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");

        auth = FirebaseAuth.getInstance();
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.guestMode);
        YoYo.with(Techniques.FlipInY).duration(1500).repeat(3).playOn( binding.passwordEye);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.skymeetLogo);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.idSkyMeet);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.passwordBox);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.emailBox);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.forgotPasswordTextView);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.createAcTextView);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn( binding.SignInBtn);



        binding.createAcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        binding.forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
            }
        });

        binding.guestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(SignInActivity.this, GuestActivity.class));
            }
        });


        binding.SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                String email, password;
                email =  binding.emailBox.getText().toString().trim();
                password =  binding.passwordBox.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    binding.emailBox.setError("Email cannot be empty");
                    binding.emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn( binding.SignInBtn);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailBox.setError("Please provide a valid email!");
                    binding.emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn( binding.SignInBtn);
                } else if (TextUtils.isEmpty(password)) {
                    binding.passwordBox.setError("Password cannot be empty!");
                    binding.passwordBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn( binding.SignInBtn);
                } else if (password.length() < 6) {
                    binding.passwordBox.setError("Min password length should be 6 characters!");
                    binding.passwordBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn( binding.SignInBtn);
                } else {
                    dialog.show();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                if (!firebaseUser.isEmailVerified()) {
                                    firebaseUser.sendEmailVerification();
                                    new android.app.AlertDialog.Builder(SignInActivity.this)
                                            .setIcon(R.drawable.ic_baseline_email_24)
                                            .setTitle("Email Verification Needed")
                                            .setMessage("A link was send to your email previously. Please verify to log in.")
                                            .setPositiveButton("Verify Now", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = getPackageManager().
                                                            getLaunchIntentForPackage("com.google.android.gm");
                                                    startActivity(intent);
                                                    Toast.makeText(SignInActivity.this,
                                                            "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                                                }
                                            }).show();
                                } else {
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    Toast.makeText(SignInActivity.this, "logged in!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignInActivity.this, Objects.requireNonNull(task.getException())
                                        .getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        if(user.isEmailVerified()) {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }
    }

    public void ShowHidePass(View view) {
        if( binding.passwordBox.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView)(view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
            binding.passwordBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView)(view)).setImageResource(R.drawable.ic_baseline_visibility_24);
            binding.passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
                        startActivity(new Intent(SignInActivity.this, GuestActivity.class));
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
