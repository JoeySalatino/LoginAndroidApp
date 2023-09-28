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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
        FirebaseUser firebaseUser = auth.getCurrentUser();
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
                    updateUserUsernameAuthentication(textName,firebaseUser);
                    updateUserEmailAuthentication(textEmail,firebaseUser);
                    updateUserPasswordAuthentication(textPassword,firebaseUser);
                    updateUserDatabase(firebaseUser);
                }
                Intent userUpdateButton = new Intent(UpdatePageActivity.this, UserProfileActivity.class);
                startActivity(userUpdateButton);
                finish();
            }
        });
    }
    private void updateUserDatabase(FirebaseUser firebaseUser) {
        database.child("users").child(firebaseUser.getUid()).child("Username").setValue(editTextUpdateName.getText().toString());
        database.child("users").child(firebaseUser.getUid()).child("Email").setValue(editTextUpdateEmail.getText().toString());
        database.child("users").child(firebaseUser.getUid()).child("Password").setValue(editTextUpdatePassword.getText().toString());
    }
    private void setUserInfo(FirebaseUser firebaseUser){
        database.child("users").child(firebaseUser.getUid()).child("Username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editTextUpdateName.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdatePageActivity.this, "Couldnt update profile", Toast.LENGTH_SHORT).show();
            }
        });
        database.child("users").child(firebaseUser.getUid()).child("Email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editTextUpdateEmail.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdatePageActivity.this, "Couldnt update profile", Toast.LENGTH_SHORT).show();
            }
        });
        database.child("users").child(firebaseUser.getUid()).child("Password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editTextUpdatePassword.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdatePageActivity.this, "Couldnt update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUserUsernameAuthentication(String textName, FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
        user.updateProfile(profileUpdates);
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdatePageActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserEmailAuthentication(String textEmail, FirebaseUser user) {
        user.updateEmail(textEmail);
        user.updateEmail(textEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdatePageActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserPasswordAuthentication(String textPassword, FirebaseUser user) {
        user.updatePassword(textPassword);
        user.updatePassword(textPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdatePageActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void findViews() {
        editTextUpdateName = findViewById(R.id.editText_update_name);
        editTextUpdateEmail = findViewById(R.id.editText_update_email);
        editTextUpdatePassword = findViewById(R.id.editText_update_pwd);
        editTextWelcome = findViewById(R.id.textView_update_welcome);
        progressBar = findViewById(R.id.progressBar);
    }
}
