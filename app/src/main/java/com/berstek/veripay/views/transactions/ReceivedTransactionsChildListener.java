package com.berstek.veripay.views.transactions;

import com.berstek.veripay.callbacks.ChildEventCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ReceivedTransactionsChildListener implements ChildEventListener {

    private ChildEventCallback childEventCallback;

    public void setChildEventCallback(ChildEventCallback childEventCallback) {
        this.childEventCallback = childEventCallback;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        childEventCallback.onChildAdded(dataSnapshot);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        childEventCallback.onChildChanged(dataSnapshot);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
