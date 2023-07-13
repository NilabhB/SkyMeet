package com.skymeet.videoConference.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.skymeet.videoConference.R;
import com.skymeet.videoConference.databinding.FragmentForgotPasswordBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;
    private AuthViewModel viewModel;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding = FragmentForgotPasswordBinding.inflate(
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

        YoYo.with(Techniques.Swing).duration(1200).repeat(0).playOn(binding.skymeetLogo);
        YoYo.with(Techniques.Hinge).duration(3000).repeat(0).playOn(binding.idSkyMeet);

        binding.resetPasswordBtn.setOnClickListener(v -> {
            var email = getEmail();
            if (email != null)
                viewModel.sendPasswordResetEmail(email);
        });

        subscribeToLiveData();
    }

    private void subscribeToLiveData() {
        viewModel.passwordResetEmailSendState.observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            switch (result.getState()) {

                case LOADING -> dialog.show();
                case SUCCESS -> {
                    dialog.dismiss();
                    showEmailSentSuccessDialog();
                }
                case ERROR -> {
                    dialog.dismiss();
                    String msg = "Try again";
                    if (result.getError() != null)
                        msg = result.getError().getLocalizedMessage();
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showEmailSentSuccessDialog() {
        new AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_email_24)
                .setTitle("Check your Email")
                .setMessage("A Link has been send to your email for resetting the password." +
                        " Kindly reset it & revert back to Sign-In Screen.")
                .setPositiveButton("Reset Now", (dialog, which) -> {
                    Intent intent = requireContext().getPackageManager().
                            getLaunchIntentForPackage("com.google.android.gm");
                    startActivity(intent);
                    Toast.makeText(requireContext(),
                            "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                }).show();
    }

    @Nullable
    private String getEmail() {
        String email = binding.emailBox.getText().toString().trim();
        if (email.isEmpty()) {
            binding.emailBox.setError("Email is required");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.resetPasswordBtn);
            return null;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailBox.setError("Please provide a valid email!");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.resetPasswordBtn);
            return null;
        }
        return email;
    }

}