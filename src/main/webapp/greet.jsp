<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 29.12.2023
  Time: 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>greet</title>
    <style>
        pre.highlight {
            background-color: #b4c8e7; /* Light blue background color */
            color: #9b5050; /* Dark gray text color */
            padding: 10px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<% String name = request.getParameter("name");  %>

<%="<h1>greetings, " + name + "</h1>"%>
<p>in gsp file we can access the request variable - <code>String name = request.getParameter("name");</code>
    so that's  how we got the name guest (link should be http://localhost:8080/greeting.jsp?name=guest) </p>

<p>also you can see another example of the same thing done in servlet class via this <a href="http://localhost:8080/GreetServlet?name=Tom&surname=Hanks">link</a><p>
<br>
<p>this file name is greet.jsp, but url is greeting.jsp, we can do it in web.xml using mapping</p>


<pre class="highlight">
    &lt;servlet&gt;
        &lt;servlet-name&gt;greet&lt;/servlet-name&gt;
        &lt;jsp-file&gt;/greet.jsp&lt;/jsp-file&gt;
    &lt;/servlet&gt;
    &lt;servlet-mapping&gt;
        &lt;servlet-name&gt;greet&lt;/servlet-name&gt;
        &lt;url-pattern&gt;/greeting.jsp&lt;/url-pattern&gt;
    &lt;/servlet-mapping&gt;
</pre>

<br> <a href="http://localhost:8080/">homepage</a> <br>

</body>
</html>
