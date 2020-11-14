package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;

import java.util.List;

// Class to obtain live data regarding the users belonging to the current group
public class MembersListViewModel extends AndroidViewModel {
    // ID numbers of all members in the current group
    private LiveData<List<Integer>> allGroupMemberIDs;

    // Class constructor
    public MembersListViewModel (@NonNull Application application, int groupID) {
        super(application);

        allGroupMemberIDs = MainActivity.database.groupUserDao().getUserIDs(groupID);
    }

    // Method to return ID numbers of all members of the current group
    public LiveData<List<Integer>> getAllGroupMemberIDs() {
        return allGroupMemberIDs;
    }
}