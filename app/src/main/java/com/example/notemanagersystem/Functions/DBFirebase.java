package com.example.notemanagersystem.Functions;


import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.notemanagersystem.MainActivity;
import com.example.notemanagersystem.Table.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DBFirebase {
    boolean isOK = false;
    public boolean checkEmailExist(String email){
        Query query = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    isOK = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        if (isOK == true)
            return true;
        return false;
    }
}