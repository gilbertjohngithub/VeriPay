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
import com.berstek.veripay.models.Contact;
import com.berstek.veripay.views.user_profile.ProfileActivity;


import java.util.ArrayList;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ListHolder> {

    private Context context;
    private ArrayList<Contact> contacts;
    private LayoutInflater inflater;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_contact, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        Contact contact = contacts.get(position);
        //eto pa dito ko gagalaw

        //bale i set key mo muna yung contact sa contactsFragment para makuha mo yung UID

        //tapos sa userDA, queryUserByUID ka
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
