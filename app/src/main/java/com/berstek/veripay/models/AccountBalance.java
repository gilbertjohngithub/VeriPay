package com.berstek.veripay.models;

public class AccountBalance {

    private double accountBalance = 0;

    public void pushTransaction(TransactionHistory t) {
        if (t.getType() == TransactionHistory.CreditDebit.CREDIT)
            accountBalance += t.getAmount();
        else
            accountBalance -= t.getAmount();
    }

    public double getAccountBalance() {
        return accountBalance;
    }
}
