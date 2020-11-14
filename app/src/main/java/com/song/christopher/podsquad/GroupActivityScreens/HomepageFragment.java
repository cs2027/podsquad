package com.song.christopher.podsquad.GroupActivityScreens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.song.christopher.podsquad.R;

// Initial fragment displayed when a user first visits a group page
public class HomepageFragment extends Fragment {
    // Fields to store current group and user IDs
    private int groupID;
    private int userID;

    // Constructor for a 'HomepageFragment' object
    HomepageFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }
}