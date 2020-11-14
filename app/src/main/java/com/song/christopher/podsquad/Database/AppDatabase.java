package com.song.christopher.podsquad.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.song.christopher.podsquad.Dao.GroupDao;
import com.song.christopher.podsquad.Dao.GroupMessageDao;
import com.song.christopher.podsquad.Dao.GroupTaskDao;
import com.song.christopher.podsquad.Dao.GroupUserDao;
import com.song.christopher.podsquad.Dao.MessageDao;
import com.song.christopher.podsquad.Dao.TaskDao;
import com.song.christopher.podsquad.Dao.UserDao;

// Database for our app
// TODO: Error (5)
@Database(entities = {User.class, Group.class, Task.class, Message.class, GroupUser.class, GroupTask.class, GroupMessage.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    // Methods to interact with the 'users' table
    public abstract UserDao userDao();

    // Methods to interact with the 'groups' table
    public abstract GroupDao groupDao();

    // Methods to interact with the 'tasks' table
    public abstract TaskDao taskDao();

    // Methods to interact with the 'messages' table
    public abstract MessageDao messageDao();

    // Methods to interact with the 'group_users' table
    // Table that stores which users belong to which group
    public abstract GroupUserDao groupUserDao();

    // Methods to interact with the 'group_tasks' table
    // Table that stores which tasks belong to which group
    public abstract GroupTaskDao groupTaskDao();

    // Methods to interact with the 'group_messages' table
    // Table that stores which messages belong to which group
    public abstract GroupMessageDao groupMessageDao();
}
