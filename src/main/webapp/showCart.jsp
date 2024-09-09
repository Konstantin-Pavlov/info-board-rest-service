<%@ page import="org.example.javaeeexamle.Cart" %><%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 21.01.2024
  Time: 19:03
  To change this template use File | Settings | File Templates.
  how to show name, surname??
  session.getAttribute("name") - returns null
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>show cart content</title>
</head>
<body>
<% Cart cart = (Cart) session.getAttribute("cart"); %>
<p>
    you visited cart page: <b><%=session.getAttribute("sessionCounter")%></b>  times
</p>
<p>
    user: <%= session.getAttribute("name")%>  <%= session.getAttribute("surname")  %>
</p>
<p>
    product name: <%= cart.getProductName() %>
</p>
<p>
    product quantity: <%= cart.getQuantity() %>
</p>

<p>
    <br> <a href="http://localhost:8080/">homepage</a> <br>
</p>

</body>
</html>
