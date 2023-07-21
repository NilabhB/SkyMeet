package com.skymeet.videoConference.utils;

import static com.skymeet.videoConference.utils.NavUtils.getNavController;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.skymeet.videoConference.AuthNavDirections;
import com.skymeet.videoConference.R;

public final class UiUtils {

    /**
     * Should be called from destination of auth_nav
     *
     * @param authFragment fragment destination of auth_nav
     */
    public static void showAuthExitDialog(@NonNull Fragment authFragment) {
        new AlertDialog.Builder(authFragment.requireContext())
                .setIcon(R.drawable.ic_baseline_guest_30)
                .setTitle("Guest Mode")
                .setMessage("Do you want enter as a Guest?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getNavController(authFragment).navigate(
                                AuthNavDirections.actionGlobalGuestNav()
                        );
                    }
                })
                .setNeutralButton("Exit App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        authFragment.requireActivity().finish();
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

    public static void showAppExitDialog(@NonNull Activity activity) {
        new AlertDialog.Builder(activity)
                .setIcon(R.drawable.exit_guest)
                .setTitle("Exit App")
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Rate App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String YourPageURL = "https://play.google.com/store/apps/details?id=com.skymeet.videoConference";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                        activity.startActivity(browserIntent);
                    }
                })
                .show();
    }
}
