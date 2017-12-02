package com.berstek.veripay.callbacks;

import com.google.firebase.database.DataSnapshot;

public interface ChildEventCallback {
    void onChildAdded(DataSnapshot dataSnapshot);

    void onChildChanged(DataSnapshot dataSnapshot);
}

