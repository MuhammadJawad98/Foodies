package com.devexpert.forfoodiesbyfoodies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.activities.RestaurantDetailActivity;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Restaurant> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, List<Restaurant> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.resturant_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = mData.get(position);
        holder.restaurantTextView.setText(restaurant.getRestaurantDescription());
        Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(holder.restaurantImageView);
        holder.detailsViewButton.setOnClickListener(view -> {
            Intent intent=new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra("from", Constants.restaurantDetailActivity);
            intent.putExtra("details",restaurant);
            context.startActivity(intent);
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantTextView;
        ImageView restaurantImageView;
        Button detailsViewButton;

        ViewHolder(View itemView) {
            super(itemView);
            restaurantTextView = itemView.findViewById(R.id.restaurantDescriptionTextView_id);
            restaurantImageView = itemView.findViewById(R.id.restaurantImageView_id);
            detailsViewButton = itemView.findViewById(R.id.btnRestaurantView_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Restaurant getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}