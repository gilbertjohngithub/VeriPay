package com.berstek.veripay.views.transactions;


import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.MainActivity;
import com.berstek.veripay.R;
import com.berstek.veripay.data_access.TransactionDA;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Transaction;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomImageUtils;
import com.berstek.veripay.utils.CustomUtils;
import com.berstek.veripay.utils.UserUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFullViewDialog extends DialogFragment implements View.OnClickListener {

    private View view;
    private ArrayList transactions;
    private Transaction transaction;

    private ImageView transaction_image;

    private TextView product_name, status, price,
            username, user_type, number, address,
            transaction_option, due_date, details;
    private ImageView back_img2;

    private ImageView cancel, accept, userImage;

    private TransactionDA transactionDA;

    public ItemFullViewDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fullview_dialogue, container, false);

        transactions = getArguments().getParcelableArrayList("transactions");
        transaction = (Transaction) transactions.get(0);

        transaction_image = view.findViewById(R.id.product_image);
        product_name = view.findViewById(R.id.product_name);
        status = view.findViewById(R.id.status);
        price = view.findViewById(R.id.price);
        username = view.findViewById(R.id.user_name);
        user_type = view.findViewById(R.id.user_type);
        number = view.findViewById(R.id.number);
        address = view.findViewById(R.id.address);
        transaction_option = view.findViewById(R.id.transaction_option);
        due_date = view.findViewById(R.id.due_date);
        details = view.findViewById(R.id.details);
        back_img2 = view.findViewById(R.id.back_img2);
        cancel = view.findViewById(R.id.cancel);
        accept = view.findViewById(R.id.accept);
        userImage = view.findViewById(R.id.user_image);


        product_name.setText(transaction.getTitle());

        switch (transaction.getStatus()) {
            case AWAITING_SHIPMENT:
                status.setText("Awaiting Shipment");
                break;
            case COMPLETED:
                status.setText("Completed");
                break;
            case AWAITING_ACCEPTANCE:
                status.setText("Awaiting Acceptance");
                break;
            default:
                break;
        }

        switch (transaction.getCourier()) {
            case TWO_GO:
                transaction_option.setText("2GO");
                break;
            case LBC:
                transaction_option.setText("LBC");
                break;
            case FEDEX:
                transaction_option.setText("FedEx");
                break;
            case JRS:
                transaction_option.setText("JRS Express");
                break;
            default:
                break;
        }

        CustomImageUtils customImageUtils = new CustomImageUtils();
        due_date.setText(CustomUtils.parseDateMMddYYYY(transaction.getExpiration_date()));

        if (transaction.getImg_urls() != null) {
            if (transaction.getImg_urls().get(0) != null)
                customImageUtils.blurImage(getActivity(), transaction.getImg_urls().get(0),
                        transaction_image, true);
        }

        if (transaction.getImg_urls() != null && transaction.getImg_urls().get(0) != null) {
            Glide.with(getContext()).load(transaction.getImg_urls().get(0)).skipMemoryCache(true).into(transaction_image);
            transaction_image.setColorFilter(Color.rgb(123, 123, 123),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        new UserDA().queryUserByUID(UserUtils.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    Glide.with(getContext()).load(user.getPhoto_url()).skipMemoryCache(true).into(userImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back_img2.setOnClickListener(this);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.back_img2) {
            onBackPressedListener.onBackPressed();
        } else if (id == R.id.cancel) {
            updateTransactionStatus(Transaction.Status.DECLINED);
            this.dismiss();

        } else if (id == R.id.accept) {
            updateTransactionStatus(Transaction.Status.AWAITING_SHIPMENT);
        }

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void updateTransactionStatus(Transaction.Status status) {
        transaction.setAccepted_date(System.currentTimeMillis());
        transaction.setStatus(status);

        transactionDA.updateFullTransaction(transaction.getKey(), transaction);
    }

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    private OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
}
