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
import com.skymeet.videoConference.data.repository.UserRepository;
import com.skymeet.videoConference.databinding.FragmentSplashScreenBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashScreenFragment extends Fragment {

    @Inject
    public UserRepository userRepository;
    private FragmentSplashScreenBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            if (!userRepository.isUserSignedIn()) {
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
