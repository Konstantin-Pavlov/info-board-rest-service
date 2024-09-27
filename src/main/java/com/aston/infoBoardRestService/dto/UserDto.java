package com.aston.infoBoardRestService.dto;

import com.aston.infoBoardRestService.entity.Message;

import java.util.List;
import java.util.Objects;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Message> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(name, userDto.name) && Objects.equals(email, userDto.email) && Objects.equals(messages, userDto.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, messages);
    }
}