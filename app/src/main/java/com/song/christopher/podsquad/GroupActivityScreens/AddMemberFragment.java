package com.song.christopher.podsquad.GroupActivityScreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.song.christopher.podsquad.Adapter.UserAdapter;
import com.song.christopher.podsquad.Database.GroupUser;
import com.song.christopher.podsquad.Database.User;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

import java.util.Arrays;
import java.util.List;

// Fragment to add a new member to a given group
public class AddMemberFragment extends Fragment {
    // Fields to store current group and user IDs
    private int groupID;
    private int userID;

    // Child views in the corresponding XML layout file
    private EditText numNewUsers;
    private EditText listNewUsers;
    private Button addNewUsers;

    /* Adapter used to display list of usernames in a 'RecyclerView' obj
     * (i.e. all usernames in a given group) */
    private UserAdapter adapter;

    // Constructor: initializes group and user ID fields
    public AddMemberFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the child views in the corresponding layout file
        numNewUsers = view.findViewById(R.id.num_new_users);
        listNewUsers = view.findViewById(R.id.list_new_users);
        addNewUsers = view.findViewById(R.id.add_new_users);

        // Create a new 'UserAdapter' object to display all usernames in a given group
        adapter = new UserAdapter(groupID);

        // Go here to add more users to a group (when the corresponding button is pressed)
        addNewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Fields that store the following:
                 * (1) Number of users attempted to be added
                 * (2) A comma-separated list of usernames (corresponding to users to be added)
                 * (3) Count to track how many users are successfully added */
                String numString = numNewUsers.getText().toString();
                int num = -1; // TODO (Fix?)

                // Error catching: make sure the user enters an integer number of users to add
                try {
                    num = Integer.parseInt(numString); // (1)
                } catch(NumberFormatException e) {
                    alertMessage("You must enter an integer number of users to add.");
                    return;
                }

                String listUsers = listNewUsers.getText().toString(); // (2)
                int count = 0; // (3)

                // Error catching: make sure the user adds at least one user (i.e. cannot have a blank field)
                if (listUsers.trim().isEmpty()) {
                    alertMessage("You must enter at least one user to add.");
                    return;
                }

                // List holding all usernames to be added to the group
                List<String> newUsernamesList = Arrays.asList(listUsers.split(", "));

                for (String username : newUsernamesList) {
                    // For each username in the^^ list, obtain the corresponding 'User' object and ID number
                    User user = MainActivity.database.userDao().getUserByUsername(username);
                    int userID = user.id;

                    // Determine whether this user is already in the group
                    GroupUser groupUser = MainActivity.database.groupUserDao().isUser(groupID, userID);

                    /* If a 'User' object with the given username exists and
                     * the user is not yet in the group, go ahead and add the user;
                     * also update the count variable (the number of users successfully added) */
                    if (user != null && groupUser == null) {
                        MainActivity.database.groupUserDao().addUser(groupID, userID);
                        count++;
                    }
                }

                // Direct users back to the the current group's homepage via an Intent
                Context context = view.getContext();
                Intent intent = new Intent(context, GroupActivity.class);
                intent.putExtra("groupID", groupID);
                intent.putExtra("userID", userID);

                startActivity(intent);

                // Also display a message based on whether or not all users were added successfully to the group
                if (count == num) {
                    alertMessage("All users were added successfully!");
                } else {
                    alertMessage("There was an error adding some users. " +
                            "Common issues include the following: " +
                            "(1) misspelling one or more usernames, " +
                            "(2) attempting to add a user who has already been added, " +
                            "(3) not submitting a comma-separated list of usernames, " +
                            "(4) typing in the wrong number of users to add.");
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