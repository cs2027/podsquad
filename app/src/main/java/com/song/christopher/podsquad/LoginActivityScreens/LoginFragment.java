package com.song.christopher.podsquad.LoginActivityScreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// TODO
import org.apache.commons.codec.binary.Hex;

// Fragment displaying the login page for a user
public class LoginFragment extends Fragment {
    // Buttons to navigate between fragments
    // Field to hold the register page to add a new user
    private Button login_button;
    private Button initial_register_button;
    private RegisterFragment registerPage;

    // This method takes the XML layout file and creates its corresponding (child) view objects
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (i.e. converts the layout file to a [set of] view objects)
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // Called once the child views for this fragment have been created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize our aforementioned fields
        login_button = (Button) getActivity().findViewById(R.id.login_button);
        initial_register_button = (Button) getActivity().findViewById(R.id.initial_register_button);
        registerPage = new RegisterFragment();

        // Go here when the user attempts to login to his/her account
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Parse and obtain the username and password inputted by the current user
                // TODO: Error (1)
                EditText usernameField = getActivity().findViewById(R.id.username_input);
                String username = usernameField.getText().toString();
                EditText passwordField = getActivity().findViewById(R.id.password_input);
                String password_unhashed = passwordField.getText().toString();
                String password_hashed = "";

                // Hash the user's inputted password
                try {
                    password_hashed = passwordHash(password_unhashed);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                // Check to see if there exists a user with the inputted credentials
                User user = MainActivity.database.userDao().checkDatabase(username, password_hashed);

                // Allow the user the login if their credentials were valid OR display an error message otherwise
                if(user != null) {
                    // Go here for valid login credentials; obtain and store the user's id, username, and password
                    int current_id = user.id;
                    String current_username = user.username;
                    String current_password = user.password;
                    Context context = getContext();

                    // Create a new Intent to move to the 'Welcome Page', passing in user data via the Intent
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("id", current_id);
                    intent.putExtra("username", current_username);
                    intent.putExtra("password", current_password);

                    // Update the user's login status to true (indicating that he/she is logged in)
                    MainActivity.database.userDao().loginStatus(current_id, true);

                    // Start the new activity (the welcome page)
                    context.startActivity(intent);
                } else {
                    alertMessage("Sorry, your login credentials were incorrect. Please try again.");
                }
            }
        });

        // Button to toggle from the login page fragment to the register page fragment
        initial_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, registerPage).commit();
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