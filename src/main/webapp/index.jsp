<%@ page import="com.aston.infoBoardRestService.util.TableUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String createUsersTableMessage = (String) request.getAttribute("createUsersTableMessage");
    String createMessagesTableMessage = (String) request.getAttribute("createMessagesTableMessage");
    String insertUsersMessage = (String) request.getAttribute("insertUsersMessage");
    String insertMessagesMessage = (String) request.getAttribute("insertMessagesMessage");

    boolean needsRedirect = createUsersTableMessage == null || createMessagesTableMessage == null;

%>
<html>
<head>
    <title>Start Page</title>
    <script type="text/javascript">
        function redirectToInit() {
            window.location.href = "init";
        }
    </script>
</head>
<body>
<h1>Home Page</h1>
<p>Loading JDBC driver... <%= TableUtil.loadJdbcDriver() %></p>
<p>
    <%= createUsersTableMessage != null ? createUsersTableMessage : "Checking if users table exists and create if needed ..." %>
</p>
<p>
    <%= createMessagesTableMessage != null ? createMessagesTableMessage : "Checking if messages table exists and create if needed ..." %>
</p>
<p>
    <%= insertUsersMessage != null ? insertUsersMessage : "" %>
</p>
<p>
    <%= insertMessagesMessage != null ? insertMessagesMessage : "" %>
</p>

<%
    if (needsRedirect) {
%>
<script type="text/javascript">
    setTimeout(redirectToInit, 1000); // Redirect after 2 seconds
</script>
<%
    }
%>

<br> <a href="http://localhost:8080/init">Reload InitServlet</a> <br>
<br> <a href="http://localhost:8080/hello-servlet">Visit hello-servlet</a> <br>
<br> <a href="http://localhost:8080/firstJsp.jsp">Visit firstJsp to see current time</a> <br>
<br> <a href="http://localhost:8080/greeting.jsp?name=guest">Visit greeting page</a> <br>
<br> <a href="http://localhost:8080/GreetServlet?name=Tom&surname=Hanks">Visit greet servlet page</a> <br>
<br> <a href="http://localhost:8080/library-servlet">Library servlet using JDBC drivers</a> <br>
</body>
</html>