package com.song.christopher.podsquad.GroupActivityScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.song.christopher.podsquad.Adapter.MessageAdapter;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.ViewModel.MessageViewModel;
import com.song.christopher.podsquad.ViewModel.MessageViewModelFactory;
import com.song.christopher.podsquad.R;

import java.util.List;

// Fragment to display all messages of a given group
public class MessageFragment extends Fragment {
    // Fields to store current group and user IDs
    private int groupID;
    private int userID;

    // Fields related to the 'RecyclerView' object to display all of a group's messages
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // Child views used to help add a new message (see the <fragment_message.xml> layout file)
    private EditText newMessageContent;
    private Button newMessageButton;

    // 'ViewModel' object allows us to track live data (i.e. the messages in the current group)
    // 'Factory' object allows us to pass the current groupID to filter the live data appropriately
    private MessageViewModel messageViewModel;
    private MessageViewModelFactory factory;

    // Constructor: initializes group and user ID number fields
    public MessageFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sets up the 'RecyclerView' object to display all messages
        recyclerView = view.findViewById(R.id.message_list);
        adapter = new MessageAdapter(userID, groupID);
        layoutManager = new LinearLayoutManager(getContext());

        /* Initialize the 'Factory' and 'ViewModel' objects to monitor the live data regarding
         * message in the current group */
        factory = new MessageViewModelFactory(getActivity().getApplication(), groupID);
        messageViewModel = ViewModelProviders.of(this, factory).get(MessageViewModel.class);
        messageViewModel.getAllGroupMessageIDs().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                adapter.reloadMessages(integers);

                Log.e("cs50", "Message have been reloaded!");
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize the child views used to help add a new message
        newMessageContent = view.findViewById(R.id.new_message_content);
        newMessageButton = view.findViewById(R.id.new_message_send);

        // Go here to add a new message (when the "Add New Message" button is clicked)
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contents = newMessageContent.getText().toString();

                // Make sure the new message is not blank/empty
                if (contents.trim().isEmpty() || contents == null) {
                    alertMessage("You cannot send an empty message.");
                } else {
                    MainActivity.database.messageDao().addNewMessage(userID, contents);

                    int messageID = MainActivity.database.messageDao().latestMessageID(userID, contents);
                    MainActivity.database.groupMessageDao().addNewGroupMessage(groupID, messageID);

                    newMessageContent.setText("");
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