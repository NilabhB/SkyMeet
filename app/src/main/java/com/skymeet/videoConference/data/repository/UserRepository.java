package com.skymeet.videoConference.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.skymeet.videoConference.data.model.User;
import com.skymeet.videoConference.data.model.UserSignInRequest;
import com.skymeet.videoConference.data.model.UserSignUpRequest;
import com.skymeet.videoConference.data.utils.NetworkResult;

public interface UserRepository {
    LiveData<NetworkResult<User>> signUpUser(@NonNull UserSignUpRequest request);

    LiveData<NetworkResult<String>> signInUser(@NonNull UserSignInRequest request);

    boolean sendUserVerificationEmail();

    @NonNull
    LiveData<NetworkResult<User>> saveUser(@NonNull User user);

    LiveData<NetworkResult<Void>> sendPasswordResetEmail(@NonNull String email);

    LiveData<NetworkResult<User>> getUser();

    boolean isUserSignedIn();

    void signOut();
}
