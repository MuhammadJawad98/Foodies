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
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StreetFoodRecyclerviewAdapter extends RecyclerView.Adapter<StreetFoodRecyclerviewAdapter.ViewHolder> {

    private final List<StreetFood> streetFoodList;
    private final LayoutInflater inflater;
    private final Context context;
    private final User user;

    public StreetFoodRecyclerviewAdapter(Context context, List<StreetFood> data, User user) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.streetFoodList = data;
        this.user = user;
    }

    @NotNull
    @Override
    public StreetFoodRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.resturant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StreetFoodRecyclerviewAdapter.ViewHolder holder, int position) {
        StreetFood streetFood = streetFoodList.get(position);
        holder.streetFoodTextView.setText(streetFood.getName());
        Picasso.get().load(streetFood.getPicture()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(holder.streetFoodImageView);
        holder.streetFoodViewButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra(Constants.from, Constants.streetFoodActivity);
            intent.putExtra(Constants.details,
                    new Restaurant(streetFood.getPicture(), streetFood.getDescription(), streetFood.getName(), streetFood.getId()));
            intent.putExtra("userData", user);
            context.startActivity(intent);
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return streetFoodList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView streetFoodTextView;
        ImageView streetFoodImageView;
        Button streetFoodViewButton;

        ViewHolder(View itemView) {
            super(itemView);
            streetFoodTextView = itemView.findViewById(R.id.restaurantDescriptionTextView_id);
            streetFoodTextView.setGravity(Gravity.CENTER);
            streetFoodImageView = itemView.findViewById(R.id.restaurantImageView_id);
            streetFoodViewButton = itemView.findViewById(R.id.btnRestaurantView_id);
        }


    }
}