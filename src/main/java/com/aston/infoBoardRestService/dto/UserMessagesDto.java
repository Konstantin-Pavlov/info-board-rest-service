package com.aston.infoBoardRestService.dto;

import java.util.List;

public class UserMessagesDto {
    private UserDto user;
    private List<MessageDto> messages;

    // Getters and Setters
    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }
}