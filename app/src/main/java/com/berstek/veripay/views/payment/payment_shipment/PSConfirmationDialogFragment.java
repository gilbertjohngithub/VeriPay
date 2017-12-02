package com.berstek.veripay.views.payment.payment_shipment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.R;

import com.berstek.veripay.callbacks.ConfirmationDialogListener;
import com.berstek.veripay.data_access.BusinessSettingsDA;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.BusinessSettings;
import com.berstek.veripay.models.Transaction;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomUtils;
import com.berstek.veripay.views.parent_layouts.CustomDialogFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PSConfirmationDialogFragment extends CustomDialogFragment
        implements View.OnClickListener {

    private View view;
    private ImageView cancel, confirm;

    private TextView recipient, payment, charge, total, description;
    private TextView due_date, transaction_type, address;
    private TextView title, details;

    private ImageView courier, dp;

    private ArrayList transactions;
    private Transaction transaction;

    private UserDA userDA;
    private BusinessSettingsDA businessSettingsDA;

    public PSConfirmationDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_psconfirmation_dialog, container,
                false);


        userDA = new UserDA();
        businessSettingsDA = new BusinessSettingsDA();

        transactions = getArguments().getParcelableArrayList("transactions");
        transaction = (Transaction) transactions.get(0);

        description = view.findViewById(R.id.description);

        cancel = view.findViewById(R.id.cancel);
        confirm = view.findViewById(R.id.confirm);

        recipient = view.findViewById(R.id.recipient);
        payment = view.findViewById(R.id.payment);
        charge = view.findViewById(R.id.charge);
        total = view.findViewById(R.id.total);
        due_date = view.findViewById(R.id.due_date);
        transaction_type = view.findViewById(R.id.transaction_type);
        address = view.findViewById(R.id.address);
        title = view.findViewById(R.id.title);
        details = view.findViewById(R.id.details);

        courier = view.findViewById(R.id.courier);
        dp = view.findViewById(R.id.dp);

        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        loadData();

        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void loadData() {

        due_date.setText(CustomUtils.parseDateMMdd(transaction.getExpiration_date()));
        address.setText(transaction.getAddress());
        payment.setText(CustomUtils.formatDF(transaction.getAmount()));
        title.setText(transaction.getTitle());
        details.setText(transaction.getDetail());

        switch (transaction.getTransaction_type()) {
            case DOOR:
                transaction_type.setText("DIRECT SHIPPING");
                break;
            case PICKUP:
                transaction_type.setText("PICKUP");
                break;
            default:
                break;
        }

        switch (transaction.getCourier()) {
            case JRS:
                courier.setImageDrawable(getResources().getDrawable(R.drawable.ic_jrs));
                break;
            case LBC:
                courier.setImageDrawable(getResources().getDrawable(R.drawable.ic_lbc));
                break;
            case TWO_GO:
                courier.setImageDrawable(getResources().getDrawable(R.drawable.ic_2go));
                break;
            default:
                break;
        }

//        load recipient data
        userDA.queryUserByUID(transaction.getReceiver_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);

                    Glide.with(getActivity())
                            .load(user.getPhoto_url())
                            .skipMemoryCache(true)
                            .into(dp);

                    recipient.setText(user.getFullName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //load charges
        businessSettingsDA.queryBusinessSettings().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BusinessSettings businessSettings = dataSnapshot.getValue(BusinessSettings.class);
                transaction.setTrans_charge(businessSettings.getTrans_charge());
                double percentCharge = transaction.getAmount() * businessSettings.getPercentage();
                transaction.setPercent_charge(percentCharge);

                double totalCharge = percentCharge + transaction.getTrans_charge();
                charge.setText(CustomUtils.formatDF(totalCharge));

                total.setText("PHP " + CustomUtils.formatDF(totalCharge + transaction.getAmount()));

                description.setText(businessSettings.getTrans_charge()
                        + " + " + businessSettings.getPercentage() * 100 + " of total payment");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.cancel) {
            dialogListener.onCancel();
        } else if (id == R.id.confirm) {
            dialogListener.onAgree();
        }
    }

    private ConfirmationDialogListener dialogListener;

    public void setDialogListener(ConfirmationDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
