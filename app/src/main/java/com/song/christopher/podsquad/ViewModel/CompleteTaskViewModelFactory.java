package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// Class to allow us to pass custom parameters (i.e. a group ID number) to the corresponding 'ViewModel' class
public class CompleteTaskViewModelFactory implements ViewModelProvider.Factory {
    // Fields holding an 'Application' object & the current group ID number
    private Application application;
    private int groupID;

    // Class constructor
    public CompleteTaskViewModelFactory(Application application, int groupID) {
        this.application = application;
        this.groupID = groupID;
    }

    // Used to help construct the aforementioned custom 'ViewModel' object
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CompleteTaskViewModel(application, groupID);
    }
}
