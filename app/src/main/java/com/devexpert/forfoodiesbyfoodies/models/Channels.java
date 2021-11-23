package com.devexpert.forfoodiesbyfoodies.models;

public class Channels {
    String id;
    String topic;

    public Channels() {
    }

    public Channels(String id, String topic) {
        this.id = id;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
