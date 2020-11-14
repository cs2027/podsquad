package com.song.christopher.podsquad.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.song.christopher.podsquad.Database.GroupUser;
import com.song.christopher.podsquad.Database.User;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

import java.util.ArrayList;
import java.util.List;

// Adapter class to help display the list of users for a given group
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    /* Fields storing the following data:
     * (1) the ID number of the current group
     * (2) a list of ID numbers corresponding to users in this^^ group
     * (3) a list of 'User' objects corresponding to users in this^^ group */
    private int groupID; // (1)
    private List<Integer> userIDList = new ArrayList<>(); // (2)
    private List<User> userList = new ArrayList<>(); // (3)

    // Constructor to obtain and initialize the current group ID number
    public UserAdapter(int groupID) {
        this.groupID = groupID;
    }

    /* 'ViewHolder' object encapsulating a single row in the RecyclerView
     * (corresponding to a single 'User' object) */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        // Child views of the 'ViewHolder' object
        LinearLayout containerView;
        TextView textView;

        UserViewHolder(View view) {
            super(view);

            containerView = view.findViewById(R.id.user_row); // Stores entire row (i.e. an entire 'User' object)
            textView = view.findViewById(R.id.user_row_text); // Stores username of a given 'User' object
        }
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        // Obtain the list of users associated with the current group
        userList = MainActivity.database.userDao().getUsersByID(userIDList);

        // Obtain the current 'User' object & determine if he/she is the group owner
        User currentUser = userList.get(position);
        GroupUser isOwner = MainActivity.database.groupUserDao().isOwner(groupID, currentUser.id);

        // Display the current user's username, also denoting them if they are the group owner
        if (isOwner != null) {
            holder.textView.setText(currentUser.username + " (Owner)");
        } else {
            holder.textView.setText(currentUser.username);
        }
    }

    @Override
    public int getItemCount() {
        return userIDList.size();
    }

    // Reload the list of users of the current group
    // Notifies the RecyclerView of these^^ updates
    public void reloadMembersList(List<Integer> userIDList) {
        this.userIDList = userIDList;
        userList = MainActivity.database.userDao().getUsersByID(userIDList);

        notifyDataSetChanged();
    }
}
