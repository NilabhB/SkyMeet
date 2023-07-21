package com.skymeet.videoConference.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

final public class NavUtils {
    @NonNull
    public static NavController getNavController(@NonNull Fragment fragment) {
        return Navigation.findNavController(fragment.requireView());
    }
}
