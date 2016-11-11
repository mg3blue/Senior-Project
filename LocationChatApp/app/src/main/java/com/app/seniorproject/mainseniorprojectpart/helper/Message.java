package com.app.seniorproject.mainseniorprojectpart.helper;

/**
 * Created by Blue on 10/31/2016.
 */

public class Message {
    private int usersId;
    private String message;
    private String sentAt;
    private String name;

    public Message(int usersId, String message, String sentAt, String name) {
        this.usersId = usersId;
        this.message = message;
        this.sentAt = sentAt;
        this.name = name;
    }

    public int getUsersId() {
        return usersId;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getName() {
        return name;
    }
}