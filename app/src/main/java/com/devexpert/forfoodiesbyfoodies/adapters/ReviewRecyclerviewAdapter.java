package com.devexpert.forfoodiesbyfoodies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.activities.OtherUserProfileActivity;
import com.devexpert.forfoodiesbyfoodies.activities.RestaurantDetailActivity;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.utils.CustomDialogClass;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewRecyclerviewAdapter extends RecyclerView.Adapter<ReviewRecyclerviewAdapter.ViewHolder> {

    private List<Review> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ReviewRecyclerviewAdapter(Context context, List<Review> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.reviews_items, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mData.get(position);
        System.out.println("comment >>>>"+review.getComment());
        holder.nameTv.setText(review.getName());
        holder.commentTv.setText(review.getComment());
        Picasso.get().load(review.getProfileUrl()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(holder.imageView);

        holder.ratingBar.setRating((float) review.getReviewRating());
        holder.imageView.setOnClickListener(view -> {
            System.out.println("$$$$$$$$$" + review.getName());
            try {
                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra("details", review);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                System.out.println("error::::" + e.toString());
            }

        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTv;
        TextView commentTv;
        RatingBar ratingBar;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.reviewerName_id);
            commentTv = itemView.findViewById(R.id.reviewerComment_id);
            ratingBar = itemView.findViewById(R.id.reviewRating_id);
            imageView = itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Review getItem(int id) {
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