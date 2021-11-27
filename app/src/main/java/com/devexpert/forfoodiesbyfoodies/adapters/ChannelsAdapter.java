package com.devexpert.forfoodiesbyfoodies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.activities.ChatActivity;
import com.devexpert.forfoodiesbyfoodies.models.Channels;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

    private final List<Channels> channelList;
    private final LayoutInflater inflater;
    private final Context context;


    public ChannelsAdapter(Context context, List<Channels> channelList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.channelList = channelList;
    }

    @NotNull
    @Override
    public ChannelsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.channel_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelsAdapter.ViewHolder holder, int position) {
        Channels channels = channelList.get(position);
        holder.textView.setText("#".concat(channels.getTopic()));
        holder.linearLayout.setOnClickListener(view -> {
            //navigate to chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.docId, channels.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.channelsTv_id);
            linearLayout = itemView.findViewById(R.id.linearLayout_id);
        }
    }
}