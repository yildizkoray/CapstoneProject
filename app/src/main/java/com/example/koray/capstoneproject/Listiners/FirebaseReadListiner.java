package com.example.koray.capstoneproject.Listiners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Koray on 12.12.2017.
 */

public interface FirebaseReadListiner {
    public void onStart();

    public void onSuccess(DataSnapshot dataSnapshot);

    public void onFailed(DatabaseError databaseError);
}
