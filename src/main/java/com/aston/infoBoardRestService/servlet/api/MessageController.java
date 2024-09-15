package com.aston.infoBoardRestService.servlet.api;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.service.impl.MessageServiceImpl;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "message controller", urlPatterns = {"/api/messages/*"})
public class MessageController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());
    private final MessageService messageService = new MessageServiceImpl();
    private final ObjectMapper objectMapper;

    public MessageController() {
        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        this.objectMapper.registerModule(javaTimeModule);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            String param = pathInfo.substring(1); // Remove the leading "/"
            try {
                if (param.contains("@")) {
                    // Assume it's an email
                    List<MessageDto> messages = messageService.getMessagesByAuthorEmail(param);
                    if (messages != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(resp.getWriter(), messages);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().print(String.format("messages for user with email %s not found", param));
                    }
                } else if (param.contains("get-by-author-id")) {
                    Long authorId = Long.parseLong(param.split("/")[1]);
                    // Assume it's an user id
                    List<MessageDto> messages = messageService.getMessagesByAuthorId(authorId);
                    if (messages != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(resp.getWriter(), messages);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().print(String.format("messages for user with id %d not found", authorId));
                    }
                } else {
                    // Assume it's an ID
                    Long id = Long.parseLong(param);
                    MessageDto messageDto = messageService.getMessageById(id);
                    if (messageDto != null) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(resp.getWriter(), messageDto);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().print(String.format("message with id %s not found", id));
                    }
                }
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print("{\"message\": \"Error retrieving message: " + e.getMessage() + "\"}");
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\": \"Invalid ID format: " + param + "\"}");
            }
        } else {
            // No specific message requested, return all messages
            try {
                List<MessageDto> messages = messageService.getAllMessages();
                logger.log(Level.INFO, "Retrieved messages: {0}", objectMapper.writeValueAsString(messages.size()));
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), messages);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error retrieving messages", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print("{\"message\": \"Error retrieving messages: " + e.getMessage() + "\"}");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "Invalid ID format", e);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                assert pathInfo != null;
                resp.getWriter().write("{\"message\": \"Invalid ID format: " + pathInfo.substring(1) + "\"}");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print("{\"message\": \"Unexpected error: " + e.getMessage() + "\"}");
            } finally {
                resp.getWriter().flush();
                resp.getWriter().close();
            }
        }
    }
}
