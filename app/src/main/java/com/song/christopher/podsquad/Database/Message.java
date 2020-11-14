package com.song.christopher.podsquad.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Table holding information regarding a specific message object
@Entity(tableName = "messages")
public class Message {
    @PrimaryKey
    public int id;

    @ColumnInfo(name="user_id")
    public int user_id;

    @ColumnInfo(name="contents")
    public String contents;
}
