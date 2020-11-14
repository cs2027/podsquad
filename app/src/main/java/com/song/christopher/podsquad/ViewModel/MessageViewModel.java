package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;

import java.util.List;

// Class to obtain live data regarding the messages of a given group
public class MessageViewModel extends AndroidViewModel {
    // List of ID numbers of all messages in the current group
    private LiveData<List<Integer>> allGroupMessageIDs;

    // Class constructor
    public MessageViewModel(@NonNull Application application, int groupID) {
        super(application);

        allGroupMessageIDs = MainActivity.database.groupMessageDao().getAllMessagesIDs(groupID);
    }

    // Method to return ID numbers of all messages in the current group
    public LiveData<List<Integer>> getAllGroupMessageIDs() {
        return allGroupMessageIDs;
    }
}
