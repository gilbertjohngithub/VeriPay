package com.berstek.veripay.views.contacts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berstek.veripay.R;
import com.berstek.veripay.data_access.ContactDA;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Contact;
import com.berstek.veripay.utils.UserUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private Query contactQuery, totalQuery;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.contact_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadContacts();
        return view;
    }

    private void loadContacts() {
        final ArrayList<Contact> contacts = new ArrayList<>();
        final ContactsAdapter adapter = new ContactsAdapter(getContext(), contacts);
        recyclerView.setAdapter(adapter);

        ContactDA contactDA = new ContactDA();
        UserDA userDA = new UserDA();

        contactDA.queryuserContactsByUID(UserUtils.getUID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Contact contact = new Contact();
                contact.setKey(dataSnapshot.getKey());
                contacts.add(contact);
                adapter.notifyItemInserted(contacts.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

}
