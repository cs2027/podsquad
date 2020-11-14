package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;

import java.util.List;

// Class to obtain live data regarding the completed tasks of a given group
public class CompleteTaskViewModel extends AndroidViewModel {
    // List holding ID numbers of all completed tasks
    private LiveData<List<Integer>> allCompleteTaskIDs;

    // Class constructor
    public CompleteTaskViewModel(@NonNull Application application, int groupID) {
        super(application);

        allCompleteTaskIDs = MainActivity.database.groupTaskDao().getCompleteTaskIDs(groupID);
    }

    // Method to obtain ID number list of all completed tasks
    public LiveData<List<Integer>> getAllCompleteTaskIDs() {
        return allCompleteTaskIDs;
    }
}