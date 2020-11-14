package com.song.christopher.podsquad.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Table identifying which tasks belong to which groups
@Entity(tableName = "group_tasks")
public class GroupTask {
    @PrimaryKey
    public int id;

    @ColumnInfo(name="group_id")
    public int group_id;

    @ColumnInfo(name="task_id")
    public int task_id;

    // 1 = completed task, 0 = incomplete task
    @ColumnInfo(name="task_status")
    public int task_status;
}
