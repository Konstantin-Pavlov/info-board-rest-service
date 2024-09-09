package com.aston.infoBoardRestService;

import com.aston.infoBoardRestService.util.DbUtil;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
        try (Connection connection = DbUtil.getInstance().getConnection()) {
            out.println("<p>database loaded</p>");

            Statement statement = connection.createStatement();

            // Create users table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL)";
            statement.executeUpdate(createTableSQL);


            out.println("<p>database created</p>");
            // Insert 2-3 records into users table
//            String insertRecordsSQL = "INSERT INTO users (username, email) VALUES " +
//                    "('user1', 'user1@example.com'), " +
//                    "('user2', 'user2@example.com'), " +
//                    "('user3', 'user3@example.com')";
//            statement.executeUpdate(insertRecordsSQL);

        } catch (SQLException e) {
            out.println("<p>database failed to load</p>");
            e.printStackTrace();
        }

        out.println("<br>");
        out.println("<br>");

        out.println("<br>  <a href=\"http://localhost:8080/\"\">homepage</a> <br>\n");
        out.println("</html>");
    }
}
