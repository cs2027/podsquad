package com.song.christopher.podsquad.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Table modeling which users belong to which groups
@Entity(tableName = "group_users")
public class GroupUser {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "group_id")
    public int group_id;

    @ColumnInfo(name = "user_id")
    public int userID;

    // 1 = User is owner of current group, 0 = user is not owner of current group
    @ColumnInfo(name= "is_owner")
    public int isOwner;
}