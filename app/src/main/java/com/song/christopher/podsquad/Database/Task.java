package com.song.christopher.podsquad.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Table holding information about tasks (both complete and incomplete)
@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey
    public int id;

    // ID number of user who posted a task
    @ColumnInfo(name="user_id")
    public int user_id;

    // Title of the task (displayed in the RecyclerView obj.)
    @ColumnInfo(name="title")
    public String title;

    // Contents of the task
    @ColumnInfo(name="contents")
    public String contents;
}
