package com.aston.infoBoardRestService.servlet.api;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "user controller", urlPatterns = {"/api/users/*"})
public class UserController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());
    private UserService userService;
    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(
                    new JavaTimeModule()
                            .addSerializer(
                                    LocalDateTime.class, new LocalDateTimeSerializer()
                            )
            );

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.userService = (UserService) servletConfig.getServletContext().getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            } catch (UserNotFoundException e) {
                logger.warning(String.format("error: %s", e.getMessage()));
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().print("{\"message\": \"Error retrieving user: " + e.getMessage() + "\"}");
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
                if (users.isEmpty()) {
                    logger.warning("No Users found");
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    logger.info(String.format("Found %d users", users.size()));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    objectMapper.writeValue(resp.getWriter(), users);
                }

            } catch (SQLException e) {
                logger.warning(String.format("error while getting users: %s", e.getMessage()));
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"message\": \"Error retrieving users: " + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Read the request body
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Parse the JSON input
        String jsonString = jsonBuffer.toString();
        UserDto userDto;
        try {
            userDto = objectMapper.readValue(jsonString, UserDto.class);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Invalid JSON format\"}");
            return;
        }

        // Validate the input
        if (userDto.getEmail() == null || userDto.getName() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Invalid input: email and name are required\"}");
            return;
        }

        boolean isSaved;

        try {
            isSaved = userService.saveUser(userDto);
        } catch (SQLException e) {
            logger.warning(String.format("Error while saving user with email %s; error message: %s", userDto.getEmail(), e.getMessage()));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Error saving user: " + e.getMessage() + "\"}");
            return;
        }

        if (isSaved) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"User saved successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Failed to save user\"}");
        }

    }

}
