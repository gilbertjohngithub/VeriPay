package com.berstek.veripay.views.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berstek.veripay.R;

import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Contact;
import com.berstek.veripay.models.User;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchContactsAdapter extends RecyclerView.Adapter<SearchContactsAdapter.ListHolder> {

    private Context context;
    private ArrayList<Contact> contacts;
    private LayoutInflater inflater;
    private UserDA userDA;

    public SearchContactsAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        inflater = LayoutInflater.from(context);
        userDA = new UserDA();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_search_contact, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        final Contact contact = contacts.get(position);

        userDA.queryUserByUID(contact.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);

                    Glide.with(context).load(user.getPhoto_url()).
                            skipMemoryCache(true).into(holder.dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView dp;

        public ListHolder(View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.dp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            contactSelectedListener.onContactSelected(contacts.get(getAdapterPosition()));
        }
    }

    public interface OnContactSelectedListener {
        void onContactSelected(Contact contact);
    }

    private OnContactSelectedListener contactSelectedListener;

    public void setContactSelectedListener(OnContactSelectedListener contactSelectedListener) {
        this.contactSelectedListener = contactSelectedListener;
    }
}
