package com.song.christopher.podsquad.GroupActivityScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.song.christopher.podsquad.Database.Task;
import com.song.christopher.podsquad.Database.User;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

// Activity to edit the title and/or contents of a task
public class EditTaskActivity extends AppCompatActivity {
    // Data regarding the current group, user, and task
    // Passed in via an Intent (from 'TaskActivity.java')
    private int groupID;
    private int userID; // Refers to the ID number of the user currently logged in
    private int taskID;
    private int taskStatus;
    private Task task;
    private User taskOwner; // User who created the current task

    // Child views from the <activity_edit_task.xml> layout file to edit a task
    private EditText taskTitle;
    private EditText taskContents;
    private TextView taskUser; // View that displays who created the current task
    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Parse data from the Intent and initialize fields about current group, user, and task
        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID", 0);
        userID = intent.getIntExtra("userID", 0);
        taskID = intent.getIntExtra("taskID", 0);
        taskStatus = intent.getIntExtra("taskStatus", -1);
        task = MainActivity.database.taskDao().getTaskByID(taskID);
        taskOwner = MainActivity.database.userDao().getUserByID(task.user_id);

        // Obtain child views in the corresponding layout file & set their contents based on the current task
        // i.e. display information about the current task
        taskTitle = findViewById(R.id.edit_task_title);
        taskContents = findViewById(R.id.edit_task_contents);
        taskUser = findViewById(R.id.edit_task_user);
        saveChangesButton = findViewById(R.id.save_task_changes);

        taskTitle.setText(task.title);
        taskContents.setText(task.contents);
        taskUser.setText(taskOwner.username);

        // Button that allows user to save changes to the current task
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain the updated task title and contents
                String newTaskTitle = taskTitle.getText().toString();
                String newTaskContents = taskContents.getText().toString();

                // Error catching: make sure the title and contents of the current task are not empty/blank
                if (newTaskTitle.trim().isEmpty() || newTaskTitle == null) {
                    alertMessage("You cannot have a blank task title.");
                } else if (newTaskContents.trim().isEmpty() || newTaskContents == null) {
                    alertMessage("You cannot have blank task contents.");
                } else {
                    // Update the database entry for this^^ task
                    MainActivity.database.taskDao().editTask(
                            userID,
                            newTaskTitle,
                            newTaskContents,
                            taskID);

                    // Return to the 'TaskActivity' showing information about a specific task via an Intent
                    Context context = view.getContext();

                    Intent intent = new Intent(context, TaskActivity.class);
                    intent.putExtra("groupID", groupID);
                    intent.putExtra("userID", userID);
                    intent.putExtra("taskID", taskID);
                    intent.putExtra("taskStatus", taskStatus);

                    startActivity(intent);
                }
            }
        });
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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