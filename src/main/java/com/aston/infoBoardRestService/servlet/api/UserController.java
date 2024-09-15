package com.aston.infoBoardRestService.servlet.api;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.service.impl.UserServiceImpl;
import com.aston.infoBoardRestService.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "user controller", urlPatterns = {"/api/users/*"})
public class UserController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());
    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper;

    public UserController() {
        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        this.objectMapper.registerModule(javaTimeModule);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo(); // Get the path info after /user-servlet
        if (pathInfo != null && pathInfo.length() > 1) {
            String param = pathInfo.substring(1); // Remove the leading "/"
            try {
                if (param.contains("@")) {
                    // Assume it's an email
                    UserDto user = userService.getUserByEmail(param);
                    if (user != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(resp.getWriter(), user);
                    } else {
                        logger.warning("User with email " + param + " not found");
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().print(String.format("User with email %s not found", param));
                    }
                } else {
                    // Assume it's an ID
                    Long id = Long.parseLong(param);
                    UserDto user = userService.getUserById(id);
                    if (user != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(resp.getWriter(), user);
                    } else {
                        logger.warning("User with id " + id + " not found");
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().print(String.format("User with id %s not found", id));
                    }
                }
            } catch (SQLException e) {
                logger.warning(String.format("error while getting user: %s", e.getMessage()));
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print("{\"message\": \"Error retrieving user: " + e.getMessage() + "\"}");
            } catch (NumberFormatException e) {
                logger.warning(String.format("invalid id format: %s", param));
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\": \"Invalid ID format: " + param + "\"}");
            }
        } else {
            // No specific user requested, return all users
            try {
                List<UserDto> users = userService.getAllUsers();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), users);
            } catch (SQLException e) {
                logger.warning(String.format("error while getting users: %s", e.getMessage()));
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"message\": \"Error retrieving users: " + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Parse the request body to get the UserDto
            UserDto userDto = objectMapper.readValue(req.getReader(), UserDto.class);

            // Save the user using the UserService
            boolean isUserSaved = userService.saveUser(userDto);

            if (isUserSaved) {
                resp.setStatus(HttpServletResponse.SC_CREATED); // HTTP 201 Created
                objectMapper.writeValue(resp.getWriter(), userDto); // Return the saved user
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // HTTP 400 Bad Request
                resp.getWriter().write("{\"message\": \"User could not be saved\"}");
            }
        } catch (SQLException e) {
            logger.warning(String.format("error while saving user: %s", e.getMessage()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Error saving user: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            logger.warning(String.format("error in request: %s", e.getMessage()));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Invalid request data\"}");
        }
    }

}
