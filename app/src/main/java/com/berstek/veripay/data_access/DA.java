package com.berstek.veripay.data_access;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DA {

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot();
}
