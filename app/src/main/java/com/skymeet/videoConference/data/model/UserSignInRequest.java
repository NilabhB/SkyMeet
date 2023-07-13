package com.skymeet.videoConference.data.model;

import androidx.annotation.NonNull;

public class UserSignInRequest {
    @NonNull
    private final String email;
    @NonNull
    private final String password;

    public UserSignInRequest(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }
}
