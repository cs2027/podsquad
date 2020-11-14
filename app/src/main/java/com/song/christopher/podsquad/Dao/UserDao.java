package com.song.christopher.podsquad.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.song.christopher.podsquad.Database.User;

import java.util.List;

// Methods to query the 'users' table in our database
@Dao
public interface UserDao {
    // Add a new user to our database
    @Query("INSERT INTO users (username, password) VALUES (:username, :password)")
    void registerNewUser(String username, String password);

    // Used to authenticate a user at login time
    @Query("SELECT * FROM users WHERE username=:username AND password=:password")
    User checkDatabase(String username, String password);

    // Used to see if a username is already taken (during registration)
    @Query("SELECT * FROM users WHERE username=:username")
    User checkUsernames(String username);

    // Used to see if a password if already taken (during registration)
    @Query("SELECT * FROM users WHERE password=:password")
    User checkPasswords(String password);

    // Update the login status of a user based on if he/she logs in or logs out
    @Query("UPDATE users SET (login_status) = (:status) WHERE id=:id ")
    void loginStatus(int id, boolean status);

    // Select the user with the given user ID
    @Query("SELECT * FROM users WHERE id=:id")
    User getUserByID(int id);

    // Select all users whose ID number is in a given list
    @Query("SELECT * FROM users WHERE id IN (:idList)")
    List<User> getUsersByID(List<Integer> idList);

    // Selects the user with a given username
    @Query("SELECT * FROM users WHERE username=:username")
    User getUserByUsername(String username);

    // Delete a user from the database
    @Query("DELETE FROM users WHERE id=:id")
    void deleteUser(int id);

    // TODO: TEMPORARY
    @Query("DELETE FROM users")
    void deleteAllUsers();

    @Query("INSERT INTO users (username, password) VALUES ('cs2027', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da')")
    void addUserOne();

    @Query("INSERT INTO users (username, password) VALUES ('cj7545', '92a891f888e79d1c2e8b82663c0f37cc6d61466c508ec62b8132588afe354712b20bb75429aa20aa3ab7cfcc58836c734306b43efd368080a2250831bf7f363f')")
    void addUserTwo();
}

