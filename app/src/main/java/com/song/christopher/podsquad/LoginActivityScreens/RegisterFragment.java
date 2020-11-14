package com.song.christopher.podsquad.LoginActivityScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.song.christopher.podsquad.Database.User;
import com.song.christopher.podsquad.R;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Fragment display the register page to add a new user
public class RegisterFragment extends Fragment {
    // Buttons to register a new user & return to login page
    // Fragment field to store the login page fragment
    private Button final_register_button;
    private Button to_login_page;
    private LoginFragment loginPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize our class's fields
        final_register_button = (Button) getActivity().findViewById(R.id.final_register_button);
        to_login_page = (Button) getActivity().findViewById(R.id.to_login_page);
        loginPage = new LoginFragment();

        // Go here when the user attempts to register a new account
        final_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain the user's desired username and password
                // TODO: Error (1)
                EditText usernameField = getActivity().findViewById(R.id.new_user_username);
                String username = usernameField.getText().toString();
                EditText passwordField = getActivity().findViewById(R.id.new_user_password);
                String password_unhashed = passwordField.getText().toString();
                EditText passwordConfirmField = getActivity().findViewById(R.id.password_confirm);
                String password_confirm = passwordConfirmField.getText().toString();
                String password_hashed = "";

                // Hash the user's inputted password
                try {
                    password_hashed = passwordHash(password_unhashed);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                // Check if the passwords match & if the username or password is already taken
                boolean passwordsMatch = password_unhashed.equals(password_confirm);
                User usernameRepeatCheck = MainActivity.database.userDao().checkUsernames(username);
                User passwordRepeatCheck = MainActivity.database.userDao().checkPasswords(password_hashed);

                // If the passwords don't match AND/OR if the username or password is taken, display an error message
                // Else, add the user to the database and return to the login page fragment
                if (password_unhashed == "") {
                    alertMessage("You cannot have a blank password.");
                } else if (! passwordsMatch) {
                    alertMessage("Sorry, your passwords do not match.");
                } else if (usernameRepeatCheck != null) {
                    alertMessage("Sorry, that username is already taken.");
                } else if (passwordRepeatCheck != null) {
                    alertMessage("Sorry, that password is already taken.");
                } else {
                    MainActivity.database.userDao().registerNewUser(username, password_hashed);
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, loginPage).commit();

                    alertMessage("Congrats! You have successfully registered for an account. " +
                            "You may now login with your username and password.");
                }
            }
        });


        // Button to switch from the register page fragment to the login page fragment
        to_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, loginPage).commit();
            }
        });
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // SHA-512 algorithm used to hash the user's inputted password
    public String passwordHash(String s) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(s.getBytes());
        byte[] output = messageDigest.digest();

        return Hex.encodeHexString(output);
    }
}