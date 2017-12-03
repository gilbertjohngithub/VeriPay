package com.berstek.veripay.models;

public class TransactionHistory {

    private String transaction_code;
    private double amount;
    private CreditDebit type;

    public enum CreditDebit {
        CREDIT, DEBIT
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CreditDebit getType() {
        return type;
    }

    public void setType(CreditDebit type) {
        this.type = type;
    }
}
