package com.aston.infoBoardRestService.servlet;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.dto.UserMessagesDto;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.service.impl.MessageServiceImpl;
import com.aston.infoBoardRestService.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "messageBoardServlet", value = "/api/message-board")
public class MessageBoardServlet extends HttpServlet {
    private final MessageService messageService = new MessageServiceImpl();
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<UserDto> users = userService.getAllUsers();
            List<UserMessagesDto> userMessagesDtos = new ArrayList<>();

            for (UserDto userDto : users) {
                try {
                    List<MessageDto> messages = messageService.getMessagesByAuthorId(userDto.getId());
                    userMessagesDtos.add(new UserMessagesDto(userDto, messages));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            // Set the userMessagesDtos list as a request attribute
            req.setAttribute("userMessagesDtos", userMessagesDtos);

            // Forward the request to the JSP page
            req.getRequestDispatcher("/messageBoard.jsp").forward(req, resp);

        } catch (SQLException e) {
            resp.getWriter().println("<p>Error retrieving messages: " + e.getMessage() + "</p>");
        }
    }
}
