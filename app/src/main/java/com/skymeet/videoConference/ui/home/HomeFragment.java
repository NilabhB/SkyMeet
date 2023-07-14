package com.skymeet.videoConference.ui.home;

import static com.skymeet.videoConference.utils.NavUtils.getNavController;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.skymeet.videoConference.HomeNavDirections;
import com.skymeet.videoConference.R;
import com.skymeet.videoConference.databinding.FragmentHomeBinding;
import com.skymeet.videoConference.utils.UiUtils;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private HomeViewModel mViewModel;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UiUtils.showAppExitDialog(requireActivity());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.videoCall);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.facebook);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.linkedin);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.instagram);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.hellotv);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.joinBtn);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.profileInfo);
        YoYo.with(Techniques.Pulse).duration(1200).repeat(3).playOn(binding.followtv);
        YoYo.with(Techniques.Wobble).duration(1200).repeat(0).playOn(binding.shareCode);
        YoYo.with(Techniques.Flash).duration(1200).repeat(0).playOn(binding.logoutText);

        subscribeToLiveData();

        binding.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.codeBox.getText().toString())) {
                    binding.codeBox.setError("Meeting Code cannot be empty");
                    binding.codeBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn(binding.joinBtn);
                } else {
                    JitsiMeetConferenceOptions options
                            = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(binding.codeBox.getText().toString().trim())
                            .setFeatureFlag("welcomepage.enabled", false)
                            .setFeatureFlag("live-streaming.enabled", false)
                            .setFeatureFlag("invite.enabled", false)
                            .setFeatureFlag("video-share.enabled", false)
                            .setFeatureFlag("fullscreen.enabled", true)
                            .build();
                    JitsiMeetActivity.launch(requireContext(), options);
                }
            }
        });

        binding.profileInfo.setOnClickListener(view1 -> {
            view1.startAnimation(buttonClick);
            String userName = null;
            if (mViewModel.user.getValue() != null && mViewModel.user.getValue().getData() != null) {
                userName = mViewModel.user.getValue().getData().getName();
            }
            getNavController(this).navigate(
                    HomeFragmentDirections.actionHomeFragmentToProfileFragment(userName)
            );
        });

        binding.shareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if (TextUtils.isEmpty(binding.codeBox.getText().toString())) {
//                    codeBox.setError("Enter Meeting Code before sharing!");
//                    codeBox.requestFocus();
                    YoYo.with(Techniques.Wave).duration(1200).repeat(0).playOn(binding.shareCode);
                } else {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "To join the meeting on SkyMeet Conference, please use\n\n"
                            + "Code: " + binding.codeBox.getText().toString().trim() + "\n\nDownload SkyMeet:\n\n"
                            + "Android: https://play.google.com/store/apps/details?id=com.skymeet.videoConference\n\n"
                            + "iOS: An apple a day keeps a doctor away but visiting a lady doctor everyday could be your spouse someday.";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share Code via"));
                }

            }
        });

        binding.videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Wave).duration(1200).repeat(0).playOn(binding.videoCall);
            }
        });

        binding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
//                Profile/Page opens up on chrome, not on the app
                String YourPageURL = "https://www.facebook.com/skymeet.conference/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });

        binding.linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://www.linkedin.com/company/skymeet.conference/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);

            }
        });

        binding.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://www.instagram.com/skymeet.conference/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);

            }
        });

        binding.logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                new AlertDialog.Builder(requireContext())
                        .setIcon(R.drawable.ic_baseline_logout_24)
                        .setTitle("Log Out")
                        .setMessage("Do you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewModel.signOut();
                                getNavController(HomeFragment.this).navigate(
                                        HomeNavDirections.actionGlobalAuthNav()
                                );
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
        });
    }

    private void subscribeToLiveData() {
        mViewModel.user.observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            switch (result.getState()) {

                case LOADING -> {
                }
                case SUCCESS -> {
                    assert result.getData() != null;
                    binding.welcomeUser.setText(result.getData().getName());
                    YoYo.with(Techniques.FadeInLeft).duration(700).repeat(0).playOn(binding.welcomeUser);
                }
                case ERROR -> Toast.makeText(
                        requireContext(),
                        "Failed to fetch data",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}