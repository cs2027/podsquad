package com.song.christopher.podsquad.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

// Methods to interact with the 'group_messages' table
// Table that identifies which messages correspond with which groups
@Dao
public interface GroupMessageDao {
    // Add a new 'GroupMessage' object
    @Query("INSERT INTO group_messages (group_id, message_id) VALUES (:groupID, :messageID)")
    void addNewGroupMessage(int groupID, int messageID);

    // Obtain all message IDs belonging to a given group
    @Query("SELECT message_id FROM group_messages WHERE group_id=:groupID")
    LiveData<List<Integer>> getAllMessagesIDs(int groupID);

    // TODO: Temporary
    @Query("DELETE FROM group_messages")
    void deleteAllGroupMessages();
}
