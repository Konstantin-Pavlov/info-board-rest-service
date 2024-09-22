<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.aston.infoBoardRestService.dto.UserMessagesDto" %>
<%@ page import="com.aston.infoBoardRestService.dto.MessageDto" %>
<%@ page import="com.aston.infoBoardRestService.dto.UserDto" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Message Board</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
        h1 { color: #333; }
        .user-messages { margin-bottom: 20px; padding: 15px; background: #fff; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .message { margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        .timestamp { font-size: 0.9em; color: #888; }
    </style>
</head>
<body>
<h1>Message Board</h1>

<c:if test="${not empty userMessagesDtos}">
    <c:forEach var="userMessages" items="${userMessagesDtos}">
        <div class="user-messages">
            <h2>User: ${userMessages.user.name} (ID: ${userMessages.user.id})</h2>
            <c:forEach var="message" items="${userMessages.messages}">
                <div class="message">
                    <strong>author id:</strong> ${message.authorId}<br>
                    <strong>Content:</strong> ${message.content}<br>
                    <strong>Author Name:</strong> ${message.authorName}<br>
                    <span class="timestamp">${message.timestamp}</span>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</c:if>

<c:if test="${empty userMessagesDtos}">
    <p>No messages found.</p>
</c:if>

<br>
<a href="http://localhost:8080/info-board">Homepage</a>
</body>
</html>