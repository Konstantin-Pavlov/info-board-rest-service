package com.aston.infoBoardRestService.servlet.api;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.service.impl.MessageServiceImpl;
import com.aston.infoBoardRestService.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "message controller", urlPatterns = {"/api/messages/*"})
public class MessageController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());
    private MessageService messageService;
    private final ObjectMapper objectMapper;

    public MessageController() {
        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        this.objectMapper.registerModule(javaTimeModule);
    }

    @Override
    public void init() {
        this.messageService = new MessageServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        MessageDto messageDto;
        try {
            messageDto = objectMapper.readValue(jsonString, MessageDto.class);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Invalid JSON format\"}");
            return;
        }

        // Validate the input
        if (messageDto.getAuthorId() == null
                || messageDto.getContent() == null
                || messageDto.getAuthorName() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Author id, author name, and content are required\"}");
            return;
        }

        // Save the message
        boolean isSaved;
        try {
            messageDto.setTimestamp(LocalDateTime.now());
            isSaved = messageService.saveMessage(messageDto);
        } catch (SQLException e) {
            logger.warning(String.format("Error while saving message: %s", messageDto));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Error saving message: " + e.getMessage() + "\"}");
            return;
        }

        // Respond to the client
        if (isSaved) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"message saved successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Failed to save message\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo(); // Get the path info after /message-servlet
        if (pathInfo != null && pathInfo.length() > 1) {
            String param = pathInfo.substring(1); // Remove the leading "/"
            try {
                Long messageId = Long.parseLong(param);
                // Assume it's an email
                if (messageService.deleteMessage(messageId)) {
                    logger.info(String.format("message with id %s deleted successfully", param));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().print(String.format("message with id %s deleted successfully", param));
                } else {
                    logger.warning(String.format("Message with id %s not found", param));
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().print(String.format("Message with id %s not found", param));
                }
            }catch (NumberFormatException e){
                logger.warning(String.format(
                        "Bad request for delete method; should be a number (message id), instead got: %s", pathInfo
                ));
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(String.format(
                        "Bad request for delete method; should be a number (message id), instead got: %s", pathInfo
                ));
            } catch (SQLException e) {
                logger.warning(String.format("error while getting message: %s", e.getMessage()));
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print("{\"message\": \"Error retrieving message: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                logger.warning(String.format("exception: %s", e.getMessage()));
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(String.format("exception: %s", e.getMessage()));
            }
        } else {
            logger.warning(String.format("empty request for delete method: %s", pathInfo));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(String.format("empty or bad request for delete method: <%s>. Should provide id for message you want to delete", pathInfo));
        }
    }
}
