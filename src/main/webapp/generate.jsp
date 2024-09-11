<%@ page import="com.aston.infoBoardRestService.util.TableUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String dbStatusMessage = "";
    String generateDataMessage = "";
    Boolean tablesExist = (Boolean) session.getAttribute("tablesExist");

    if (tablesExist == null) {
        tablesExist = false;
    }

    if ("checkTables".equals(request.getParameter("action"))) {
        String usersTableStatus = TableUtil.createUsersTableIfNotExists();
        String messagesTableStatus = TableUtil.createMessagesTableIfNotExists();

        if (usersTableStatus.contains("already exists") && messagesTableStatus.contains("already exists")) {
            tablesExist = true;
        }
        dbStatusMessage = usersTableStatus + "<br>" + messagesTableStatus;
        session.setAttribute("tablesExist", tablesExist);
    }

    if ("generate".equals(request.getParameter("action"))) {
        generateDataMessage = TableUtil.insertSampleUsers();
        generateDataMessage += "<br>" + TableUtil.insertSampleMessages();
    }

    String userGenerationMessage = (String) request.getAttribute("userGenerationMessage");
    String messageGenerationMessage = (String) request.getAttribute("messageGenerationMessage");
%>
<html>
<head>
    <title>Initialization Page</title>
</head>
<body>
<h1>Database Initialization</h1>
<p><%= dbStatusMessage %></p>
<%
    if (!tablesExist) {
%>
<form method="post">
    <input type="hidden" name="action" value="checkTables">
    <button type="submit">Check and Create Tables</button>
</form>
<%
} else {
%>
<form method="post">
    <input type="hidden" name="action" value="generate">
    <button type="submit">Generate Users and Messages</button>
</form>
<p><%= generateDataMessage %></p>
<form method="get" action="generator">
    <label for="numUsers">Enter number of users to generate:</label>
    <input type="number" id="numUsers" name="numUsers" min="1" required>
    <button type="submit">Generate Custom Amount of Users</button>
</form>
<%
    }
%>
<%
    if (userGenerationMessage != null || messageGenerationMessage != null) {
%>
<h2>Generation Results</h2>
<p><%= userGenerationMessage %></p>
<p><%= messageGenerationMessage %></p>
<br>
<%
    }
%>
<a href="http://localhost:8080/">Homepage</a>
</body>
</html>