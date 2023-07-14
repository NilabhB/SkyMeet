package com.skymeet.videoConference.ui.guest;

import static com.skymeet.videoConference.utils.NavUtils.getNavController;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.skymeet.videoConference.GuestNavDirections;
import com.skymeet.videoConference.databinding.FragmentGuestBinding;
import com.skymeet.videoConference.utils.UiUtils;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GuestFragment extends Fragment {
    private FragmentGuestBinding binding;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Inject
    public JitsiMeetConferenceOptions.Builder jitsiMeetOptionsBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        binding = FragmentGuestBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.videoCall);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.guestModeBigView);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.facebook);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.linkedin);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.instagram);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn(binding.guestprofileAppInfo);
        YoYo.with(Techniques.Pulse).duration(1200).repeat(3).playOn(binding.followtv);
        YoYo.with(Techniques.Landing).duration(1200).repeat(0).playOn(binding.joinBtn);
        YoYo.with(Techniques.Wobble).duration(1200).repeat(0).playOn(binding.shareCode);
        YoYo.with(Techniques.Wobble).duration(1200).repeat(0).playOn(binding.backToSignIn);

        binding.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (TextUtils.isEmpty( binding.codeBox.getText().toString())) {
                    binding.codeBox.setError("Meeting Code cannot be empty");
                    binding.codeBox.requestFocus();
                    YoYo.with(Techniques.Shake).duration(1200).repeat(0).playOn( binding.joinBtn);
                } else {
                    var options = jitsiMeetOptionsBuilder
                            .setRoom(binding.codeBox.getText().toString().trim())
                            .build();
                    JitsiMeetActivity.launch(requireContext(), options);
                }
            }
        });

        binding.guestprofileAppInfo.setOnClickListener(view12 -> {
            view12.startAnimation(buttonClick);
            getNavController(GuestFragment.this).navigate(
                    GuestFragmentDirections.actionGuestFragmentToGuestInfoFragment()
            );
        });

        binding.backToSignIn.setOnClickListener(view1 -> {
            view1.startAnimation(buttonClick);
            getNavController(this).navigate(
                    GuestNavDirections.actionGlobalAuthNav2()
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
                YoYo.with(Techniques.Wave).duration(1200).repeat(0).playOn( binding.videoCall);
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
    }
}
