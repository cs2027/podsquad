package com.song.christopher.podsquad.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Table identifying which messages belong to which groups
@Entity(tableName = "group_messages")
public class GroupMessage {
    @PrimaryKey
    public int id;

    @ColumnInfo(name="group_id")
    public int group_id;

    @ColumnInfo(name="message_id")
    public int message_id;
}
