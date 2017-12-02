package com.berstek.veripay.utils;

import com.google.firebase.auth.FirebaseAuth;

public class UserUtils {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static String getUID() {
        return auth.getCurrentUser().getUid();
    }
}
