package com.song.christopher.podsquad.GroupActivityScreens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.song.christopher.podsquad.Adapter.CompleteTaskAdapter;
import com.song.christopher.podsquad.ViewModel.CompleteTaskViewModel;
import com.song.christopher.podsquad.ViewModel.CompleteTaskViewModelFactory;
import com.song.christopher.podsquad.R;

import java.util.List;

// Fragment for displaying a list of all completed tasks for a given group
public class CompleteTaskFragment extends Fragment {
    // Fields to store current group and user IDs
    private int groupID;
    private int userID;

    // Fields related to the RecyclerView displaying list of completed tasks
    private RecyclerView recyclerView;
    private CompleteTaskAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // Constructor: initializes user and group ID fields
    public CompleteTaskFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    // 'ViewModel' object allows us to track live data (i.e. the completed tasks list)
    // 'Factory' object allows us to pass the current groupID to filter the live data appropriately
    private CompleteTaskViewModel completeTaskViewModel;
    private CompleteTaskViewModelFactory factory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializes & sets up the RecyclerView to display completed tasks
        recyclerView = view.findViewById(R.id.complete_task_list);
        adapter = new CompleteTaskAdapter(userID, groupID);
        layoutManager = new LinearLayoutManager(getContext());

        // Initialize the 'Factory' and 'ViewModel' objects to monitor the live data regarding the current completed tasks list
        factory = new CompleteTaskViewModelFactory(getActivity().getApplication(), groupID);
        completeTaskViewModel = ViewModelProviders.of(this, factory).get(CompleteTaskViewModel.class);
        completeTaskViewModel.getAllCompleteTaskIDs().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                adapter.reloadCompleteTasks(integers);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}