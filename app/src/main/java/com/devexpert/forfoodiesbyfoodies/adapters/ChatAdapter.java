package com.devexpert.forfoodiesbyfoodies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

public class ChatAdapter extends Adapter {

    private final List<Chat> chatList;
    private final String userId;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ChatAdapter(List<Chat> chatList, String userId) {
        this.chatList = chatList;
        this.userId = userId;

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(chat);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chat);
        }

    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Chat message = chatList.get(position);

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

}

class SentMessageHolder extends ViewHolder {
    TextView messageText, timeText, tvUserName;

    SentMessageHolder(View itemView) {
        super(itemView);

        messageText = itemView.findViewById(R.id.message_id);
        tvUserName = itemView.findViewById(R.id.userName_id);
        timeText = itemView.findViewById(R.id.timestamp_id);
    }

    void bind(Chat chat) {
        messageText.setText(chat.getText());
        tvUserName.setText(chat.getUserName());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(CommonFunctions.convertTime(chat.getTimestamp()));
    }
}

class ReceivedMessageHolder extends ViewHolder {
    TextView messageText, timeText, nameText;


    ReceivedMessageHolder(View itemView) {
        super(itemView);

        messageText = itemView.findViewById(R.id.message_id);
        timeText = itemView.findViewById(R.id.timestamp_id);
        nameText = itemView.findViewById(R.id.userName_id);
    }

    void bind(Chat message) {
        messageText.setText(message.getText());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(CommonFunctions.convertTime(message.getTimestamp()));

        nameText.setText(message.getUserName());

    }
}