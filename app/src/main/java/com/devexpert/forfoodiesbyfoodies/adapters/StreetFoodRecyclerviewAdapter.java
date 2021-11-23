package com.devexpert.forfoodiesbyfoodies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
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
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StreetFoodRecyclerviewAdapter extends RecyclerView.Adapter<StreetFoodRecyclerviewAdapter.ViewHolder> {

    private List<StreetFood> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ReviewRecyclerviewAdapter.ItemClickListener mClickListener;

    public StreetFoodRecyclerviewAdapter(Context context, List<StreetFood> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    @Override
    public StreetFoodRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.resturant_item, parent, false);
        return new StreetFoodRecyclerviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StreetFoodRecyclerviewAdapter.ViewHolder holder, int position) {
        StreetFood streetFood = mData.get(position);
        holder.streetFoodTextView.setText(streetFood.getName());
        Picasso.get().load(streetFood.getPicture()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(holder.streetFoodImageView);
        holder.streetFoodViewButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra("from", Constants.streetFoodActivity);
            intent.putExtra("details",
                    new Restaurant(streetFood.getPicture(), streetFood.getDescription(), streetFood.getName(), streetFood.getId()));
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
        TextView streetFoodTextView;
        ImageView streetFoodImageView;
        Button streetFoodViewButton;

        ViewHolder(View itemView) {
            super(itemView);
            streetFoodTextView = itemView.findViewById(R.id.restaurantDescriptionTextView_id);
            streetFoodTextView.setGravity(Gravity.CENTER);
            streetFoodImageView = itemView.findViewById(R.id.restaurantImageView_id);
            streetFoodViewButton = itemView.findViewById(R.id.btnRestaurantView_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    StreetFood getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ReviewRecyclerviewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}