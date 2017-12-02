package com.berstek.veripay.views.transactions;

import android.app.Activity;
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
import com.berstek.veripay.models.Transaction;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomImageUtils;
import com.berstek.veripay.utils.CustomUtils;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.user_profile.ProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ListHolder> {

    private Context context;
    private ArrayList<Transaction> transactions;
    private LayoutInflater inflater;
    private CustomImageUtils imageUtils;
    private UserDA userDA;

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;

        inflater = LayoutInflater.from(context);
        imageUtils = new CustomImageUtils();
        userDA = new UserDA();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_transaction, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.product_name.setText(transaction.getTitle());
        imageUtils.blurImage((Activity) context, "https://cnet1.cbsistatic.com/img/tfZ_Yy-BLrVitMGK7rODLhzxha4=/830x467/2017/10/31/ca251fb7-a46f-4934-8ab2-ddf6639f32ea/iphone-x-charging-01.jpg", holder.productImg, false);
        holder.name.setText(transaction.getTitle());
        holder.transaction_code.setText(transaction.getTransaction_code());
        holder.transaction_date.setText(CustomUtils.parseDateMMddYYYY(transaction.getCreation_date()));
        holder.price.setText(CustomUtils.formatDF(transaction.getAmount()));

        if (transaction.getShipment_date() != 0) {
            holder.date_shipped.setText(CustomUtils.parseDateMMddYYYY(transaction.getShipment_date()));
        }

        final String uidToLoad, identity;
        if (UserUtils.getUID().equals(transaction.getSender_uid())) {
            uidToLoad = transaction.getReceiver_uid();
            identity = "Recipient";
            holder.price.setTextColor(context.getResources().getColor(R.color.custom_red));
        } else {
            uidToLoad = transaction.getSender_uid();
            identity = "Sender";
            holder.price.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        //test commit 2

        userDA.queryUserByUID(uidToLoad).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    Glide.with(context).load(user.getPhoto_url()).skipMemoryCache(true).into(holder.dp);
                    holder.name.setText(user.getFullName());
                    holder.identity.setText(identity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        switch (transaction.getStatus()) {
            case AWAITING_SHIPMENT:
                holder.status.setText("Awaiting Shipment");
                break;
            case COMPLETED:
                holder.status.setText("Completed");
                break;
            case AWAITING_ACCEPTANCE:
                holder.status.setText("Awaiting Acceptance");
                break;
            default:
                break;
        }

        switch (transaction.getCourier()) {
            case TWO_GO:
                holder.courier.setText("2GO");
                break;
            case LBC:
                holder.courier.setText("LBC");
                break;
            case FEDEX:
                holder.courier.setText("FedEx");
                break;
            case JRS:
                holder.courier.setText("JRS Express");
                break;
            default:
                break;
        }

    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        private ImageView dp, productImg;
        private TextView product_name, status, name,
                identity, price, courier, transaction_date,
                date_shipped, transaction_code, rating;
        private View.OnClickListener clickListener;

        public ListHolder(View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.recview_images);
            dp = itemView.findViewById(R.id.profile_image);
            product_name = itemView.findViewById(R.id.product_name_textview);
            status = itemView.findViewById(R.id.status);
            name = itemView.findViewById(R.id.name_textview);
            identity = itemView.findViewById(R.id.identity_textview);
            transaction_code = itemView.findViewById(R.id.transaction_code);
            rating = itemView.findViewById(R.id.rating_textview);
            price = itemView.findViewById(R.id.price_textview);
            courier = itemView.findViewById(R.id.courier_value);
            transaction_date = itemView.findViewById(R.id.transaction_date_value);
            date_shipped = itemView.findViewById(R.id.shipped_date_value);

            dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
