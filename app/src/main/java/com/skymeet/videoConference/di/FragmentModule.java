package com.skymeet.videoConference.di;

import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.scopes.FragmentScoped;

@Module
@InstallIn(FragmentComponent.class)
public class FragmentModule {

    @Provides
    @FragmentScoped
    public static JitsiMeetConferenceOptions.Builder providesJitsiMeetConferenceOptionsBuilder() {
        return new JitsiMeetConferenceOptions.Builder()
                .setFeatureFlag("welcomepage.enabled", false)
                .setFeatureFlag("live-streaming.enabled", false)
                .setFeatureFlag("invite.enabled", false)
                .setFeatureFlag("video-share.enabled", false)
                .setFeatureFlag("overflow-menu.enabled", true)
                .setFeatureFlag("fullscreen.enabled", true);
    }
}
