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
import com.devexpert.forfoodiesbyfoodies.models.User;

import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

    private List<Channels> channelList;
    private LayoutInflater mInflater;
    private Context context;


    public ChannelsAdapter(Context context, List channelList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.channelList = channelList;

    }

    @Override
    public ChannelsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.channel_items, parent, false);
        return new ChannelsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelsAdapter.ViewHolder holder, int position) {
        Channels channels = channelList.get(position);
        holder.textView.setText("#"+channels.getTopic());
        holder.linearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("docId", channels.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.channelsTv_id);
            linearLayout = itemView.findViewById(R.id.linearLayout_id);

        }

    }

    Channels getItem(int id) {
        return channelList.get(id);
    }


}