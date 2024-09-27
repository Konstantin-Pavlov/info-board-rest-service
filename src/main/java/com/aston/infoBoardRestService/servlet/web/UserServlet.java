package com.aston.infoBoardRestService.servlet.web;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
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
import java.util.logging.Logger;

@WebServlet(name = "user servlet", urlPatterns = {"/users/*"})
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final Logger logger = Logger.getLogger(UserServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<h1>User Servlet</h1>");

        String pathInfo = req.getPathInfo(); // Get the path info after /user-servlet
        if (pathInfo != null && pathInfo.length() > 1) {
            String param = pathInfo.substring(1); // Remove the leading "/"
            try {
                if (param.contains("@")) {
                    // Assume it's an email
                    UserDto user = userService.getUserByEmail(param);
                    if (user != null) {
                        out.println(String.format("<p>User <b>%s</b> with email <b>%s</b> found", user.getName(), user.getEmail()));
                    } else {
                        out.println("<p>No user found with email: " + param + "</p>");
                    }
                } else {
                    // Assume it's an ID
                    Long id = Long.parseLong(param);
                    UserDto user = userService.getUserById(id);
                    if (user != null) {
                        out.println(String.format("<p>User <b>%s</b> with id <b>%d</b> found", user.getName(), user.getId()));
                    } else {
                        out.println("<p>No user found with ID: " + id + "</p>");
                    }
                }
            } catch (UserNotFoundException e) {
                logger.warning(e.getMessage());
                out.println(String.format("<p> Error retrieving user <b>%s</b>", e.getMessage()));
            } catch (SQLException e) {
                logger.warning(e.getMessage());
                out.println("<p>Error retrieving user: " + e.getMessage() + "</p>");
            } catch (NumberFormatException e) {
                logger.warning(e.getMessage());
                out.println("<p>Invalid ID format: " + param + "</p>");
            }
        } else {
            // No specific user requested, return all users
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
        }

        out.println("<br>");
        out.println("<br>  <a href=\"http://localhost:8080/info-board/\"\">homepage</a> <br>\n");
        out.println("</html>");
    }
}
