package com.song.christopher.podsquad.GroupActivityScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.song.christopher.podsquad.Database.Group;
import com.song.christopher.podsquad.ViewModel.GroupNameViewModel;
import com.song.christopher.podsquad.ViewModel.GroupNameViewModelFactory;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

// TODO: Prevent an empty group name?
// Fragment that allows a group's owner to edit the group name
public class EditNameFragment extends Fragment {
    /* Fields to store:
     * (1) the current group and user IDs
     * (2) the child views in the XML layout file */
    private int groupID; // (1)
    private int userID;
    private EditText groupNameContents; // (2)
    private Button updateNameButton;

    // 'ViewModel' object allows us to track live data (i.e. the current group name)
    // 'Factory' object allows us to pass the current groupID to filter the live data appropriately
    private GroupNameViewModel groupNameViewModel;
    private GroupNameViewModelFactory factory;

    // Constructor: initializes the group and user ID fields
    public EditNameFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the current 'Group' object and its group name
        Group currentGroup = MainActivity.database.groupDao().getGroupByID(groupID);
        String groupName = currentGroup.name;

        // Obtain the relevant child views from the XML layout file
        groupNameContents = view.findViewById(R.id.group_name_contents);
        updateNameButton = view.findViewById(R.id.update_group_name);

        // // Initialize the 'Factory' and 'ViewModel' objects to monitor the live data regarding the current group name
        factory = new GroupNameViewModelFactory(getActivity().getApplication(), groupID);
        groupNameViewModel = ViewModelProviders.of(this, factory).get(GroupNameViewModel.class);
        groupNameViewModel.getGroupName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                groupNameContents.setText(s);
            }
        });

        // When this 'update' button is clicked, update the group name
        updateNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Updates the database to store the new group name
                String newGroupName = groupNameContents.getText().toString();
                MainActivity.database.groupDao().editGroupName(newGroupName, groupID);

                // Create an intent to reload the 'GroupActivity' screen, passing in the current group and user ID
                Intent intent = new Intent(getActivity(), GroupActivity.class);
                intent.putExtra("groupID", groupID);
                intent.putExtra("userID", userID);
                startActivity(intent);

                // Display a message saying that the group name was updated.
                alertMessage("Group name has been updated!");
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