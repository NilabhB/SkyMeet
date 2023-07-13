package com.skymeet.videoConference.data.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

public class NetworkResult<T> {
    @Nullable
    private final T data;
    @Nullable
    private final Exception error;
    @NonNull
    final private NetworkResultState state;

    private NetworkResult(@NonNull NetworkResultState state, @Nullable T data, @Nullable Exception error) {
        this.state = state;
        this.error = error;
        this.data = data;
    }


    @Nullable
    public Exception getError() {
        return error;
    }

    @NonNull
    public NetworkResultState getState() {
        return state;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> NetworkResult<T> success(T data) {
        return new NetworkResult<>(
                NetworkResultState.SUCCESS,
                data,
                null
        );
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public static <T> NetworkResult<T> loading() {
        return new NetworkResult<>(
                NetworkResultState.LOADING,
                null,
                null
        );
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> NetworkResult<T> error(@Nullable Exception e) {
        return new NetworkResult<>(
                NetworkResultState.ERROR,
                null,
                e
        );
    }
}
