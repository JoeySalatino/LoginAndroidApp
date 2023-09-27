package com.example.mobileapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UpdatePageActivity extends AppCompatActivity {
    private EditText editTextUpdateName, editTextUpdateEmail, editTextUpdatePassword;
    private TextView editTextWelcome;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page);
        getSupportActionBar().setTitle("Update Profile");
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        findViews();
        setUserInfo(firebaseUser);
        updateButton();
    }



    private void updateButton() {
        Button buttonUpdate = findViewById(R.id.button_updatePage_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextUpdateEmail.getText().toString();
                String textPassword = editTextUpdatePassword.getText().toString();
                String textName = editTextUpdateName.getText().toString();
                if(textPassword.length()<6) {
                    Toast.makeText(UpdatePageActivity.this, "PLease enter password longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextUpdatePassword.setError("Password is required to be more than 6 characters!");
                    editTextUpdatePassword.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    updateUserAuthentication(textEmail, textName, textPassword);
                    updateUserDatabase(textName,textEmail,textPassword);
            }
                Intent userUpdateButton = new Intent(UpdatePageActivity.this, UserProfileActivity.class);
                startActivity(userUpdateButton);
                finish();
            }
        });
    }

    private void updateUserAuthentication(String textEmail, String textName, String textPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdateName = new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
        user.updateProfile(profileUpdateName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdatePageActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        user.updateEmail(textEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdatePageActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        user.updatePassword(textPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdatePageActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

        public void updateUserDatabase(String textName, String textEmail, String textPassword){
            database.child("users").child("username").setValue(textName);
            database.child("users").child("email").setValue(textEmail);
            database.child("users").child("password").setValue(textPassword);
        }


    private void setUserInfo(FirebaseUser firebaseUser) {
        database.child("users").child(firebaseUser.getUid()).child("password").setValue(editTextUpdatePassword.getText().toString());
        database.child("users").child(firebaseUser.getUid()).child("password").setValue(editTextUpdatePassword.getText().toString());
        database.child("users").child(firebaseUser.getUid()).child("password").setValue(editTextUpdatePassword.getText().toString());
    }



    private void findViews() {
        editTextUpdateName = findViewById(R.id.editText_update_name);
        editTextUpdateEmail = findViewById(R.id.editText_update_email);
        editTextUpdatePassword = findViewById(R.id.editText_update_pwd);
        editTextWelcome = findViewById(R.id.textView_update_welcome);
        progressBar = findViewById(R.id.progressBar);
    }
}