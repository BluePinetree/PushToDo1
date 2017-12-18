package com.bluepinetree.pushtodo;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Su on 2017-12-11.
 */

public class RemoteData {
    public static ArrayList<HashMap<String,String>> remoteDataStructure;
    public RemoteData(){
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    remoteDataStructure = new ArrayList<>();
                }
                else{
                    remoteDataStructure = (ArrayList<HashMap<String,String>>) dataSnapshot.getValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void commit(){
        FirebaseDatabase.getInstance().getReference().setValue(remoteDataStructure);
    }
}
