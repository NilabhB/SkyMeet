package com.skymeet.videoConference.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.skymeet.videoConference.data.model.User;
import com.skymeet.videoConference.data.model.UserSignInRequest;
import com.skymeet.videoConference.data.model.UserSignUpRequest;
import com.skymeet.videoConference.data.repository.UserRepository;
import com.skymeet.videoConference.data.utils.NetworkResult;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final UserRepository mUserRepository;

    private final MutableLiveData<UserSignUpRequest> userSignUpRequest = new MutableLiveData<>();
    public final LiveData<NetworkResult<User>> userSignUpState;

    private final MutableLiveData<UserSignInRequest> userSignInRequest = new MutableLiveData<>();
    public final LiveData<NetworkResult<String>> userSignInState;

    private final MutableLiveData<String> passwordResetEmail = new MutableLiveData<>();
    public final LiveData<NetworkResult<Void>> passwordResetEmailSendState;

    @Inject
    public AuthViewModel(@NonNull UserRepository userRepository) {
        this.mUserRepository = userRepository;
        userSignUpState = Transformations.switchMap(userSignUpRequest, mUserRepository::signUpUser);
        userSignInState = Transformations.switchMap(userSignInRequest, mUserRepository::signInUser);
        passwordResetEmailSendState = Transformations.switchMap(passwordResetEmail, userRepository::sendPasswordResetEmail);
    }

    public void signUp(@NonNull UserSignUpRequest request) {
        userSignUpRequest.setValue(request);
    }

    public void signIn(@NonNull UserSignInRequest request) {
        userSignInRequest.setValue(request);
    }

    public boolean sendVerificationEmail() {
        return mUserRepository.sendUserVerificationEmail();
    }

    public void sendPasswordResetEmail(@NonNull String email) {
        passwordResetEmail.setValue(email);
    }

}
