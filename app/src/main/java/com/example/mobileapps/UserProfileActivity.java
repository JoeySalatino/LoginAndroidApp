package com.example.mobileapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileapps.MainActivity2;
import com.example.mobileapps.R;
import com.example.mobileapps.UpdatePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewName, textViewEmail, textViewRegisterDate;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Home");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        findViews();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        showUserProfile1(firebaseUser);
        signOut();
        update();
    }
    public void showUserProfile1(FirebaseUser firebaseUser){
        database.child("users").child(firebaseUser.getUid()).child("Username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewName.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        database.child("users").child(firebaseUser.getUid()).child("Email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewEmail.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Couldnt update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {
        Button buttonSignOut = findViewById(R.id.button_sign_out);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfileActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                //clear the stack so user can't use the back arrow to get in
                Intent mainActivity = new Intent(UserProfileActivity.this, MainActivity2.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                finish();
            }
        });
    }

    private void update() {
        Button buttonUpdate = findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePage = new Intent(UserProfileActivity.this, UpdatePageActivity.class);
                startActivity(updatePage);
                finish();
            }
        });
    }

    private void findViews() {
        textViewName = findViewById(R.id.textView_show_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        progressBar = findViewById(R.id.progressBar);
    }
}