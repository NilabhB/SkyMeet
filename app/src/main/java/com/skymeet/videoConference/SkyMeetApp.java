package com.skymeet.videoConference;

import android.app.Application;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SkyMeetApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //since this onCreate method is called
        //before any activity, service, or receiver objects
        //so, set default options like url
        //to jitsi conference here
        URL serverURL = null;
        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .build();

        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
    }
}
