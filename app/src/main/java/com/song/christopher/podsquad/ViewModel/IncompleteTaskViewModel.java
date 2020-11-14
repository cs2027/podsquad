package com.song.christopher.podsquad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;

import java.util.List;

// Class to obtain live data regarding the incomplete tasks of a given group
public class IncompleteTaskViewModel extends AndroidViewModel {
    // List of ID numbers of all incomplete tasks
    private LiveData<List<Integer>> allIncompleteTaskIDs;

    // Class constructor
    public IncompleteTaskViewModel(@NonNull Application application, int groupID) {
        super(application);

        allIncompleteTaskIDs = MainActivity.database.groupTaskDao().getIncompleteTaskIDs(groupID);
    }

    // Method to obtain ID numbers of all incomplete tasks
    public LiveData<List<Integer>> getAllIncompleteTaskIDs() {
        return allIncompleteTaskIDs;
    }
}
