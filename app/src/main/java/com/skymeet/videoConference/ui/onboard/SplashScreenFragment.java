package com.skymeet.videoConference.ui.onboard;

import static com.skymeet.videoConference.utils.NavUtils.getNavController;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.skymeet.videoConference.databinding.FragmentSplashScreenBinding;

public class SplashScreenFragment extends Fragment {
    private FragmentSplashScreenBinding binding;
    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        binding = FragmentSplashScreenBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        YoYo.with(Techniques.BounceInLeft).duration(600).playOn(binding.SkyMeetLogo);
        YoYo.with(Techniques.Shake).duration(600).playOn(binding.welcomeTextView);
        YoYo.with(Techniques.Pulse).duration(600).playOn(binding.jitsiLogo);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (mFirebaseAuth.getCurrentUser() == null) {
                getNavController(this).navigate(
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToAuthNav()
                );
            } else {
                getNavController(this).navigate(
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeNav()
                );
            }
        }, 650);

    }
}
