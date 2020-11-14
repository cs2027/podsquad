package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;

import java.util.List;

// Class to obtain live data regarding all groups the current user belongs to
public class GroupViewModel extends AndroidViewModel {
    // List holding ID numbers of all groups the current user belongs to
    private LiveData<List<Integer>> allGroupIDs;

    // Class constructor
    public GroupViewModel(@NonNull Application application, int userID) {
        super(application);

        allGroupIDs = MainActivity.database.groupUserDao().getGroupIDs(userID);
    }

    // Method to obtain all group ID numbers that the current user belongs to
    public LiveData<List<Integer>> getAllGroupIDs() {
        return allGroupIDs;
    }
}
