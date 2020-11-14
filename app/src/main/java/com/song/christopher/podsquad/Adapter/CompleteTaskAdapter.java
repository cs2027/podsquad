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

// Adapter class to help display the list of all completed tasks in a given group
public class CompleteTaskAdapter extends RecyclerView.Adapter<CompleteTaskAdapter.TaskViewHolder_C> {
    /* Fields to store the following data:
     * (1) user and group ID
     * (2) the completed task list for the current group */
    private int userID; // (1)
    private int groupID;
    private List<Integer> taskIDList = new ArrayList<>(); // (2)
    private List<Task> taskList = new ArrayList<>();

    // Constructor for the adapter class; initializes user and group ID fields
    public CompleteTaskAdapter(int userID, int groupID) {
        this.userID = userID;
        this.groupID = groupID;
    }

    // Constructor for the ViewHolder object that encapsulates a single completed task
    public static class TaskViewHolder_C extends RecyclerView.ViewHolder {
        // Child views in the corresponding XML file <complete_task_row.xml>
        LinearLayout containerView;
        TextView textView_Title;
        TextView textView_Username;
        TextView textView_Status;

        // Constructor for the 'ViewHolder' object
        TaskViewHolder_C(View view) {
            super(view);

            // Initialize the child views from the corresponding XML layout file
            containerView = view.findViewById(R.id.complete_task_row);
            textView_Title = view.findViewById(R.id.complete_task_row_title);
            textView_Username = view.findViewById(R.id.complete_task_row_username);
            textView_Status = view.findViewById(R.id.complete_task_row_status);

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
                    intent.putExtra("taskStatus", 1);

                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder_C onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complete_task_row, parent, false);

        return new TaskViewHolder_C(view);
    }

    // Binds data about a specific task to its corresponding view in the layout file
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder_C holder, int position) {
        // List of all completed tasks for the current group
        taskList = MainActivity.database.taskDao().getTasksByIDs(taskIDList);

        // Current task in this^^ list
        Task currentTask = taskList.get(position);

        // Obtain & display data information about the current task
        int id = currentTask.user_id;
        User user = MainActivity.database.userDao().getUserByID(id);
        String username = user.username;

        holder.textView_Title.setText("Task Title: " + currentTask.title);
        holder.textView_Username.setText("Posted By: " + username);

        if (id == userID) {
            holder.textView_Status.setText(" " + "(You)");
        } else {
            holder.textView_Status.setText("");
        }

        // Add tags to each 'containerView' object to allow us to display specific info about each task
        // See constructor for the 'ViewHolder' object defined above
        holder.containerView.setTag(R.string.group_obj, MainActivity.database.groupDao().getGroupByID(groupID));
        holder.containerView.setTag(R.string.user_obj, user);
        holder.containerView.setTag(R.string.task_obj, currentTask);
    }

    @Override
    public int getItemCount() {
        return taskIDList.size();
    }

    // Reloads the list of completed tasks & notifies the corresponding RecyclerView of changes
    public void reloadCompleteTasks(List<Integer> taskIDList) {
        this.taskIDList = taskIDList;
        taskList = MainActivity.database.taskDao().getTasksByIDs(taskIDList);

        notifyDataSetChanged();
    }
}
