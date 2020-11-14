package com.song.christopher.podsquad.LoginActivityScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.song.christopher.podsquad.Database.AppDatabase;
import com.song.christopher.podsquad.R;

public class MainActivity extends AppCompatActivity {
    // Fields to store the login page fragment & the database for our app
    private LoginFragment loginPage;
    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build our app's database
        database = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "notes")
                .allowMainThreadQueries()
                .build();

        // Display the login page in our main activity's XML file
        loginPage = new LoginFragment();
        View loginView = getLayoutInflater().inflate(R.layout.fragment_login, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, loginPage).commit();

        /*TODO: TEMPORARY (START)
        Button clearData = (Button) findViewById(R.id.test_button_delete);
        Button sampleData = (Button) findViewById(R.id.test_button_populate);

        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.groupDao().deleteAllGroups();
                database.groupMessageDao().deleteAllGroupMessages();
                database.groupTaskDao().deleteAllGroupTasks();
                database.groupUserDao().deleteAllGroupUsers();
                database.messageDao().deleteAllMessages();
                database.taskDao().deleteAllTasks();
                database.userDao().deleteAllUsers();

                alertMessage("Database has been cleared.");
            }
        });

        sampleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.userDao().addUserOne();
                database.userDao().addUserTwo();

                database.groupDao().createGroup(1, "TamFam");
                database.groupUserDao().addOwner(1, 1);
                database.groupUserDao().addUser(1, 2);

                database.groupDao().createGroup(2, "CS 211");
                database.groupUserDao().addOwner(2, 2);
                database.groupUserDao().addUser(2, 1);

                database.taskDao().createNewTask(
                        1,
                        "Dishwashing",
                        "Can somebody wash the dishes later today? Thanks!");
                database.groupTaskDao().addGroupTask(1, 1);
                database.messageDao().addNewMessage(1, "Hi everyone! Can you see this message?");
                database.groupMessageDao().addNewGroupMessage(1, 1);

                database.taskDao().createNewTask(
                        2,
                        "Assignment #4 Help",
                        "Can somebody help me with Section 3 - I'm a bit confused.");
                database.groupTaskDao().addGroupTask(2, 2);
                /*database.messageDao().addNewMessage(2, "Can someone respond to see if this chat is working.");
                database.groupMessageDao().addNewGroupMessage(2, 2);

                alertMessage("Sample data has been added.");
            }
        }); */
    }

    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    // TODO: TEMPORARY (END)
}