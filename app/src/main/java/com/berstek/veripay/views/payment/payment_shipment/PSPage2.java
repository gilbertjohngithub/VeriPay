package com.berstek.veripay.views.payment.payment_shipment;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.berstek.veripay.R;

import com.berstek.veripay.data_access.TestDA;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Transaction;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomUtils;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.parent_layouts.FragmentWithBackAndNext;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PSPage2 extends FragmentWithBackAndNext implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener,
        TextWatcher, ProductImagesAdapter.OnImageRemovedListener {
    private TextView amount, product_name,
            product_details, lbc_express,
            jrs_express, courier_2go, seekbar_days;
    private RecyclerView recyclerView;

    private ArrayList transactions;
    private Transaction transaction;
    private SeekBar seekBar;
    private EditText address;
    private UserDA userDA;
    private RadioGroup radioGroup;
    private String selectedCourier;
    private ProductImagesAdapter adapter;

    public PSPage2() {
        // Required empty public constructor
    }


    public interface OnPage2ReadyListener {
        public void onPage2Ready(Transaction transaction);
    }

    private OnPage2ReadyListener page2ReadyListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        page2ReadyListener = (OnPage2ReadyListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pspage2, container, false);
        // Inflate the layout for this fragment
        userDA = new UserDA();

        transactions = getArguments().getParcelableArrayList("transactions");
        transaction = (Transaction) transactions.get(0);

        transaction.setTransaction_type(Transaction.ShippingType.PICKUP);

        amount = view.findViewById(R.id.amount);
        product_name = view.findViewById(R.id.title);
        product_details = view.findViewById(R.id.details);

        lbc_express = view.findViewById(R.id.lbc_express);
        jrs_express = view.findViewById(R.id.jrs_express);
        courier_2go = view.findViewById(R.id.courier_2go);
        selectedCourier = "lbc";

        seekBar = view.findViewById(R.id.seekbar);
        seekbar_days = view.findViewById(R.id.seekbar_days);
        address = view.findViewById(R.id.address_edit);

        recyclerView = view.findViewById(R.id.recview_images);
        radioGroup = view.findViewById(R.id.radiogroup);

        super.onCreateView(inflater, container, savedInstanceState);

        lbc_express.setOnClickListener(this);
        jrs_express.setOnClickListener(this);
        courier_2go.setOnClickListener(this);
        address.addTextChangedListener(this);

        loadTransactionData();

        seekBar.setOnSeekBarChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        return view;
    }


    private void loadTransactionData() {
        seekbar_days.setText(CustomUtils.parseDateMMdd(transaction.getExpiration_date()));
        amount.setText(transaction.getAmount() + "");
        product_name.setText(transaction.getTitle());
        product_details.setText(transaction.getDetail());

        userDA.queryUserByUID(UserUtils.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    String a = user.getAddress_street_brgy() + ", " +
                            user.getAddress_city() + ", Philippines";
                    address.setText(a);

                    transaction.setAddress(a);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        new TestDA().writeToConsole1("IMAGES: " + transaction.getImg_urls().size());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        adapter = new ProductImagesAdapter(getContext(),
                transaction.getImg_urls());

        adapter.setImageRemovedListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        super.onClick(view);

        if (id == R.id.lbc_express) {
            transaction.setCourier(Transaction.Courier.LBC);
            setSelectedCourier("lbc", selectedCourier, lbc_express);
        } else if (id == R.id.courier_2go) {
            transaction.setCourier(Transaction.Courier.TWO_GO);
            setSelectedCourier("2go", selectedCourier, courier_2go);
        } else if (id == R.id.jrs_express) {
            transaction.setCourier(Transaction.Courier.JRS);
            setSelectedCourier("jrs", selectedCourier, jrs_express);
        } else if (id == R.id.next_btn) {
            page2ReadyListener.onPage2Ready(transaction);
        }
    }

    private void setSelectedCourier(String newCourier, String oldCourier, TextView newCourierTextView) {
        TextView oldCourierTextView = null;
        if (!oldCourier.equals(newCourier)) {
            if (oldCourier.equals("lbc")) {
                oldCourierTextView = lbc_express;
            } else if (oldCourier.equals("jrs")) {
                oldCourierTextView = jrs_express;
            } else if (oldCourier.equals("2go")) {
                oldCourierTextView = courier_2go;
            }
            oldCourierTextView.setBackground(null);
            oldCourierTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            oldCourierTextView.setTypeface(null, Typeface.ITALIC);

            selectedCourier = newCourier;
            newCourierTextView.setBackgroundResource(R.drawable.oval);
            newCourierTextView.setBackground(getResources().getDrawable(R.drawable.oval));
            newCourierTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            newCourierTextView.setTextColor(getResources().getColor(android.R.color.white));
            newCourierTextView.setTypeface(null, Typeface.BOLD_ITALIC);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        long date = transaction.getCreation_date();

        date = date + (i * (24 * 60 * 60 * 1000));

        Date due_date = new Date(date);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");

        seekbar_days.setText(df.format(due_date));

        transaction.setExpiration_date(date);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.pickup) {
            new TestDA().writeToConsole1("DOOR");
            transaction.setTransaction_type(Transaction.ShippingType.PICKUP);
        } else {
            new TestDA().writeToConsole1("PICKUP");
            transaction.setTransaction_type(Transaction.ShippingType.DOOR);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        transaction.setAddress(address.getText().toString());
    }

    @Override
    public void onImageRemoved(int position) {
        transaction.getImg_urls().remove(position);
        adapter.notifyItemRemoved(position);
    }
}
