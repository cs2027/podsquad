package com.song.christopher.podsquad.LoginActivityScreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.song.christopher.podsquad.Adapter.GroupAdapter;
import com.song.christopher.podsquad.R;
import com.song.christopher.podsquad.ViewModel.GroupViewModel;
import com.song.christopher.podsquad.ViewModel.GroupViewModelFactory;

import java.util.List;

// Welcome page when a user logs in successfully to his/her account
public class WelcomeActivity extends AppCompatActivity {
    /* Fields that store the following information:
     * (1) the welcome page's child views (excluding the RecyclerView)
     * (2) data associated with the RecyclerView object
     * (3) the current user's ID number */
    // TODO (Update)
    private TextView usernameDisplay; // (1)
    private EditText newGroupName;
    private Button toNewGroupButton;
    private Button logoutButton;
    private RecyclerView recyclerView; // (2)
    private GroupAdapter groupAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int userID; // (3)
    private String username;

    // 'ViewModel' object allows us to track live data (i.e. the groups the current user belongs to)
    // 'Factory' object allows us to pass the current groupID to filter the live data appropriately
    private GroupViewModel groupViewModel;
    private GroupViewModelFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // (1) Obtain the child views from the XML layout file (excluding the RecyclerView)
        // (2) Display the user's username (from the Intent) in the XML layout file
        Intent intent = getIntent();
        usernameDisplay = findViewById(R.id.user_username); // (1)
        newGroupName = findViewById(R.id.new_group_name);
        toNewGroupButton = findViewById(R.id.to_new_group_page);
        logoutButton = findViewById(R.id.logout_button);
        username = (String) intent.getStringExtra("username");
        usernameDisplay.setText(username); // (2)

        // Initialize all other fields (current user ID, RecyclerView objects)
        userID = (Integer) intent.getIntExtra("id", 0);
        recyclerView = findViewById(R.id.groups_recycler_view);
        groupAdapter = new GroupAdapter(userID);
        layoutManager = new LinearLayoutManager(this);

        /* Initialize the 'Factory' and 'ViewModel' objects to monitor the live data regarding
         * groups the current user belongs to */
        factory = new GroupViewModelFactory(getApplication(), userID);
        groupViewModel = ViewModelProviders.of(this, factory).get(GroupViewModel.class);
        groupViewModel.getAllGroupIDs().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                groupAdapter.reloadGroups(integers);
            }
        });

        // Setup the RecyclerView object
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

        // Button to create a new group
        // TODO (Update)
        toNewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, NewGroupActivity.class);
                intent.putExtra("id", userID);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        });

        // Log the user out of his/her account when the logout button is pressed
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
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
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