package com.berstek.veripay.views.payment.payment_shipment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.berstek.veripay.R;
import com.berstek.veripay.views.parent_layouts.FragmentWithBackAndNext;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PSPage1 extends FragmentWithBackAndNext implements View.OnClickListener {

    private PSPage1Listener inputListener;

    private EditText price, title, details;
    private Button uploadBtn, nextBtn;

    private ArrayList<String> imgURLs;

    public PSPage1() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        inputListener = (PSPage1Listener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pspage1, container, false);
        price = view.findViewById(R.id.price_edit);
        title = view.findViewById(R.id.title_edit);
        details = view.findViewById(R.id.details_edit);
        uploadBtn = view.findViewById(R.id.upload_btn);

        uploadBtn.setOnClickListener(this);

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.upload_btn) {

        } else if (id == R.id.next_btn) {
            inputListener.onPage1Ready(title.getText().toString(),
                    details.getText().toString(), price.getText().toString(), imgURLs);
        }
    }

    interface PSPage1Listener {
        void onPage1Ready(String title, String details, String amount, ArrayList<String> imgURLs);
    }

}
