package com.berstek.veripay.data_access;

import com.google.firebase.database.Query;

public class ContactDA extends DA {

    private final String node = "contacts";

    public Query queryuserContactsByUID(String uid) {
        return rootRef.child(node).child(uid);
    }

    public void addContact(String contact_uid, String user_uid) {
        rootRef.child(node).child(user_uid).child(contact_uid).setValue(System.currentTimeMillis());
    }

    public void deleteContact(String contact_uid, String user_uid) {
        rootRef.child(node).child(user_uid).child(contact_uid).setValue(null);
    }
}
