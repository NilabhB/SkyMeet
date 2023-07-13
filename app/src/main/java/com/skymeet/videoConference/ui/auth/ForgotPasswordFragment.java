package com.skymeet.videoConference.ui.auth;

import android.content.DialogInterface;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.skymeet.videoConference.R;
import com.skymeet.videoConference.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        binding = FragmentForgotPasswordBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        YoYo.with(Techniques.Swing).duration(1200).repeat(0).playOn(binding.skymeetLogo);
        YoYo.with(Techniques.Hinge).duration(3000).repeat(0).playOn(binding.idSkyMeet);

        binding.resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = binding.emailBox.getText().toString().trim();

        if (email.isEmpty()) {
            binding.emailBox.setError("Email is required");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.resetPasswordBtn);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailBox.setError("Please provide a valid email!");
            binding.emailBox.requestFocus();
            YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.resetPasswordBtn);
        } else {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        new AlertDialog.Builder(requireContext())
                                .setIcon(R.drawable.ic_baseline_email_24)
                                .setTitle("Check your Email")
                                .setMessage("A Link has been send to your email for resetting the password." +
                                        " Kindly reset it & revert back to Sign-In Screen.")
                                .setPositiveButton("Reset Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = requireContext().getPackageManager().
                                                getLaunchIntentForPackage("com.google.android.gm");
                                        startActivity(intent);
                                        Toast.makeText(requireContext(),
                                                "Check in Spam Folder if not found", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                        Toast.makeText(requireContext(), "Check your email to reset your password", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

}