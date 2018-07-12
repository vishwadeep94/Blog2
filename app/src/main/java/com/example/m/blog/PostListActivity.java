package com.example.m.blog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class    PostListActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabse;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        //declaring the databse reference object, this is what we use to access the database

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabse.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        setUserID(user.getUid());
        System.out.println("######## USER ID: " + getUserID() + " ########");


    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
