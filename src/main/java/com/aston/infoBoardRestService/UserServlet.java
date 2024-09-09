package com.aston.infoBoardRestService;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.service.impl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "user servlet", value = "/user-servlet")
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<h1>user list servlet using jdbc drivers</h1>");

        try {
            List<UserDto> users = userService.getAllUsers();
            out.println("<ul>");
            for (UserDto user : users) {
                out.println("<li>" + user.getName() + " (" + user.getEmail() + ")</li>");
            }
            out.println("</ul>");
        } catch (SQLException e) {
            out.println("<p>Error retrieving users: " + e.getMessage() + "</p>");
        }

        out.println("<br>");

        out.println("<br>  <a href=\"http://localhost:8080/\"\">homepage</a> <br>\n");
        out.println("</html>");
    }
}
