package com.skymeet.videoConference;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.skymeet.videoConference.databinding.ActivityGuestInfoBinding;

import java.util.Objects;

public class GuestInfoActivity extends AppCompatActivity {

    ActivityGuestInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGuestInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        YoYo.with(Techniques.FlipInX).duration(1500).repeat(0).playOn( binding.upiID);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn( binding.facebook);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn( binding.linkedin);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn( binding.instagram);
        YoYo.with(Techniques.FlipInX).duration(1200).repeat(3).playOn( binding.github);
        YoYo.with(Techniques.Pulse).duration(1200).repeat(100).playOn( binding.coffeCup);

        binding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
//                Profile/Page opens up on chrome, not on the app
                String YourPageURL = "https://www.facebook.com/nilabhnayan.borthakur/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });

        binding.linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://www.linkedin.com/in/nilabh-borthakur/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);

            }
        });

        binding.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://www.instagram.com/nilabh_borthakur/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);

            }
        });

        binding.github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://github.com/NilabhB";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });

        binding.gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent intent = getPackageManager().
                        getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user");
                startActivity(intent);
            }
        });

        binding.shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey,\n" +"I urge you to check this new amazing conferencing app, SkyMeet\n\n"
                        + "Android: https://play.google.com/store/apps/details?id=com.skymeet.videoConference\n\n"
                        + "iOS: An apple a day keeps a doctor away but visiting a lady doctor everyday could be your spouse someday.";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share App via"));


            }
        });

        binding.rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String YourPageURL = "https://play.google.com/store/apps/details?id=com.skymeet.videoConference";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });
    }

    public void onCopyClick(View view) {
        view.startAnimation(buttonClick);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label",  binding.upiID.getText().toString().trim());
        if (clipboard == null || clip == null) return;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "UPI ID copied!", Toast.LENGTH_SHORT).show();
    }


    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
}