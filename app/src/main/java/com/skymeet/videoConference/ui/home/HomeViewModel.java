package com.skymeet.videoConference.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skymeet.videoConference.data.model.User;
import com.skymeet.videoConference.data.repository.UserRepository;
import com.skymeet.videoConference.data.utils.NetworkResult;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    public final LiveData<NetworkResult<User>> user;
    private final UserRepository mUserRepository;

    @Inject
    public HomeViewModel(@NonNull UserRepository userRepository) {
        this.mUserRepository = userRepository;
        user = userRepository.getUser();
    }

    public void signOut() {
        mUserRepository.signOut();
    }

}
