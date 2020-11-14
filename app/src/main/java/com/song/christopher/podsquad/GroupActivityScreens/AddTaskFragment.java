package com.song.christopher.podsquad.GroupActivityScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

// TODO
public class AddTaskFragment extends Fragment {
    // Fields to store current group and user IDs
    private int groupID;
    private int userID;

    // Fields related to adding a new task
    private EditText newTaskTitle;
    private EditText newTaskContents;
    private Button addTaskButton;

    // Constructor: initializes user and group ID fields
    public AddTaskFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize fields related to adding a new task
        newTaskTitle = view.findViewById(R.id.new_task_title);
        newTaskContents = view.findViewById(R.id.new_task_contents);
        addTaskButton = view.findViewById(R.id.add_task_button);

        // Go here to add a new task
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain initial title and contents of the new task
                String title = newTaskTitle.getText().toString();
                String contents = newTaskContents.getText().toString();

                // Ensure neither the title nor contents of the new task is empty/blank
                if (title.trim().isEmpty() || title == null) {
                    alertMessage("You cannot have a blank task title.");
                } else if (contents.trim().isEmpty() || contents == null) {
                    alertMessage("You cannot have blank task contents.");
                } else {
                    // TODO
                    alertMessage("The task - '" + title + "' - has successfully been added.");
                    // Add the new task to the 'tasks' and 'group_tasks' tables
                    MainActivity
                            .database
                            .taskDao()
                            .createNewTask(userID, title, contents);

                    int newTaskID = MainActivity.database.taskDao().latestTaskID(userID, title, contents);
                    MainActivity.database.groupTaskDao().addGroupTask(groupID, newTaskID);

                    // Clear the fields to add a new task
                    newTaskTitle.setText("");
                    newTaskContents.setText("");
                }
            }
        });
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