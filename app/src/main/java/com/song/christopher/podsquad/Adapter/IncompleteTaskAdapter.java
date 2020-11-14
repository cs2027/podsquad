package com.song.christopher.podsquad.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.song.christopher.podsquad.Database.Group;
import com.song.christopher.podsquad.Database.Task;
import com.song.christopher.podsquad.Database.User;
import com.song.christopher.podsquad.GroupActivityScreens.TaskActivity;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

import java.util.ArrayList;
import java.util.List;

// Adapter class to help display the list of all incomplete tasks in a given group
public class IncompleteTaskAdapter extends RecyclerView.Adapter<IncompleteTaskAdapter.TaskViewHolder_IC> {
    /* Fields to store the following data:
     * (1) user and group ID
     * (2) the incomplete task list for the current group */
    private int userID;
    private int groupID;
    private List<Integer> taskIDList = new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();

    // Constructor for the adapter class; initializes user and group ID fields
    public IncompleteTaskAdapter(int userID, int groupID) {
        this.userID = userID;
        this.groupID = groupID;
    }

    // Constructor for the ViewHolder object that encapsulates a single incomplete task
    public static class TaskViewHolder_IC extends RecyclerView.ViewHolder {
        // Child views in the corresponding XML file <incomplete_task_row.xml>
        LinearLayout containerView;
        TextView textView_Title;
        TextView textView_Username;
        TextView textView_Status;

        // Constructor for the 'ViewHolder' object
        TaskViewHolder_IC(View view) {
            super(view);

            // Initialize the child views from the corresponding XML layout file
            containerView = view.findViewById(R.id.incomplete_task_row);
            textView_Title = view.findViewById(R.id.incomplete_task_row_title);
            textView_Username = view.findViewById(R.id.incomplete_task_row_username);
            textView_Status = view.findViewById(R.id.incomplete_task_row_status);

            // Move to the task activity screen displaying information about a specific task via an Intent
            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Group group = (Group) containerView.getTag(R.string.group_obj);
                    User user = (User) containerView.getTag(R.string.user_obj);
                    Task task = (Task) containerView.getTag(R.string.task_obj);
                    Intent intent = new Intent(context, TaskActivity.class);
                    intent.putExtra("groupID", group.id);
                    intent.putExtra("userID", user.id);
                    intent.putExtra("taskID", task.id);
                    intent.putExtra("taskStatus", 0);

                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder_IC onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incomplete_task_row, parent, false);

        return new TaskViewHolder_IC(view);
    }

    // Binds data about a specific task to its corresponding view in the layout file
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder_IC holder, int position) {
        // List of all incomplete tasks for the current group
        taskList = MainActivity.database.taskDao().getTasksByIDs(taskIDList);

        // Current task in this^^ list
        Task currentTask = taskList.get(position);

        // Obtain & display data information about the current task
        int taskOwnerID = currentTask.user_id;
        User taskOwner = MainActivity.database.userDao().getUserByID(taskOwnerID);
        String taskOwnerUsername = taskOwner.username;

        holder.textView_Title.setText("Task Title: " + currentTask.title);
        holder.textView_Username.setText("Posted By: " + taskOwnerUsername);

        if (taskOwnerID == userID) {
            holder.textView_Status.setText(" " + "(You)");
        } else {
            holder.textView_Status.setText("");
        }

        // Add tags to each 'containerView' object to allow us to display specific info about each task
        // See constructor for the 'ViewHolder' object defined above
        holder.containerView.setTag(R.string.group_obj, MainActivity.database.groupDao().getGroupByID(groupID));
        holder.containerView.setTag(R.string.user_obj, MainActivity.database.userDao().getUserByID(userID));
        holder.containerView.setTag(R.string.task_obj, currentTask);
    }


    @Override
    public int getItemCount() {
        return taskIDList.size();
    }

    // Reloads the list of incomplete tasks & notifies the corresponding RecyclerView of changes
    public void reloadIncompleteTasks(List<Integer> taskIDList) {
        this.taskIDList = taskIDList;
        taskList = MainActivity.database.taskDao().getTasksByIDs(taskIDList);

        notifyDataSetChanged();
    }
}
