package com.song.christopher.podsquad.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Table to store data about each user
@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "username")
    public String username;

    // This password is hashed before being stored in the database
    @ColumnInfo(name = "password")
    public String password;

    // True = user is logged in, false = user is not logged in
    @ColumnInfo(name = "login_status", defaultValue = "false")
    public boolean loginStatus;
}
