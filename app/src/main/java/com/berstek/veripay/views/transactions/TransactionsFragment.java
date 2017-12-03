package com.berstek.veripay.views.transactions;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.R;

import com.berstek.veripay.callbacks.ChildEventCallback;
import com.berstek.veripay.data_access.AccountBalanceDA;
import com.berstek.veripay.data_access.TransactionDA;
import com.berstek.veripay.models.AccountBalance;
import com.berstek.veripay.models.Transaction;
import com.berstek.veripay.models.TransactionHistory;
import com.berstek.veripay.utils.CustomUtils;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.parent_layouts.CustomDialogFragment;
import com.berstek.veripay.views.payment.PaymentTypeActivity;
import com.berstek.veripay.views.payment.payment_shipment.PSConfirmationDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment implements View.OnClickListener, ItemFullViewDialog.OnBackPressedListener {

    private View view;
    private RecyclerView recyclerView;
    private ImageView cashin, cashout, send, receive;
    private TransactionsChildListener rc;
    private Query sentTransationsQuery, receivedTransactionsQuery;
    private AccountBalanceDA accountBalanceDA;
    private TextView balance;
    private ArrayList<Transaction> transactions;
    private ItemFullViewDialog dialog;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transactions, container, false);

        balance = view.findViewById(R.id.balance);
        cashin = view.findViewById(R.id.cashin);
        cashout = view.findViewById(R.id.cashout);
        send = view.findViewById(R.id.send);
        receive = view.findViewById(R.id.receive);

        accountBalanceDA = new AccountBalanceDA();

        recyclerView = view.findViewById(R.id.recview_transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cashin.setOnClickListener(this);
        cashout.setOnClickListener(this);
        send.setOnClickListener(this);
        receive.setOnClickListener(this);

        loadTransactions();
        loadAccountBalance();

        return view;
    }

    private void loadAccountBalance() {
        final AccountBalance accountBalance = new AccountBalance();

        accountBalanceDA.getTransactionHistory(UserUtils.getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TransactionHistory transactionHistory = child.getValue(TransactionHistory.class);
                    accountBalance.pushTransaction(transactionHistory);
                    balance.setText(CustomUtils.formatDF(accountBalance.getAccountBalance()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadTransactions() {
        transactions = new ArrayList<>();
        final TransactionsAdapter adapter = new TransactionsAdapter(getContext(), transactions);
        recyclerView.setAdapter(adapter);

        final TransactionDA transactionDA = new TransactionDA();
//
        rc = new TransactionsChildListener();

        rc.setChildEventCallback(new ChildEventCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Transaction transaction = dataSnapshot.getValue(Transaction.class);
                transaction.setKey(dataSnapshot.getKey());
                transactions.add(transaction);
                adapter.notifyItemInserted(transactions.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {

            }
        });

        //query sent and received transactions from the database and add listeners to them
        sentTransationsQuery = transactionDA.queryTransactionsBySender(UserUtils.getUID());
        sentTransationsQuery.addChildEventListener(rc);

        receivedTransactionsQuery = transactionDA.queryTransactionsByReceiver(UserUtils.getUID());
        receivedTransactionsQuery.addChildEventListener(rc);

        adapter.setTransactionClickedListener(new TransactionsAdapter.OnTransactionClickedListener() {
            @Override
            public void onTransactionClicked(int pos) {
                ArrayList t = new ArrayList<>();
                t.add(transactions.get(pos));

                dialog = new ItemFullViewDialog();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("transactions", t);
                dialog.setArguments(bundle);

                dialog.setOnBackPressedListener(TransactionsFragment.this);


                dialog.show(getFragmentManager(), null);
            }
        });
    }

    @Override
    public void onClick(View view) {
        //TODO function for each button
        int id = view.getId();

        if (id == R.id.send) {
            Intent intent = new Intent(getContext(), PaymentTypeActivity.class);
            startActivity(intent);
        } else if (id == R.id.receive) {
            PSConfirmationDialogFragment fragment = new PSConfirmationDialogFragment();
            fragment.show(getActivity().getFragmentManager(), null);
        } else if (id == R.id.cashin) {

        } else if (id == R.id.cashout) {

        }
    }

    @Override
    public void onBackPressed() {
        dialog.dismiss();
    }
}
