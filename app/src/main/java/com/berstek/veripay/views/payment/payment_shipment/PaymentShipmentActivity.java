package com.berstek.veripay.views.payment.payment_shipment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.berstek.veripay.R;
import com.berstek.veripay.callbacks.ConfirmationDialogListener;
import com.berstek.veripay.data_access.TransactionDA;
import com.berstek.veripay.models.Transaction;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.home.HomeActivity;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;

public class PaymentShipmentActivity extends AppCompatActivity
        implements PSPage1.PSPage1Listener, PSPage2.OnPage2ReadyListener,
        ConfirmationDialogListener {

    /*
    PAYMENT FLOW
    1. Select recipient
    2. Title, details and amount (PSPage1)
    3. Courier and due date (PSPage2)
    4. Confirmation Dialog
     */

    private TransactionDA transactionDA;
    private Transaction transaction;
    private String receiver_uid;
    private PSConfirmationDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_shipment);

        transactionDA = new TransactionDA();

        receiver_uid = getIntent().getExtras().getString("receiver_uid");

        transaction = new Transaction();
        transaction.setSender_uid(UserUtils.getUID());
        transaction.setStatus(Transaction.Status.AWAITING_ACCEPTANCE);
        transaction.setCreation_date(System.currentTimeMillis());
        //default due date is the same day the payment is made
        transaction.setExpiration_date(transaction.getCreation_date());
        transaction.setReceiver_uid(receiver_uid);
        transaction.setStatus(Transaction.Status.AWAITING_ACCEPTANCE);
        //default courier
        transaction.setCourier(Transaction.Courier.LBC);

        PSPage1 page1 = new PSPage1();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_payment, page1).commit();
    }

    @Override
    public void onPage1Ready(String title, String details, String amount,
                             ArrayList<String> imgURLs) {
        transaction.setTitle(title);
        transaction.setDetail(details);
        transaction.setAmount(Double.parseDouble(amount));
        transaction.setImg_urls(imgURLs);
        PSPage2 page2 = new PSPage2();
        Bundle bundle = new Bundle();
        ArrayList transactions = new ArrayList<>();
        transactions.add(transaction);
        bundle.putParcelableArrayList("transactions", transactions);
        page2.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_payment, page2).
                addToBackStack(null).commit();
    }

    @Override
    public void onPage2Ready(Transaction transaction) {
        dialogFragment = new PSConfirmationDialogFragment();
        ArrayList transactions = new ArrayList();
        transactions.add(transaction);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("transactions", transactions);
        dialogFragment.setArguments(bundle);

        dialogFragment.setDialogListener(this);
        dialogFragment.show(getFragmentManager(), null);
    }

    @Override
    public void onAgree() {
        transaction.setTransaction_code(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
        transactionDA.addTransaction(transaction);
        dialogFragment.dismiss();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCancel() {
        dialogFragment.dismiss();
    }
}
