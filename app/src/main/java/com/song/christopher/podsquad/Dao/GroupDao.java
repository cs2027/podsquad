package com.song.christopher.podsquad.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.song.christopher.podsquad.Database.Group;

import java.util.List;

// Methods to interact with the 'group' table
@Dao
public interface GroupDao {
    // Creates a new group
    @Query("INSERT INTO groups (owner_id, name) VALUES (:ownerID, :name)")
    void createGroup(int ownerID, String name);

    // Returns the ID of the newest group (used when a group is first created)
    // TODO: Error (5)
    @Query("SELECT id FROM groups WHERE owner_id=:ownerID AND name=:name ORDER BY id DESC LIMIT 1")
    int latestGroupID(int ownerID, String name);

    // Returns a 'Group' object given its ID number
    @Query("SELECT * FROM groups WHERE id=:id")
    Group getGroupByID(int id);

    // Obtains group name based on a group's corresponding ID number
    @Query("SELECT name FROM groups WHERE id=:id")
    LiveData<String> getGroupNameByID(int id);

    // Selects all groups from a given list of group IDs
    @Query("SELECT * FROM groups WHERE id IN (:groupIDs)")
    List<Group> getAllGroups(List<Integer> groupIDs);

    // Edits the name of a given group
    @Query("UPDATE groups SET name=:name WHERE id=:id")
    void editGroupName(String name, int id);

    // Select all groups from two input lists of group IDs
    @Query("SELECT * FROM groups WHERE id IN (:first) OR id IN (:second) ORDER BY id DESC")
    List<Group> combineGroupsByID(List<Integer> first, List<Integer> second);

    // TODO: TEMPORARY
    @Query("DELETE FROM groups")
    void deleteAllGroups();
}
