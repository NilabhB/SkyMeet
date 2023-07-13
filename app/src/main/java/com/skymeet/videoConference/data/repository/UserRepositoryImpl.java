package com.skymeet.videoConference.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skymeet.videoConference.data.model.User;
import com.skymeet.videoConference.data.model.UserSignInRequest;
import com.skymeet.videoConference.data.model.UserSignUpRequest;
import com.skymeet.videoConference.data.utils.AuthError;
import com.skymeet.videoConference.data.utils.NetworkResult;
import com.skymeet.videoConference.data.utils.NetworkResultState;

import javax.inject.Inject;

public class UserRepositoryImpl implements UserRepository {

    private final FirebaseAuth mFirebaseAuth;
    private final FirebaseFirestore mFirebaseFirestore;


    @Inject
    public UserRepositoryImpl(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        mFirebaseAuth = firebaseAuth;
        mFirebaseFirestore = firebaseFirestore;
    }

    public LiveData<NetworkResult<User>> signUpUser(@NonNull UserSignUpRequest request) {
        var signupState = new MediatorLiveData<NetworkResult<User>>(NetworkResult.loading());

        mFirebaseAuth.createUserWithEmailAndPassword(request.getEmail(), request.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                var authUser = task.getResult().getUser();
                assert authUser != null;
                var user = new User(request.getName(), request.getEmail(), authUser.getUid());

                //when user is saved, pass that result to signupState
                signupState.addSource(saveUser(user), result -> {
                    //when success, send email verification
                    if (result.getState() == NetworkResultState.SUCCESS)
                        authUser.sendEmailVerification();
                    signupState.setValue(result);

                });
            } else {
                signupState.setValue(NetworkResult.error(task.getException()));
            }
        });

        return signupState;
    }

    public LiveData<NetworkResult<String>> signInUser(@NonNull UserSignInRequest request) {
        var signInState = new MutableLiveData<NetworkResult<String>>(NetworkResult.loading());
        mFirebaseAuth.signInWithEmailAndPassword(request.getEmail(), request.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        var authUser = task.getResult().getUser();
                        assert authUser != null;
                        if (authUser.isEmailVerified()) {
                            signInState.setValue(NetworkResult.success(authUser.getUid()));
                        } else {
                            signInState.setValue(NetworkResult.error(new AuthError.UserNotVerifiedError()));
                        }
                    } else {
                        signInState.setValue(NetworkResult.error(task.getException()));
                    }
                });
        return signInState;
    }

    public boolean sendUserVerificationEmail() {
        var authUser = mFirebaseAuth.getCurrentUser();
        if (authUser == null)
            return false;
        authUser.sendEmailVerification();
        return true;
    }

    @NonNull
    public LiveData<NetworkResult<User>> saveUser(@NonNull User user) {
        var userCreationState = new MutableLiveData<NetworkResult<User>>();
        mFirebaseFirestore.collection("Users").document(user.getUserId()).set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userCreationState.setValue(NetworkResult.success(user));
                    } else {
                        userCreationState.setValue(NetworkResult.error(task.getException()));
                    }
                });
        return userCreationState;
    }

    public LiveData<NetworkResult<Void>> sendPasswordResetEmail(@NonNull String email) {
        var passwordResetEmailSendState = new MutableLiveData<NetworkResult<Void>>(NetworkResult.loading());

        mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                passwordResetEmailSendState.setValue(NetworkResult.success(task.getResult()));
            } else {
                passwordResetEmailSendState.setValue(NetworkResult.error(task.getException()));
            }
        });

        return passwordResetEmailSendState;
    }

    public LiveData<NetworkResult<User>> getUser() {
        var userState = new MutableLiveData<NetworkResult<User>>(NetworkResult.loading());
        var currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser == null)
            userState.setValue(NetworkResult.error(new IllegalArgumentException("User not authenticated")));
        else
            mFirebaseFirestore.collection("Users").document(currentUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isComplete()) {
                            userState.setValue(NetworkResult.success(task.getResult().toObject(User.class)));
                        } else {
                            userState.setValue(NetworkResult.error(task.getException()));
                        }
                    });
        return userState;
    }

    public boolean isUserSignedIn() {
        var user = mFirebaseAuth.getCurrentUser();
        return user != null && user.isEmailVerified();
    }

    public void signOut() {
        mFirebaseAuth.signOut();
    }
}
