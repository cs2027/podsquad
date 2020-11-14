package com.song.christopher.podsquad.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

// Methods to interact with the 'group_tasks' table
// Table identifying which tasks correspond with which groups
@Dao
public interface GroupTaskDao {
    // Adds a new task for a given group
    @Query("INSERT INTO group_tasks (group_id, task_id, task_status) VALUES (:groupID, :taskID, 0)")
    void addGroupTask(int groupID, int taskID);

    // Delete a 'GroupTask' object
    @Query("DELETE FROM group_tasks WHERE group_id=:groupID AND task_id=:taskID")
    void deleteGroupTask(int groupID, int taskID);

    // Changes a task from incomplete to complete (or vice versa)
    @Query("UPDATE group_tasks SET task_status = :taskStatus WHERE task_id = :taskID")
    void changeTaskStatus(int taskID, int taskStatus);

    // Get all tasks from a given group
    @Query("SELECT task_id FROM group_tasks WHERE group_id=:groupID")
    List<Integer> getAllTasksIDs(int groupID);

    // Gets all complete tasks from a given group
    @Query("SELECT task_id FROM group_tasks WHERE group_id=:groupID AND task_status=1")
    LiveData<List<Integer>> getCompleteTaskIDs(int groupID);

    // Gets all incomplete tasks from a given group
    @Query("SELECT task_id FROM group_tasks WHERE group_id=:groupID AND task_status=0")
    LiveData<List<Integer>> getIncompleteTaskIDs(int groupID);

    // TODO: Temporary
    @Query("DELETE FROM group_tasks")
    void deleteAllGroupTasks();
}
