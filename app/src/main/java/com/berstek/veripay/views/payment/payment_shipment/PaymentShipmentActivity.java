package com.berstek.veripay.views.payment.payment_shipment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.berstek.veripay.R;
import com.berstek.veripay.callbacks.ConfirmationDialogListener;
import com.berstek.veripay.data_access.TestDA;
import com.berstek.veripay.data_access.TransactionDA;
import com.berstek.veripay.models.Transaction;
import com.berstek.veripay.utils.FileUploader;
import com.berstek.veripay.utils.IntentMarker;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.home.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;


import junit.framework.Test;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PaymentShipmentActivity extends AppCompatActivity
        implements PSPage1.PSPage1Listener, PSPage2.OnPage2ReadyListener,
        ConfirmationDialogListener, PSPage1.OnUploadStartedListener {

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

    private UploadTask uploadTask;
    private FileUploader uploader;
    private ArrayList<String> imgUrls;
    private PSPage1 page1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_shipment);

        imgUrls = new ArrayList<>();

        transactionDA = new TransactionDA();
        uploader = new FileUploader();

        receiver_uid = getIntent().getExtras().getString("receiver_uid");

        transaction = new Transaction();
        transaction.setSender_uid(UserUtils.getUID());
        transaction.setStatus(Transaction.Status.AWAITING_ACCEPTANCE);
        transaction.setCreation_date(System.currentTimeMillis());
        //default due date is the same day the payment is made
        transaction.setExpiration_date(transaction.getCreation_date());
        transaction.setReceiver_uid(receiver_uid);
        transaction.setImg_urls(imgUrls);
        transaction.setStatus(Transaction.Status.AWAITING_ACCEPTANCE);
        //default courier
        transaction.setCourier(Transaction.Courier.LBC);

        page1 = new PSPage1();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.activity_payment, page1).commit();
    }

    @Override
    public void onPage1Ready(String title, String details, String amount) {
        transaction.setTitle(title);
        transaction.setDetail(details);
        transaction.setAmount(Double.parseDouble(amount));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        new TestDA().writeToConsole1("UPLOADING FILE NICE");

        if (requestCode == IntentMarker.RC_OPEN_FILE) {
            ContentResolver cr = getContentResolver();
            InputStream is;
            try {
                is = cr.openInputStream(data.getData());
                final double size = is.available();

                uploadTask = uploader.uploadFile
                        (data, PaymentShipmentActivity.this);

                if (uploadTask != null) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadURL = snapshot.getDownloadUrl();
                            imgUrls.add(downloadURL.toString());
                            transaction.setImg_urls(imgUrls);
                            new TestDA().writeToConsole1(downloadURL.toString());
                            new TestDA().writeToConsole1(transaction.getImg_urls().size());

                            if (page1 != null) {
                                page1.onImageUploaded(transaction.getImg_urls());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new TestDA().writeToConsole1("UPLOAD FAILED");
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot snapshot) {
                            @SuppressWarnings("VisibleForTests")
                            long bytesTransferred = snapshot.getBytesTransferred();
                            double percentage = (bytesTransferred / size) * 100;
                            DecimalFormat decimalFormat = new DecimalFormat("0");

                            if (page1 != null) {
                                page1.onProgressUpdate(decimalFormat.format(percentage) + "%");
                            }
                        }
                    });

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUploadStarted() {
        uploader.openFileChooser(this);
    }

}
