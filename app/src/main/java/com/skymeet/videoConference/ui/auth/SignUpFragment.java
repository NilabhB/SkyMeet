package com.skymeet.videoConference.ui.auth;

import static com.skymeet.videoConference.utils.NavUtils.getNavController;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.skymeet.videoConference.AuthNavDirections;
import com.skymeet.videoConference.R;
import com.skymeet.videoConference.data.model.UserSignUpRequest;
import com.skymeet.videoConference.databinding.FragmentSignUpBinding;
import com.skymeet.videoConference.utils.UiUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    ProgressDialog dialog;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    private AuthViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Creating account...");

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UiUtils.showAuthExitDialog(SignUpFragment.this);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        YoYo.with(Techniques.Shake).duration(1500).repeat(0).playOn(binding.guestMode);
        YoYo.with(Techniques.FlipInY).duration(1500).repeat(3).playOn(binding.passwordEye);
        YoYo.with(Techniques.FlipInX).duration(1500).repeat(3).playOn(binding.passwordEye2);
        YoYo.with(Techniques.RotateInUpLeft).duration(1500).repeat(0).playOn(binding.skymeetLogo);
        YoYo.with(Techniques.Wobble).duration(1500).repeat(0).playOn(binding.idSkyMeet);

        binding.passwordEye.setOnClickListener(this::showHidePass);
        binding.passwordEye2.setOnClickListener(this::showHidePass2);

        binding.guestMode.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            getNavController(this).navigate(
                    AuthNavDirections.actionGlobalGuestNav()
            );
        });

        binding.signInAcTextView.setOnClickListener(view1 -> {
            view1.startAnimation(buttonClick);
            getNavController(this).navigate(
                    SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            );
        });

        binding.SignUpBtn.setOnClickListener(v -> {
            var userRequest = getUserRequest();
            if (userRequest != null)
                viewModel.signUp(userRequest);
        });

        subscribeToLiveData();
    }

    public void togglePasswordVisibility(View view, @NonNull EditText passwordBox) {
        if (passwordBox.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
            passwordBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_24);
            passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private void showHidePass(View view) {
        togglePasswordVisibility(view, binding.passwordBox);
    }

    private void showHidePass2(View view) {
        togglePasswordVisibility(view, binding.passwordBox2);
    }

    /**
     * @return Returns null if user data is invalid else UserSignUpRequest
     */
    @Nullable
    private UserSignUpRequest getUserRequest() {
        var request = new UserSignUpRequest(
                binding.userName.getText().toString().trim(),
                binding.emailBox.getText().toString().trim(),
                binding.passwordBox.getText().toString().trim()
        );

        var passwordVerify = binding.passwordBox2.getText().toString().trim();

        if (TextUtils.isEmpty(request.getName())) {
            binding.userName.setError("Please enter your name!");
            binding.userName.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
            return null;
        } else if (TextUtils.isEmpty(request.getEmail())) {
            binding.emailBox.setError("Email cannot be empty!");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
            return null;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(request.getEmail()).matches()) {
            binding.emailBox.setError("Please provide a valid email!");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
            return null;
        } else if (TextUtils.isEmpty(request.getPassword())) {
            binding.passwordBox.setError("Password cannot be empty!");
            binding.passwordBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
            return null;
        } else if (request.getPassword().length() < 6) {
            binding.passwordBox.setError("Min password length should be 6 characters!");
            binding.passwordBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
            return null;
        } else if (!request.getPassword().equals(passwordVerify)) {
            YoYo.with(Techniques.Wobble).duration(1000).repeat(0).playOn(binding.passwordEye);
            YoYo.with(Techniques.FlipInY).duration(1500).repeat(2).playOn(binding.passwordEye);
            YoYo.with(Techniques.Hinge).duration(1000).repeat(0).playOn(binding.passwordEye2);
            binding.passwordBox2.setError("Password didn't match!");
            binding.passwordBox2.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignUpBtn);
            return null;
        }
        return request;
    }

    private void subscribeToLiveData() {
        viewModel.userSignUpState.observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            switch (result.getState()) {
                case LOADING -> dialog.show();
                case SUCCESS -> {
                    dialog.dismiss();
                    showUserVerificationEmailSentDialog();
                }
                case ERROR -> {
                    dialog.dismiss();
                    String msg;
                    if (result.getError() == null)
                        msg = "Something went wrong, Please try again";
                    else msg = result.getError().getLocalizedMessage();
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showUserVerificationEmailSentDialog() {
        new android.app.AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_email_24)
                .setTitle("Verify your Email")
                .setMessage("A link has been sent to your email. Please verify now before logging in")
                .setNeutralButton("Later", (dialog, which) ->
                        getNavController(SignUpFragment.this).navigate(
                                SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                        )
                )
                .setPositiveButton("Verify Now", (dialog, which) -> {
                    Intent intent = requireContext().getPackageManager().
                            getLaunchIntentForPackage("com.google.android.gm");
                    startActivity(intent);
                    Toast.makeText(requireContext(),
                            "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                })
                .show();
        Toast.makeText(requireContext(), "Account Created.", Toast.LENGTH_SHORT).show();
    }

}