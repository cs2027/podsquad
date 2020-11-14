package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// Class to allow us to pass custom parameters (i.e. a group ID number) to the corresponding 'ViewModel' class
public class GroupViewModelFactory implements ViewModelProvider.Factory {
    // Fields holding an 'Application' object & the current group ID number
    private Application application;
    private int userID;

    // Class constructor
    public GroupViewModelFactory(Application application, int userID) {
        this.application = application;
        this.userID = userID;
    }

    // Used to help construct the aforementioned custom 'ViewModel' object
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GroupViewModel(application, userID);
    }
}
