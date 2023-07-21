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
import com.skymeet.videoConference.data.model.UserSignInRequest;
import com.skymeet.videoConference.data.utils.AuthError;
import com.skymeet.videoConference.databinding.FragmentSignInBinding;
import com.skymeet.videoConference.utils.UiUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    ProgressDialog dialog;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    private AuthViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
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

        subscribeToLiveData();

        binding.SignInBtn.setOnClickListener(v -> {
            var request = getUserSignInRequest();
            if (request != null)
                viewModel.signIn(request);
        });
    }

    private void showHidePass(View view) {
        if (binding.passwordBox.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
            binding.passwordBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_24);
            binding.passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private void subscribeToLiveData() {
        viewModel.userSignInState.observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            switch (result.getState()) {
                case LOADING -> dialog.show();
                case SUCCESS -> {
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "logged in!", Toast.LENGTH_SHORT).show();
                    getNavController(SignInFragment.this).navigate(
                            AuthNavDirections.actionGlobalHomeNav()
                    );
                }
                case ERROR -> {
                    dialog.dismiss();
                    if (result.getError() instanceof AuthError.UserNotVerifiedError) {
                        viewModel.sendVerificationEmail();
                        showUserVerificationDialog();
                    } else {
                        String msg = "Something went wrong! Please try again";
                        if (result.getError() != null)
                            msg = result.getError().getLocalizedMessage();
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Nullable
    private UserSignInRequest getUserSignInRequest() {
        var request = new UserSignInRequest(
                binding.emailBox.getText().toString().trim(),
                binding.passwordBox.getText().toString().trim()
        );

        if (TextUtils.isEmpty(request.getEmail())) {
            binding.emailBox.setError("Email cannot be empty");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignInBtn);
            return null;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(request.getEmail()).matches()) {
            binding.emailBox.setError("Please provide a valid email!");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignInBtn);
            return null;
        } else if (TextUtils.isEmpty(request.getPassword())) {
            binding.passwordBox.setError("Password cannot be empty!");
            binding.passwordBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignInBtn);
            return null;
        } else if (request.getPassword().length() < 6) {
            binding.passwordBox.setError("Min password length should be 6 characters!");
            binding.passwordBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.SignInBtn);
            return null;
        }
        return request;
    }

    private void showUserVerificationDialog() {
        new android.app.AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_email_24)
                .setTitle("Email Verification Needed")
                .setMessage("A link was send to your email previously. Please verify to log in.")
                .setPositiveButton("Verify Now", (dialog, which) -> {
                    Intent intent = requireContext().getPackageManager().
                            getLaunchIntentForPackage("com.google.android.gm");
                    startActivity(intent);
                    Toast.makeText(requireContext(),
                            "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                }).show();
    }
}