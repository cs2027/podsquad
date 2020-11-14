package com.song.christopher.podsquad.GroupActivityScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.song.christopher.podsquad.Database.Group;
import com.song.christopher.podsquad.Database.Task;
import com.song.christopher.podsquad.Database.User;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

// Class to display information about a specific 'Task' object
public class TaskActivity extends AppCompatActivity {
    // Fields to hold information about the current group, user, and task
    private int groupID;
    private int userID;
    private int taskID;
    private Group currentGroup;
    private User currentUser;
    private Task currentTask;
    private int taskStatus;

    // Fields for the child views in the corresponding XML layout file <activity_task.xml>
    private TextView taskTitle;
    private TextView taskContents;
    private TextView taskUser;
    private Button editTaskButton;
    private Button deleteTaskButton;
    private Button changeStatusButton;
    private Button homepageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Initialize fields regarding the current group, user and task objects
        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID", 0);
        userID = intent.getIntExtra("userID", 0);
        taskID = intent.getIntExtra("taskID", 0);
        currentGroup = MainActivity.database.groupDao().getGroupByID(groupID);
        currentUser = MainActivity.database.userDao().getUserByID(userID);
        currentTask = MainActivity.database.taskDao().getTaskByID(taskID);
        taskStatus = intent.getIntExtra("taskStatus", -1);

        // Initialize the child views in the corresponding layout file
        taskTitle = findViewById(R.id.task_title);
        taskContents = findViewById(R.id.task_contents);
        taskUser = findViewById(R.id.task_user);
        editTaskButton = findViewById(R.id.edit_task_button);
        deleteTaskButton = findViewById(R.id.delete_task_button);
        changeStatusButton = findViewById(R.id.change_task_status);
        homepageButton = findViewById(R.id.to_group_homepage);

        taskTitle.setText(currentTask.title);
        taskContents.setText(currentTask.contents);
        taskUser.setText(MainActivity.database.userDao().getUserByID(currentTask.user_id).username);

        // Goes to another activity ('EditTaskActivity.java') to allow the user to edit the current task
        // Only allows user to edit the task if he/she is the one who created the task
        if (userID == MainActivity.database.taskDao().getTaskByID(taskID).user_id) {
            editTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();

                    Intent intent = new Intent(context, EditTaskActivity.class);
                    intent.putExtra("groupID", groupID);
                    intent.putExtra("userID", userID);
                    intent.putExtra("taskID", taskID);
                    intent.putExtra("taskStatus", taskStatus);

                    startActivity(intent);
                }
            });
        } else {
            editTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertMessage("You do not have permission to edit tasks that you did not create.");
                }
            });
        }

        // Deletes the current task
        // Only allows users to delete their own tasks
        if (userID == MainActivity.database.taskDao().getTaskByID(taskID).user_id) {
            deleteTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Erases task from database
                    MainActivity.database.taskDao().deleteTask(taskID);
                    MainActivity.database.groupTaskDao().deleteGroupTask(groupID, taskID);

                    // Direct the user back to the group's homepage
                    Context context = view.getContext();
                    Intent intent = new Intent(context, GroupActivity.class);
                    intent.putExtra("groupID", groupID);
                    intent.putExtra("userID", userID);

                    startActivity(intent);

                    // Display a message to the user saying that the task has been deleted
                    alertMessage("Task has been deleted.");
                }
            });
        } else {
            deleteTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertMessage("You do not have permission to delete tasks that you did not create.");
                }
            });
        }

        // This block of code allows users to change the status of a task from complete to incomplete (or vice versa)
        // Only the original user who posted a task can change its status
        if (taskStatus == 1) { // Go here for tasks currently marked as complete
            changeStatusButton.setText("Mark as Incomplete");

            if (userID == MainActivity.database.taskDao().getTaskByID(taskID).user_id) {
                // Change the task status from complete to incomplete here...
                changeStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Update the database regarding the current task object
                        MainActivity.database.groupTaskDao().changeTaskStatus(taskID, 0);

                        // Reload the current group's homepage via an Intent
                        Context context = view.getContext();
                        Intent intent = new Intent(context, GroupActivity.class);
                        intent.putExtra("groupID", groupID);
                        intent.putExtra("userID", userID);

                        startActivity(intent);

                        // Display a message saying that the task is now 'incomplete'
                        alertMessage("Task has been marked as incomplete.");
                    }
                });
            } else {
                changeStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertMessage("You do not have permission to mark this task as incomplete.");
                    }
                });
            }
        } else if (taskStatus == 0) { // Do a similar thing for incomplete tasks as well
            changeStatusButton.setText("Mark as Complete");

            if (userID == MainActivity.database.taskDao().getTaskByID(taskID).user_id) {
                changeStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.database.groupTaskDao().changeTaskStatus(taskID, 1);

                        Context context = view.getContext();
                        Intent intent = new Intent(context, GroupActivity.class);
                        intent.putExtra("groupID", groupID);
                        intent.putExtra("userID", userID);

                        startActivity(intent);

                        alertMessage("Task has been marked as complete.");
                    }
                });
            } else {
                changeStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertMessage("You do not have permission to mark this task as complete.");
                    }
                });
            }
        }

        // Button to go from an individual task page back to the group's homepage (via an Intent)
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, GroupActivity.class);
                intent.putExtra("groupID", groupID);
                intent.putExtra("userID", userID);

                startActivity(intent);
            }
        });
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
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
