package com.devexpert.forfoodiesbyfoodies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.activities.OtherUserProfileActivity;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ReviewRecyclerviewAdapter extends RecyclerView.Adapter<ReviewRecyclerviewAdapter.ViewHolder> {

    private final List<Review> mData;
    private final LayoutInflater mInflater;
    private final Context context;
    private ItemClickListener mClickListener;
    private CustomSharedPreference yourPreference;
    private final String from;
    private final String restaurantId;
    private final boolean isAdmin;

    public ReviewRecyclerviewAdapter(Context context, List<Review> data, String from, String restaurantId, boolean isAdmin) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
        this.from = from;
        this.restaurantId = restaurantId;
        this.isAdmin = isAdmin;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.reviews_items, parent, false);
        yourPreference = CustomSharedPreference.getInstance(this.context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mData.get(position);
        String userId = yourPreference.getData(Constants.userId);

        holder.nameTv.setText(review.getName());
        holder.commentTv.setText(review.getComment());
        Picasso.get().load(review.getProfileUrl()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(holder.imageView);
        holder.ratingBar.setRating((float) review.getReviewRating());
        if (from.equals(Constants.restaurantDetailActivity)) {
            holder.imageViewDelete.setVisibility(View.GONE);
        } else {
            if (userId.equals(review.getUserId()) || isAdmin) {
                holder.imageViewDelete.setVisibility(View.VISIBLE);
            }
        }

        holder.imageViewDelete.setOnClickListener(view -> {
            try {
                FireStore.db.collection(Constants.rootCollectionStreetFood).document(restaurantId).collection(Constants.reviews).document(review.getId()).delete();
            } catch (Exception e) {
                CommonFunctions.customLog(e.getMessage());
            }
        });
        holder.imageView.setOnClickListener(view -> {
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

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTv;
        TextView commentTv;
        RatingBar ratingBar;
        ImageView imageView;
        ImageView imageViewDelete;

        ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.reviewerName_id);
            commentTv = itemView.findViewById(R.id.reviewerComment_id);
            ratingBar = itemView.findViewById(R.id.reviewRating_id);
            imageView = itemView.findViewById(R.id.profile_image);
            imageViewDelete = itemView.findViewById(R.id.review_delete_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    public void setClickListener(ReviewRecyclerviewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}