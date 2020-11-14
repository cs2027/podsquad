package com.song.christopher.podsquad.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.song.christopher.podsquad.Database.Task;

import java.util.List;

// Methods to interact with the 'tasks' table
@Dao
public interface TaskDao {
    // Create a new task
    @Query("INSERT INTO tasks (user_id, title, contents) VALUES (:userID, :title, :contents)")
    void createNewTask(int userID, String title, String contents);

    // Edit the title and/or contents of a given task
    @Query("UPDATE tasks SET title=:title, contents=:contents WHERE user_id=:userID AND id=:taskID")
    void editTask(int userID, String title, String contents, int taskID);

    // Delete a taski
    @Query("DELETE FROM tasks WHERE id=:taskID")
    void deleteTask(int taskID);

    // Select a 'Task' object by its ID number
    @Query("SELECT * FROM tasks WHERE id=:id")
    Task getTaskByID(int id);

    // Get all tasks posted by a given user
    @Query("SELECT * FROM tasks WHERE user_id=:userID")
    List<Task> getUserTasks(int userID);

    // Select a list of 'Task' objects given a set of ID numbers
    @Query("SELECT * FROM tasks WHERE id IN (:taskIDs) ORDER BY id DESC")
    List<Task> getTasksByIDs(List<Integer> taskIDs);

    // TODO: Error (5)
    @Query("SELECT id FROM tasks WHERE user_id=:userID AND title=:title AND contents=:contents ORDER BY id DESC LIMIT 1")
    int latestTaskID(int userID, String title, String contents);

    // TODO: Temporary
    @Query("DELETE FROM tasks")
    void deleteAllTasks();
}
