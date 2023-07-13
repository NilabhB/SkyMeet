package com.skymeet.videoConference.di;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skymeet.videoConference.data.repository.UserRepository;
import com.skymeet.videoConference.data.repository.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AppModule {

    @NonNull
    @Provides
    @Singleton
    public static FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @NonNull
    @Provides
    @Singleton
    public static FirebaseFirestore providesFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Binds
    @Singleton
    public abstract UserRepository providesUserRepository(UserRepositoryImpl userRepository);
}
