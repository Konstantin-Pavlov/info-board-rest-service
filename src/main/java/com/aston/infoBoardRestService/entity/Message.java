package com.aston.infoBoardRestService.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private Long id;

    private Long authorId;

    private String content;

    private String authorName;

    private LocalDateTime timestamp;

    private User user;

    public Message() {
    }

    public Message(Long authorId, String content, String authorName, LocalDateTime timestamp) {
        this.authorId = authorId;
        this.content = content;
        this.authorName = authorName;
        this.timestamp = timestamp;
    }

    public Message(Long authorId, String content, String authorName, LocalDateTime timestamp, User user) {
        this.authorId = authorId;
        this.content = content;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.user = user;
    }

    public Message(Long id, Long authorId, String content, String authorName, LocalDateTime timestamp, User user) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(authorId, message.authorId) && Objects.equals(content, message.content) && Objects.equals(authorName, message.authorName) && Objects.equals(timestamp, message.timestamp) && Objects.equals(user, message.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorId, content, authorName, timestamp, user);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", content='" + content + '\'' +
                ", authorName='" + authorName + '\'' +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }
}
