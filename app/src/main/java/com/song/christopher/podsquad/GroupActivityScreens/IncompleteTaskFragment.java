package com.song.christopher.podsquad.GroupActivityScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.song.christopher.podsquad.Adapter.IncompleteTaskAdapter;
import com.song.christopher.podsquad.ViewModel.IncompleteTaskViewModel;
import com.song.christopher.podsquad.ViewModel.IncompleteTaskViewModelFactory;
import com.song.christopher.podsquad.R;

import java.util.List;

// Fragment to display all incomplete tasks for a given group
public class IncompleteTaskFragment extends Fragment {
    // Fields to store current group and user IDs
    private int groupID;
    private int userID;

    // Fields related to the RecyclerView displaying list of incomplete tasks
    private RecyclerView recyclerView;
    private IncompleteTaskAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // 'ViewModel' object allows us to track live data (i.e. the incomplete tasks list)
    // 'Factory' object allows us to pass the current groupID to filter the live data appropriately
    private IncompleteTaskViewModel incompleteTaskViewModel;
    private IncompleteTaskViewModelFactory factory;

    // Constructor: initializes user and group ID fields
    public IncompleteTaskFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_incomplete_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializes & sets up the RecyclerView to display incomplete tasks
        recyclerView = view.findViewById(R.id.incomplete_task_list);
        adapter = new IncompleteTaskAdapter(userID, groupID);
        layoutManager = new LinearLayoutManager(getContext());

        // Initialize the 'Factory' and 'ViewModel' objects to monitor the live data regarding the current incomplete tasks list
        factory = new IncompleteTaskViewModelFactory(getActivity().getApplication(), groupID);
        incompleteTaskViewModel = ViewModelProviders.of(this, factory).get(IncompleteTaskViewModel.class);
        incompleteTaskViewModel.getAllIncompleteTaskIDs().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                adapter.reloadIncompleteTasks(integers);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}