package com.berstek.veripay.views.feedback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.R;
import com.berstek.veripay.models.Feedback;


import java.util.ArrayList;

public class FeedbacksAdapter extends RecyclerView.Adapter<FeedbacksAdapter.ListHolder> {

    private Context context;
    private ArrayList<Feedback> feedbacks;
    private LayoutInflater inflater;


    public FeedbacksAdapter(Context context, ArrayList<Feedback> feedbacks) {
        this.context = context;
        this.feedbacks = feedbacks;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_feedback, parent, false);

        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        //TODO
        if(holder.getAdapterPosition()==1)
            holder.name.setText("Reymark Arsenio");
        else if(holder.getAdapterPosition()==2)
            holder.name.setText("Danmark Arqueza");
        else if(holder.getAdapterPosition()==3)
            holder.name.setText("Gilbert Cuaresma");
        else
            holder.name.setText("Mark Anthony Parcia");
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        private ImageView dp,rating;
        private TextView description, name, comment;

        public ListHolder(View itemView) {
            super(itemView);
        dp = itemView.findViewById(R.id.profile_image);
        rating = itemView.findViewById(R.id.rating_imageview);
        description = itemView.findViewById(R.id.description_textview);
        name = itemView.findViewById(R.id.name_textview);
        comment = itemView.findViewById(R.id.comment_textview);
        }
    }
}
