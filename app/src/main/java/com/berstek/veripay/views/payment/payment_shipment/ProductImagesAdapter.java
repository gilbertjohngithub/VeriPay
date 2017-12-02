package com.berstek.veripay.views.payment.payment_shipment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.berstek.veripay.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ListHolder> {

    private Context context;
    private ArrayList<String> images;
    private LayoutInflater inflater;

    public ProductImagesAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_product_image, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        String imgUrl = images.get(position);
        Glide.with(context).load(imgUrl).skipMemoryCache(true).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image, cancel;

        public ListHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.product_image);
            cancel = itemView.findViewById(R.id.cancel);
            cancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            imageRemovedListener.onImageRemoved(getAdapterPosition());
        }
    }

    public interface OnImageRemovedListener {
        void onImageRemoved(int position);
    }

    private OnImageRemovedListener imageRemovedListener;

    public void setImageRemovedListener(OnImageRemovedListener imageRemovedListener) {
        this.imageRemovedListener = imageRemovedListener;
    }
}
