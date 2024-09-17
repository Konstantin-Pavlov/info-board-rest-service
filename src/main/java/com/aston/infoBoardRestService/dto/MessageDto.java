package com.aston.infoBoardRestService.dto;

import com.aston.infoBoardRestService.entity.User;

import java.time.LocalDateTime;

public class MessageDto {
    private Long id;

    private Long authorId;

    private String content;

    private String authorName;

    private LocalDateTime timestamp;

    private User user;

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
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", content='" + content + '\'' +
                ", authorName='" + authorName + '\'' +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }
}
