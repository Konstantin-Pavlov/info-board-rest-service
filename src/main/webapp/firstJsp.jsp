<%--это директивы--%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="static com.aston.infoBoardRestService.util.StringUtils.openPTag" %>
<%@ page import="static com.aston.infoBoardRestService.util.StringUtils.openStrongTag" %>
<%@ page import="static com.aston.infoBoardRestService.util.StringUtils.*" %>

<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 29.12.2023
  Time: 1:16
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>First JSP</title>
</head>
<body>


<h1 style="background-color:rgb(255, 99, 71);">Testing JSP</h1>
<p>
    <%
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        String message = now.format(dateTimeFormatter);
//        String openPTag = "<p style=\"background-color:hsla(79, 76%, 56%, 0.5);\">";
//        String closePTag = "</p>";
//        String openStrongTag = "<strong>";
//        String closeStrongTag = "</strong>";
        for (LocalDateTime cur = now; cur.isBefore(now.plusMinutes(5)); cur = cur.plusMinutes(1)) {
            out.println(openPTag + openStrongTag + cur.format(dateTimeFormatter) + closeStrongTag + closePTag);
        }
    %>
    <%= "hello jsp\n" %><br>
    <%= "current time: " + message %><br>
    <br> <a href="http://localhost:8080/info-board/">homepage</a> <br>

</p>

</body>
</html>
