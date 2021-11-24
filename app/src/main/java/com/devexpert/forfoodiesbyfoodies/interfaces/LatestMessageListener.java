package com.devexpert.forfoodiesbyfoodies.interfaces;

import com.devexpert.forfoodiesbyfoodies.models.Chat;

public interface LatestMessageListener {
    void onReceiveMessage(Chat chat);
}
