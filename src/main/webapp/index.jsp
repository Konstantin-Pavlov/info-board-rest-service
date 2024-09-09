<%--
https://www.youtube.com/playlist?list=PLAma_mKffTOTTFqIkLXgHqVuL6xJhb0mr - playlist
  Created by IntelliJ IDEA.
  User: Admin
  Date: 29.12.2023
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.aston.infoBoardRestService.util.TableUtil" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>start page</title>
    </head>

    <body>
        <p>
            <br>
        <h1>home page</h1> <br>

<%--        <%--%>
<%--            String loadJDOBCDriver = TableUtil.loadJdbcDriver();--%>
<%--            String createUsersTableMessage = TableUtil.createUsersTableIfNotExists();--%>
<%--            String createMessagesTableMessage = TableUtil.createMessagesTableIfNotExists();--%>
<%--        %>--%>

        <p>loading jdbc driver.......<%= TableUtil.loadJdbcDriver()%></p>

        <%
            PrintWriter printWriter = response.getWriter();
            String createUsersTableMessage = (String) request.getAttribute("createUsersTableMessage");
            String createMessagesTableMessage = (String) request.getAttribute("createMessagesTableMessage");

            if (createUsersTableMessage == null || createMessagesTableMessage == null) {
                if (createUsersTableMessage == null) {
                    printWriter.print("creating users table..");
                }
                if (createMessagesTableMessage == null) {
                    printWriter.print("creating messages table..");
                }
                response.sendRedirect("init");
                return;
            }
        %>

        <p>checking if user table exists and create if needed.......<%= request.getAttribute("createUsersTableMessage") %></p>
        <p>checking if messages table exists and create if needed.......<%= request.getAttribute("createMessagesTableMessage") %></p>

        <br> <a href="http://localhost:8080/init">Reload InitServlet</a> <br>
        <br> <a href="http://localhost:8080/hello-servlet">Visit hello-servlet</a> <br>
        <br> <a href="http://localhost:8080/firstJsp.jsp">Visit firstJsp to see current time</a> <br>
        <br> <a href="http://localhost:8080/greeting.jsp?name=guest">Visit greeting page</a> <br>
        <br> <a href="http://localhost:8080/GreetServlet?name=Tom&surname=Hanks">Visit greet servlet page</a> <br>
        <br> <a href="http://localhost:8080/library-servlet">library servlet using jdbc drivers</a> <br>

        </p>
    </body>
</html>
