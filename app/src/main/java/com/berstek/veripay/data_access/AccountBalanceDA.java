package com.berstek.veripay.data_access;

import com.berstek.veripay.models.TransactionHistory;
import com.google.firebase.database.Query;

public class AccountBalanceDA extends DA {

    private final String node = "transaction_history";

    public void pushTransaction(TransactionHistory transactionHistory, String userUID) {
        rootRef.child(node).child(userUID).push().setValue(transactionHistory);
    }

    public Query getTransactionHistory(String uid) {
        return rootRef.child(node).child(uid);
    }
}
