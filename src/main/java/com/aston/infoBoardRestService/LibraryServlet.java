package com.aston.infoBoardRestService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "library servlet", value = "/library-servlet")
public class LibraryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<h1>library servlet using jdbc drivers</h1>");
        try {
            Class.forName("org.postgresql.Driver");
            out.println("<p>success - jdbc driver loaded</p>");
        } catch (ClassNotFoundException e) {
            out.println("<p>jdbc driver failed to load</p>");
            e.printStackTrace();
        }
// jdbc:postgresql://localhost:5432/postgres
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "123");
            out.println("<p>database loaded</p>");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id, author_id, year, name from library.book");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Author ID</th><th>Year</th><th>Name</th></tr>");

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String author_id = resultSet.getString("author_id");
                String year = resultSet.getString("year");
                String name = resultSet.getString("name");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + author_id + "</td>");
                out.println("<td>" + year + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            statement.close();
        } catch (SQLException e) {
            out.println("<p>database failed to load</p>");
            e.printStackTrace();
        }

        out.println("<br>");
        out.println("<br>");

        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "123");
            Statement statement = connection.createStatement();
            // Execute the SQL query and retrieve the results
            ResultSet resultSet = statement.executeQuery(
                    "SELECT author.name, author.surname, book.name, book.year " +
                            "FROM library.author " +
                            "JOIN library.book ON book.author_id = author.id " +
                            "ORDER BY author.name"
            );
            out.println("<p>database loaded</p>");
            out.println("<table style='border-collapse: separate; border-spacing: 0 10px;'>");
            out.println("<tr><th>Author Name</th><th>Author Surname</th><th>Book Name</th><th>Year</th></tr>");

            // Iterate over the result set and populate the table rows
            while (resultSet.next()) {
                String authorName = resultSet.getString("name");
                String authorSurname = resultSet.getString("surname");
                String bookName = resultSet.getString("name");
                String year = resultSet.getString("year");

                out.println("<tr>");
                out.println("<td>" + authorName + "</td>");
                out.println("<td>" + authorSurname + "</td>");
                out.println("<td>" + bookName + "</td>");
                out.println("<td>" + year + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
        } catch (SQLException e) {
            out.println("<p>database failed to load</p>");
            e.printStackTrace();
        }

        out.println("<br>  <a href=\"http://localhost:8080/\"\">homepage</a> <br>\n");
        out.println("</html>");
    }
}
