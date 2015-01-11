package com.teamchief.petergok.teamchief.model.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Peter on 2015-01-07.
 */
public class Message {
    private long id;

    @SerializedName(value="_id")
    private String messageId;

    private String sender;
    private String text;
    private String teamId;
    private long sendTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return sender + " at " + sendTime + ": " + text;
    }
}
