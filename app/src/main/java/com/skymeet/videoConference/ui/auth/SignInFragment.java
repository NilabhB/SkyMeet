package com.skymeet.videoConference.ui.auth;

import static com.skymeet.videoConference.utils.NavUtils.getNavController;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skymeet.videoConference.AuthNavDirections;
import com.skymeet.videoConference.R;
import com.skymeet.videoConference.databinding.FragmentSignInBinding;
import com.skymeet.videoConference.utils.UiUtils;

import java.util.Objects;

public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UiUtils.showAuthExitDialog(SignInFragment.this);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Please wait...");

        auth = FirebaseAuth.getInstance();
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.guestMode);
        YoYo.with(Techniques.FlipInY).duration(1500).repeat(3).playOn(binding.passwordEye);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.skymeetLogo);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.idSkyMeet);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.passwordBox);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.emailBox);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.forgotPasswordTextView);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.createAcTextView);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.SignInBtn);


        binding.createAcTextView.setOnClickListener(v -> {
            view.startAnimation(buttonClick);
            getNavController(this).navigate(
                    SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            );
        });

        binding.forgotPasswordTextView.setOnClickListener(v -> {
            view.startAnimation(buttonClick);
            getNavController(this).navigate(
                    SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
            );
        });

        binding.guestMode.setOnClickListener(v -> {
            view.startAnimation(buttonClick);
            getNavController(this).navigate(
                    AuthNavDirections.actionGlobalGuestNav()
            );
        });

        binding.passwordEye.setOnClickListener(this::showHidePass);


        binding.SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                String email, password;
                email = binding.emailBox.getText().toString().trim();
                password = binding.passwordBox.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    binding.emailBox.setError("Email cannot be empty");
                    binding.emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignInBtn);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailBox.setError("Please provide a valid email!");
                    binding.emailBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignInBtn);
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
                                    new android.app.AlertDialog.Builder(requireContext())
                                            .setIcon(R.drawable.ic_baseline_email_24)
                                            .setTitle("Email Verification Needed")
                                            .setMessage("A link was send to your email previously. Please verify to log in.")
                                            .setPositiveButton("Verify Now", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = requireContext().getPackageManager().
                                                            getLaunchIntentForPackage("com.google.android.gm");
                                                    startActivity(intent);
                                                    Toast.makeText(requireContext(),
                                                            "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                                                }
                                            }).show();
                                } else {
                                    getNavController(SignInFragment.this).navigate(
                                            AuthNavDirections.actionGlobalHomeNav()
                                    );
                                    Toast.makeText(requireContext(), "logged in!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException())
                                        .getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
    }

    public void showHidePass(View view) {
        if (binding.passwordBox.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
            binding.passwordBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_24);
            binding.passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}