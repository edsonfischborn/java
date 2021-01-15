package com.pg3.dto;

public class SiteMessageStatusDto {
    private long allMessages;
    private long readMessages;
    private long unreadMessages;

    public long getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(long allMessages) {
        this.allMessages = allMessages;
    }

    public long getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(long readMessages) {
        this.readMessages = readMessages;
    }

    public long getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(long unreadMessages) {
        this.unreadMessages = unreadMessages;
    }
}
