package com.song.christopher.podsquad.LoginActivityScreens;

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

import com.song.christopher.podsquad.R;

// TODO
public class NewGroupActivity extends AppCompatActivity {
    // TODO
    private TextView usernameDisplay;
    private Button toAllGroupsPage;
    private Button logoutButton;
    private EditText groupNameField;
    private Button newGroupButton;
    private int userID;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        // TODO
        usernameDisplay = findViewById(R.id.username);
        toAllGroupsPage = findViewById(R.id.to_all_groups_page);
        logoutButton = findViewById(R.id.logout);
        groupNameField = findViewById(R.id.new_group_name);
        newGroupButton = findViewById(R.id.new_group_button);

        // TODO
        Intent intent = getIntent();
        userID = (Integer) intent.getIntExtra("id", 0);
        username = (String) intent.getStringExtra("username");

        // TODO
        usernameDisplay.setText(username);

        // TODO
        toAllGroupsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, WelcomeActivity.class);
                intent.putExtra("id", userID);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        });

        // TODO
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Update the user's login status to false (indicating that he/she is not logged in)
                MainActivity.database.userDao().loginStatus(userID, false);

                // Create a new intent to return to the main activity screen (i.e. the login page fragment)
                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        // TODO
        newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain the (initial) name of the new group
                String groupName = groupNameField.getText().toString();

                // Prevent the user from having an empty group name
                if (groupName.trim().isEmpty() || groupName == null) {
                    alertMessage("You cannot have an empty group name.");
                } else {
                    /* Use a DAO method to create a new group with the
                     * specified user and group name */
                    MainActivity.database.groupDao().createGroup(userID, groupName);

                    // Designate the current user as the owner of the newly created group
                    int newGroupID = MainActivity.database.groupDao().latestGroupID(userID, groupName);
                    MainActivity.database.groupUserDao().addOwner(newGroupID, userID);

                    // Clear the <TextView> area where users can type new group names
                    groupNameField.setText("");

                    alertMessage("A new group - '" + groupName + "' - has successfully been created.");
                }
            }
        });
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewGroupActivity.this);
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