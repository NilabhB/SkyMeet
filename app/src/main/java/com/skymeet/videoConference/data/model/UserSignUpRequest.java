package com.skymeet.videoConference.data.model;

import androidx.annotation.NonNull;

public class UserSignUpRequest {
    @NonNull
    private final String name;
    @NonNull
    private final String email;
    @NonNull
    private final String password;

    public UserSignUpRequest(@NonNull String name, @NonNull String email, @NonNull String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getName() {
        return name;
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
