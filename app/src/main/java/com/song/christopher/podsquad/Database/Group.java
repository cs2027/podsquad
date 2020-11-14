package com.song.christopher.podsquad.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Table that stores information about groups
@Entity(tableName = "groups")
public class Group {
    // Group ID number
    @PrimaryKey
    public int id;

    // ID number of the group's owner
    @ColumnInfo(name="owner_id")
    public int owner_id;

    // Group name
    @ColumnInfo(name="name")
    public String name;
}


