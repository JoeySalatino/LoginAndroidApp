package com.example.mobileapps;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextRegisterEmail, editTextRegisterPwd, editTextRegisterFirstName, editTextRegisterLastName;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Sign Up!");
        database = FirebaseDatabase.getInstance().getReference();
        findViews();
        showHidePwd();
        Button ButtonRegister = findViewById(R.id.button_register);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextRegisterEmail.getText().toString();
                String textPassword = editTextRegisterPwd.getText().toString();
                String textFname = editTextRegisterFirstName.getText().toString();
                String textLname = editTextRegisterLastName.getText().toString();
                //Check to see if data is valid before registering the user
                if(TextUtils.isEmpty(textFname)){
                    Toast.makeText(RegisterActivity.this, "PLease enter your first name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFirstName.setError("First name is required!");
                    editTextRegisterFirstName.requestFocus();
                }else if(TextUtils.isEmpty(textLname)){
                    Toast.makeText(RegisterActivity.this, "PLease enter your last name", Toast.LENGTH_SHORT).show();
                    editTextRegisterLastName.setError("Last name is required!");
                    editTextRegisterLastName.requestFocus();
                }else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "PLease enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required!");
                    editTextRegisterEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this, "PLease enter your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is required!");
                    editTextRegisterPwd.requestFocus();
                }else if(textPassword.length()<6) {
                    Toast.makeText(RegisterActivity.this, "PLease enter password longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is required to be more than 6 characters!");
                    editTextRegisterPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textEmail, textFname, textLname, textPassword);
                    firebaseUser = auth.getCurrentUser();
                }
            }
            private void registerUser(String textEmail, String textFname, String textLname, String textPassword) {
                auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check to see if the user was created succesfully
                        FirebaseUser firebaseUser = null;
                        if(task.isSuccessful()){
                            firebaseUser = auth.getCurrentUser();
                        }
                        if(firebaseUser != null){
                            //update display name of the user
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textFname + textLname).build();
                            firebaseUser.updateProfile(profileUpdates);
                            Toast.makeText(RegisterActivity.this,"registration Successfull", Toast.LENGTH_SHORT).show();
                            writeNewUser(textEmail,textFname+textLname,textPassword);
                            //Open the UserProfileActivity after the user is created
                            Intent userProfileActivity = new Intent(RegisterActivity.this, UserProfileActivity.class);
                            //Stop the user from going back to the register screen
                            userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(userProfileActivity);
                            finish();
                        }else{
                            //handle the exceptions
                            try{
                                throw task.getException();


                            }catch(Exception e){
                                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
    }
    private void writeNewUser(String textEmail, String textName, String textPassword) {
        User user = new User(textName,textEmail, textPassword);
        firebaseUser = auth.getCurrentUser();
        database.child("users").child(""+user.getID());
        database.child("users").child(firebaseUser.getUid()).child("Username").setValue(textName);
        database.child("users").child(firebaseUser.getUid()).child("Email").setValue(textEmail);
        database.child("users").child(firebaseUser.getUid()).child("Password").setValue(textPassword);
    }
    private void showHidePwd() {
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.visibility);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if password is visible then hide it
                if (editTextRegisterPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextRegisterPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.visibility);
                } else {
                    editTextRegisterPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.visibilityoff);
                }
            }
        });
    }
    private void findViews() {
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterPwd = findViewById(R.id.editText_register_pwd);
        editTextRegisterFirstName = findViewById(R.id.editText_register_fname);
        editTextRegisterLastName = findViewById(R.id.editText_register_lname);
        progressBar = findViewById(R.id.progressBar);
    }
}
