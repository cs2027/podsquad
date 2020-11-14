package com.song.christopher.podsquad.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.song.christopher.podsquad.Database.GroupUser;

import java.util.List;

// Methods to interact with the 'group_users' table
// This^^ table tells which users belong to which groups
@Dao
public interface GroupUserDao {
    // Adds a user (a non-owner) to a group
    @Query("INSERT INTO group_users (group_id, user_id, is_owner) VALUES (:groupID, :userID, 0)")
    void addUser(int groupID, int userID);

    // Adds an owner to a group
    @Query("INSERT INTO group_users (group_id, user_id, is_owner) VALUES (:groupID, :userID, 1)")
    void addOwner(int groupID, int userID);

    // Determines whether a given user is (already) in a group
    @Query("SELECT * FROM group_users WHERE group_id=:groupID AND user_id=:userID AND is_owner=0")
    GroupUser isUser(int groupID, int userID);

    // Determines whether a given user is the owner of a given group
    @Query("SELECT * FROM group_users WHERE group_id=:groupID AND user_id=:userID AND is_owner=1")
    GroupUser isOwner(int groupID, int userID);

    // Removes a user from a group
    @Query("DELETE FROM group_users WHERE group_id=:groupID AND user_id=:userID")
    void removeUser(int groupID, int userID);

    // Selects a list of all group IDs that the given user belongs to
    @Query("SELECT group_id FROM group_users WHERE user_id=:userID")
    LiveData<List<Integer>> getGroupIDs(int userID);

    // Obtains all ID numbers of users in a given group
    @Query("SELECT user_id FROM group_users WHERE group_id=:groupID")
    LiveData<List<Integer>> getUserIDs(int groupID);

    // TODO: TEMPORARY
    @Query("DELETE FROM group_users")
    void deleteAllGroupUsers();

    // TODO: TEMPORARY
    @Query("DELETE FROM group_users WHERE is_owner=0")
    void deleteAllNonOwners();
}
