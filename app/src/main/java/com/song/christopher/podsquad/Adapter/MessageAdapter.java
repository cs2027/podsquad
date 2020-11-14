package com.song.christopher.podsquad.Adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.song.christopher.podsquad.Database.Message;
import com.song.christopher.podsquad.LoginActivityScreens.MainActivity;
import com.song.christopher.podsquad.R;

import java.util.ArrayList;
import java.util.List;

// Adapter class to display all message in a given group
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    /* Fields to store the following data:
     * (1) user and group ID
     * (2) the message list for the current group */
    private int userID;
    private int groupID;
    private List<Integer> messageIDList = new ArrayList<>();
    private List<Message> messageList = new ArrayList<>();

    // Constructor for the adapter class; initializes user and group ID fields
    public MessageAdapter(int userID, int groupID) {
        this.userID = userID;
        this.groupID = groupID;
    }

    // Constructor for the ViewHolder object that encapsulates a single message
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        // Child views in the corresponding XML file <message_row.xml>
        LinearLayout containerView;
        TextView textView_Contents;
        TextView textView_Username;

        // Constructor for the 'ViewHolder' object
        MessageViewHolder(View view) {
            super(view);

            // Initialize the child views from the corresponding XML layout file
            containerView = view.findViewById(R.id.message_row);
            textView_Contents = view.findViewById(R.id.message_row_contents);
            textView_Username = view.findViewById(R.id.message_row_username);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_row, parent, false);

        return new MessageViewHolder(view);
    }

    // Binds data about a specific message to its corresponding view in the layout file
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // List of all messages for the current group
        messageList = MainActivity.database.messageDao().getMessagesByID(messageIDList);

        Log.e("cs50", "<onBindViewHolder> The size of 'messageList' is: " + messageList.size());

        // Current message in this^^ list
        Message currentMessage = messageList.get(position);

        // Obtain & display data information about the current message
        String username = MainActivity.database.userDao().getUserByID(currentMessage.user_id).username;

        holder.textView_Contents.setText(currentMessage.contents);
        holder.textView_Username.setText(username);

        if (currentMessage.user_id == userID) {
            holder.containerView.setGravity(Gravity.RIGHT);
        } else {
            holder.containerView.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        Log.e("cs50", "<getItemCount> The size of 'messageIDList' is: " + messageIDList.size());

        return messageIDList.size();
    }

    // Reloads the list of messages & notifies the corresponding RecyclerView of changes
    public void reloadMessages(List<Integer> messageIDList) {
        this.messageIDList = messageIDList;
        messageList = MainActivity.database.messageDao().getMessagesByID(messageIDList);

        Log.e("cs50", "<reloadMessages> The size of 'messageIDList' is: " + messageIDList.size());
        Log.e("cs50", "<reloadMessages> The size of 'messageList' is: " + messageList.size());

        notifyDataSetChanged();
    }
}
