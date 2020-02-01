package com.example.koray.capstoneproject.Listiners;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Koray on 20.12.2017.
 */

public interface CheckFirebaseData {
    public void onSuccess(DataSnapshot dataSnapshot, boolean status);
}
