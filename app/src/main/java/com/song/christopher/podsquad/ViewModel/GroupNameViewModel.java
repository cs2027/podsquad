package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;

// Class to obtain live data regarding the current group's name
public class GroupNameViewModel extends AndroidViewModel {
    // String to hold the group's current name
    private LiveData<String> groupName;

    // Class constructor
    public GroupNameViewModel(@NonNull Application application, int groupID) {
        super(application);

        groupName = MainActivity.database.groupDao().getGroupNameByID(groupID);
    }

    // Method to return the group's current name
    public LiveData<String> getGroupName() {
        return groupName;
    }
}
