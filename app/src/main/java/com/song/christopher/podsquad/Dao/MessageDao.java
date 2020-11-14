package com.song.christopher.podsquad.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.song.christopher.podsquad.Database.Message;

import java.util.List;

// Methods to interact the with 'messages' table
@Dao
public interface MessageDao {
    // Adds a new message to the 'messages' table
    @Query("INSERT INTO messages (user_id, contents) VALUES (:userID, :contents)")
    void addNewMessage(int userID, String contents);

    // Selects a message object with a given ID number
    @Query("SELECT * FROM messages WHERE id=:id")
    Message getMessageByID(int id);
    
    // Selects a list of message objects from a list of ID numbers
    @Query("SELECT * FROM messages WHERE id IN (:messageIDs) ORDER BY id DESC")
    List<Message> getMessagesByID(List<Integer> messageIDs);

    // Selects all messages corresponding to a given user
    @Query("SELECT * FROM messages WHERE user_id=:userID")
    List<Message> getUserMessages(int userID);

    // TODO: Error (5)
    @Query("SELECT id FROM messages WHERE user_id=:userID AND contents=:contents ORDER BY id DESC LIMIT 1")
    int latestMessageID(int userID, String contents);

    // TODO: Temporary
    @Query("DELETE FROM messages")
    void deleteAllMessages();
}
