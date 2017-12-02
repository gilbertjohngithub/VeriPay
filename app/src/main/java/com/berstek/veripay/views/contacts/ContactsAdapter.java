package com.berstek.veripay.views.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.R;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Contact;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.user_profile.ProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ListHolder> {

    private Context context;
    private ArrayList<Contact> contacts;
    private LayoutInflater inflater;
    private UserDA userDA;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        inflater = LayoutInflater.from(context);
        userDA = new UserDA();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_contact, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        Contact contact = contacts.get(position);



        userDA.queryUserByUID(contact.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    User user = child.getValue(User.class);
                    holder.name.setText(user.getFullName());
                    Glide.with(context).load(user.getPhoto_url()).skipMemoryCache(true).into(holder.dp);
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

    class ListHolder extends RecyclerView.ViewHolder{
        private TextView name, rating;
        private ImageView dp;
        private View itemView;
        public ListHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dp = itemView.findViewById(R.id.profile_image);
            dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setProfileActivity();
                }
            });
            name = itemView.findViewById(R.id.name_textview);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setProfileActivity();
                }
            });
            rating = itemView.findViewById(R.id.rating_value);



        }

        public void setProfileActivity(){
            Intent intent = new Intent(context, ProfileActivity.class);
            context.startActivity(intent);
        }
    }
}
