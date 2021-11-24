package com.devexpert.forfoodiesbyfoodies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private List<Chat> chatList;
    private LayoutInflater mInflater;
    private Context context;
    private String userId;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ChatAdapter(Context context, List chatList, String userId) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.chatList = chatList;
        this.userId = userId;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //= mInflater.inflate(R.layout.receiver_message_item, parent, false);
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sender_message_item, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receiver_message_item, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat chat = chatList.get(position);
//        holder.textView.setText(chat.getText());
//        holder.tvUserName.setText(chat.getUserName());
//        holder.tvTimestamp.setText(CommonFunctions.convertTime(chat.getTimestamp()));
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(chat);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chat);
        }
//        if(chat.getUserId().equals(userId)){
//            holder.linearLayout.setBackgroundResource(R.drawable.chat_sender_bubble);
//            holder.linearLayout.setGravity(Gravity.END);
//
//        }else{
//            holder.linearLayout.setBackgroundResource(R.drawable.chat_receiver_bubble);
//            holder.linearLayout.setHorizontalGravity(Gravity.START);
//        }


    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Chat message = (Chat) chatList.get(position);

        if (message.getUserId().equals(userId)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textView;
//        TextView tvUserName;
//        TextView tvTimestamp;
//        LinearLayout linearLayout;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            textView = itemView.findViewById(R.id.message_id);
//            tvUserName = itemView.findViewById(R.id.userName_id);
//            tvTimestamp = itemView.findViewById(R.id.timestamp_id);
//            linearLayout = itemView.findViewById(R.id.linearLayout_id);
//
//        }
//
//    }
//
//    Chat getItem(int id) {
//        return chatList.get(id);
//    }


}

class SentMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText,tvUserName;

    SentMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.message_id);
        tvUserName = (TextView) itemView.findViewById(R.id.userName_id);
        timeText = (TextView) itemView.findViewById(R.id.timestamp_id);
    }

    void bind(Chat chat) {
        messageText.setText(chat.getText());
        tvUserName.setText(chat.getUserName());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(CommonFunctions.convertTime(chat.getTimestamp()));
    }
}

class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText, nameText;


    ReceivedMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.message_id);
        timeText = (TextView) itemView.findViewById(R.id.timestamp_id);
        nameText = (TextView) itemView.findViewById(R.id.userName_id);
    }

    void bind(Chat message) {
        messageText.setText(message.getText());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(CommonFunctions.convertTime(message.getTimestamp()));

        nameText.setText(message.getUserName());

    }
}