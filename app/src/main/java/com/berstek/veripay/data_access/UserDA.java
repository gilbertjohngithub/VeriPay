package com.berstek.veripay.data_access;

import com.berstek.veripay.models.User;
import com.google.firebase.database.Query;


public class UserDA extends DA {

    private final String node = "users";

    public Query queryUserByUID(String uid) {
        return rootRef.child(node).orderByKey().equalTo(uid);
    }

    public Query queryUserByPayID(String payID) {
        return rootRef.child(node).orderByChild("pay_id").equalTo(payID);
    }

    public void addUser(String uid, User user) {
        rootRef.child(node).child(uid).setValue(user);
    }

    public void updateUser(User user) {
        rootRef.child(node).child(user.getKey()).setValue(user);
    }
}
