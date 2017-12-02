package com.berstek.veripay.data_access;

import com.berstek.veripay.models.Feedback;
import com.berstek.veripay.models.Transaction;
import com.google.firebase.database.Query;

public class TransactionDA extends DA {

    private final String node = "transactions";

    public void addTransaction(Transaction transaction) {
        rootRef.child(node).push().setValue(transaction);
    }

    public void updateTransction(String transactionUID, Transaction.Status status) {
        rootRef.child(node).child(transactionUID).child("status").
                setValue(status);
    }

    public Query queryTransactionsBySender(String senderUID) {
        return rootRef.child(node).orderByChild("sender_uid").equalTo(senderUID);
    }

    public Query queryTransactionsByReceiver(String receiverUID) {
        return rootRef.child(node).orderByChild("receiver_uid").equalTo(receiverUID);

    }

    public void setFeedbackForTransaction(String transaction_uid, Feedback feedback) {
        rootRef.child(node).child(transaction_uid).child("feedback").setValue(feedback);
    }
}
