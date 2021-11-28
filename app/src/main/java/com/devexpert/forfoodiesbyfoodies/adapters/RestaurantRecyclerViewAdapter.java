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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<Restaurant> restaurantList;
    private final LayoutInflater inflater;
    private final Context context;

    // data is passed into the constructor
    public RestaurantRecyclerViewAdapter(Context context, List<Restaurant> data) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.restaurantList = data;
    }

    // inflates the row layout from xml when needed
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.resturant_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.restaurantTextView.setText(restaurant.getRestaurantDescription());
        Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(holder.restaurantImageView);
        holder.detailsViewButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra(Constants.from, Constants.restaurantDetailActivity);
            intent.putExtra(Constants.details, restaurant);
            context.startActivity(intent);
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    // stores and recycler views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantTextView;
        ImageView restaurantImageView;
        Button detailsViewButton;

        ViewHolder(View itemView) {
            super(itemView);
            restaurantTextView = itemView.findViewById(R.id.restaurantDescriptionTextView_id);
            restaurantImageView = itemView.findViewById(R.id.restaurantImageView_id);
            detailsViewButton = itemView.findViewById(R.id.btnRestaurantView_id);
        }
    }
}