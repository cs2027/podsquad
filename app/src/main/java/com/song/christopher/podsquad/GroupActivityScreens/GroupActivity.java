package com.song.christopher.podsquad.GroupActivityScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.song.christopher.podsquad.Database.GroupUser;
import com.song.christopher.podsquad.ViewModel.GroupNameViewModel;
import com.song.christopher.podsquad.ViewModel.GroupNameViewModelFactory;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;
import com.song.christopher.podsquad.LoginActivityScreens.WelcomeActivity;

// Activity displaying information about a specific group (messages, tasks, etc.)
public class GroupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /* Fields storing the following data:
     * (1) the child view that displays the current group's name
     * (2) information about the current user and group
     * (3) whether or not the current user is the owner of the current group */
    private TextView groupNameContents; // (1)
    private int userID; // (2)
    private int groupID;
    private LiveData<String> groupName;
    private GroupUser isOwner; // (3)

    // 'ViewModel' object allows us to track live data (i.e. the current group name)
    // 'Factory' object allows us to pass the current groupID to filter the live data appropriately
    private GroupNameViewModel groupNameViewModel;
    private GroupNameViewModelFactory factory;

    /* Fields storing the following data:
     * (1) the child view holding the requested fragment
     * (2) a 'Spinner' object to toggle between the different fragments
     * (3) a 'Button' object to leave the current group */
    private FrameLayout fragmentContainer; // (1)
    private Spinner spinner;
    private Button leaveGroup;

    // Fields to store the different fragments to be displayed
    private HomepageFragment homepageFragment;
    private CompleteTaskFragment completeTaskFragment;
    private IncompleteTaskFragment incompleteTaskFragment;
    private MessageFragment messageFragment;
    private MemberListFragment memberListFragment;
    private AddMemberFragment addMembersFragment;
    private AddTaskFragment addTaskFragment;
    private EditNameFragment editNameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // Parse data regarding the current user and group from the Intent that was passed in
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", 0);
        groupNameContents = (TextView) findViewById(R.id.group_name_contents);
        groupID = intent.getIntExtra("groupID", 0);
        groupName = MainActivity.database.groupDao().getGroupNameByID(groupID);
        isOwner = MainActivity.database.groupUserDao().isOwner(groupID, userID);

        // Initialize the 'Factory' and 'ViewModel' objects to monitor the live data regarding the current group name
        factory = new GroupNameViewModelFactory(getApplication(), groupID);
        groupNameViewModel = ViewModelProviders.of(this, factory).get(GroupNameViewModel.class);
        groupNameViewModel.getGroupName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                groupNameContents.setText(s);
            }
        });

        // Initialize the spinner object to display all of the different possible fragments
        Spinner spinner = findViewById(R.id.group_activity_menu);
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.group_activity_fragments));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        /* Initialize the other child views in the layout file:
         * (1) view to hold the current fragment
         * (2) button to leave the current group when clicked */
        fragmentContainer = findViewById(R.id.group_fragment_container); // (1)
        leaveGroup = findViewById(R.id.leave_group_button); // (2)

        // Prevent the group owner from leaving his/her own group
        if (isOwner != null) {
            leaveGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertMessage("Because you are the group owner, you cannot leave the group.");
                }
            });
        } else {
            leaveGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                    builder.setMessage("Are you sure you want to leave this group?");
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Remove the current user from the current group (in the database)
                            MainActivity.database.groupUserDao().removeUser(groupID, userID);

                            // Direct the user back to the 'WelcomeActivity' displaying all of his/her groups via an Intent
                            // Will no longer include the group he/she just left
                            Context context = getApplicationContext();

                            Intent intent = new Intent(context, WelcomeActivity.class);
                            intent.putExtra("id", userID);
                            intent.putExtra("username",  MainActivity.database.userDao().getUserByID(userID).username);

                            startActivity(intent);

                            // Display a message to the user indicating that they have left the group
                            String groupName = MainActivity.database.groupDao().getGroupByID(groupID).name;
                            alertMessage("You have successfully left the group: " + groupName);
                        }
                    });

                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }

        // Initialize each of the fragment objects, passing in the current group and user ID to each
        homepageFragment = new HomepageFragment(groupID, userID);
        completeTaskFragment = new CompleteTaskFragment(groupID, userID);
        incompleteTaskFragment = new IncompleteTaskFragment(groupID, userID);
        messageFragment = new MessageFragment(groupID, userID);
        memberListFragment = new MemberListFragment(groupID, userID);
        addMembersFragment = new AddMemberFragment(groupID, userID);
        addTaskFragment = new AddTaskFragment(groupID, userID);
        editNameFragment = new EditNameFragment(groupID, userID);

        /* TODO - TEMPORARY (START)
        Button sampleMessageButton = findViewById(R.id.test_sample_message);
        Button sampleTaskButton = findViewById(R.id.test_sample_task);

        sampleMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.database.messageDao().addNewMessage(2, "This is a sample message.");
                MainActivity.database.groupMessageDao().addNewGroupMessage(1, 3);

                alertMessage("Sample message added.");
            }
        });

        sampleTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.database.taskDao().createNewTask(2, "Sample Task", "This is a sample task.");
                MainActivity.database.groupTaskDao().addGroupTask(1, 3);

                alertMessage("Sample task added.");
            }
        });

         TODO - TEMPORARY (END) */
    }

    // Based on the option selected in the 'Spinner' object, display the corresponding fragment on screen
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String text = adapterView.getItemAtPosition(position).toString();

        if (text.equals("Homepage")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, homepageFragment).commit();
        }
        else if (text.equals("Add Members")) {
            // Only allow a user to add new members if he/she is the group owner
            if (isOwner != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, addMembersFragment).commit();
            } else {
                alertMessage("You do not have permission to add new members.");
            }
        } else if (text.equals("Add New Task")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, addTaskFragment).commit();
        } else if (text.equals("Completed Tasks")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, completeTaskFragment).commit();
        } else if (text.equals("Edit Group Name")) {
            if (isOwner != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, editNameFragment).commit();

            } else {
                alertMessage("You do not have permission to edit the group name.");
            }
        } else if (text.equals("Group Members")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, memberListFragment).commit();
        } else if (text.equals("Incomplete Tasks")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, incompleteTaskFragment).commit();
        } else if (text.equals("Messages")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, messageFragment).commit();
        } else if (text.equals("View All Groups")) {
            Context context = view.getContext();

            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.putExtra("id", userID);
            intent.putExtra("username",  MainActivity.database.userDao().getUserByID(userID).username);

            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        getSupportFragmentManager().beginTransaction().replace(R.id.group_fragment_container, homepageFragment).commit();
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
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