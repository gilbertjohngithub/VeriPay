package com.berstek.veripay.views.search;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.berstek.veripay.R;
import com.berstek.veripay.data_access.ContactDA;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Contact;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.search.SearchResultsAdapter.OnResultSelectedListener;
import com.berstek.veripay.views.search.SearchContactsAdapter.OnContactSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserDialogFragment extends DialogFragment
        implements OnContactSelectedListener, OnResultSelectedListener, TextWatcher, View.OnClickListener {

    public SearchUserDialogFragment() {
        // Required empty public constructor
    }

    private UserDA userDA;
    private ContactDA contactDA;

    private OnResultSelectedListener resultSelectedListener;
    private OnContactSelectedListener contactSelectedListener;

    private ImageView backImg;
    private EditText searchEdit;
    private RecyclerView recviewContacts, recviewResults;

    private ArrayList<Contact> contacts;


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_user, container, false);
        backImg = view.findViewById(R.id.back_img);
        searchEdit = view.findViewById(R.id.search_edit);
        recviewContacts = view.findViewById(R.id.recview_contacts);
        recviewResults = view.findViewById(R.id.recview_results);

        userDA = new UserDA();
        contactDA = new ContactDA();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recviewContacts.setLayoutManager(linearLayoutManager);
        recviewResults.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEdit.addTextChangedListener(this);
        backImg.setOnClickListener(this);

        loadContacts();

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        handler.removeCallbacks(inputListener);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        handler.postDelayed(inputListener, delay);
    }

    private long delay = 1000, lastEdited = 0;
    private Handler handler = new Handler();

    private Runnable inputListener = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > (lastEdited + delay - 500)) {
                final ArrayList<User> users = new ArrayList<>();

                userDA.queryUserByPayID(searchEdit.getText().toString()).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    User user = child.getValue(User.class);
                                    user.setKey(child.getKey());
                                    users.add(user);

                                    SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(getContext(), users);
                                    searchResultsAdapter.setResultSelectedListener(SearchUserDialogFragment.this);
                                    recviewResults.setAdapter(searchResultsAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


            }
        }
    };

    private void loadContacts() {
        contactDA.queryuserContactsByUID(UserUtils.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Contact contact = new Contact();
                    contact.setKey(child.getKey());
                    contacts.add(contact);
                }

                SearchContactsAdapter searchContactsAdapter = new SearchContactsAdapter(getContext(),
                        contacts);
                searchContactsAdapter.setContactSelectedListener(SearchUserDialogFragment.this);
                recviewContacts.setAdapter(searchContactsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    public void onContactSelected(Contact contact) {
        contactSelectedListener.onContactSelected(contact);
    }

    @Override
    public void onResultSelected(User user) {
        resultSelectedListener.onResultSelected(user);
    }

    public void setResultSelectedListener(OnResultSelectedListener resultSelectedListener) {
        this.resultSelectedListener = resultSelectedListener;
    }

    public void setContactSelectedListener(OnContactSelectedListener contactSelectedListener) {
        this.contactSelectedListener = contactSelectedListener;
    }


    @Override
    public void onClick(View view) {
        this.dismiss();
    }
}
