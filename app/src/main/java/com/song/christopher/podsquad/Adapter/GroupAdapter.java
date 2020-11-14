package com.song.christopher.podsquad.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.song.christopher.podsquad.Database.Group;
import com.song.christopher.podsquad.Database.GroupUser;
import com.song.christopher.podsquad.Database.User;
import com.song.christopher.podsquad.GroupActivityScreens.GroupActivity;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

import java.util.ArrayList;
import java.util.List;

// Adapter class for our <RecyclerView> that displays a list of all groups for a given user
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    /* Fields to store the following information:
     * (1) User ID of the user who is currently logged in
     * (2) Current 'User' object
     * (3) List storing ID numbers of all groups that the current user belongs to
     * (4) List storing all groups that the current user belongs to */
    private int currentUserID; // (1)
    private User currentUser; // (2)
    private List<Integer> allGroupIDs = new ArrayList<>(); // (3)
    private List<Group> allGroups = new ArrayList<>(); // (4)

    /* Constructor for a 'GroupAdapter' object: initializes the current user's ID number
     * and the 'User' associated with that ID number */
    public GroupAdapter(int currentUserID) {
        this.currentUserID = currentUserID;
        currentUser = MainActivity.database.userDao().getUserByID(currentUserID);
    }

    // Constructor for the Adapter's 'ViewHolder' object (which encapsulates a single group object)
    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        // Create two fields to store the child views of a given row (see <group_row.xml>)
        LinearLayout containerView;
        TextView textView_Name;
        TextView textView_Status;

        // Constructor for the ViewHolder object
        GroupViewHolder(View view) {
            super(view);

            // Obtains child views from the XML layout file <group_row.xml>
            containerView = view.findViewById(R.id.group_row);
            textView_Name = view.findViewById(R.id.group_row_text);
            textView_Status = view.findViewById(R.id.member_status);

            // Moves to the 'GroupActivity' screen, displaying information about an information group
            // Passes in the current group ID and user ID via an Intent
            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Group currentGroup = (Group) containerView.getTag(R.string.group_obj);
                    User user = (User) containerView.getTag(R.string.user_obj);
                    Intent intent = new Intent(context, GroupActivity.class);
                    intent.putExtra("groupID", currentGroup.id);
                    intent.putExtra("userID", user.id);

                    context.startActivity(intent);
                }
            });

        }
    }

    // Creates a ViewHolder object encapsulating a single 'Group' object
    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Convert the XML layout file for a single group to a view object
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);

        // Construct a new ViewHolder object using this^^ view
        return new GroupViewHolder(view);
    }

    // Binds the correct data to each row in our <RecyclerView>
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        // Determines the groups that the current user belongs to
        allGroups = MainActivity.database.groupDao().getAllGroups(allGroupIDs);

        // Obtain the current 'Group' object
        Group currentGroup = allGroups.get(position);

        // Display the name of each 'Group' object in its corresponding child view in the layout file
        holder.textView_Name.setText(currentGroup.name + " | ");

        // Determine if the current user is the owner of the current group
        GroupUser isOwner = MainActivity.database.groupUserDao().isOwner(currentGroup.id, currentUserID);

        // Display a different status message (owner vs. member) based on if the current user is the group owner or not
        if (isOwner != null) {
            holder.textView_Status.setText("Status: OWNER");
            holder.textView_Status.setTextColor(Color.parseColor("#F16060"));
        } else {
            holder.textView_Status.setText("Status: MEMBER");
            holder.textView_Status.setTextColor(Color.parseColor("#188631"));
        }

        // Add tags to the containerView for a given 'Group' object, holding data about the current group and user
        holder.containerView.setTag(R.string.group_obj, currentGroup);
        holder.containerView.setTag(R.string.user_obj, currentUser);
    }

    // Returns number of groups the current user belongs to
    @Override
    public int getItemCount() {
        return allGroupIDs.size();
    }

    // Updates the groups that the current user belongs to
    // Passes these^^ updates to the 'RecyclerView' displaying all groups
    public void reloadGroups(List<Integer> allGroupIDs) {
        this.allGroupIDs = allGroupIDs;
        allGroups = MainActivity.database.groupDao().getAllGroups(allGroupIDs);

        notifyDataSetChanged();
    }
}
