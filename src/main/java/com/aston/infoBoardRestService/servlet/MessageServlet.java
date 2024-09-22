package com.aston.infoBoardRestService.servlet;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.service.impl.MessageServiceImpl;
import com.aston.infoBoardRestService.util.DateTimeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "message servlet", value = "/messages")
public class MessageServlet extends HttpServlet {

    private final MessageService messageService = new MessageServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Message List servlet</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }");
        out.println("h1 { color: #333; }");
        out.println("ul { list-style-type: none; padding: 0; }");
        out.println("li { background: #fff; margin: 10px 0; padding: 15px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }");
        out.println("strong { color: #007BFF; }");
        out.println(".timestamp { font-size: 0.9em; color: #888; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Message List servlet</h1>");

        try {
            List<MessageDto> messages = messageService.getAllMessages();
            out.println("<ul>");
            for (MessageDto messageDto : messages) {
                out.println("<li>");
                out.println("<strong>Message ID:</strong> " + messageDto.getId() + "<br>");
                out.println("<strong>Author ID:</strong> " + messageDto.getAuthorId() + "<br>");
                out.println("<strong>Content:</strong> " + messageDto.getContent() + "<br>");
                out.println("<strong>Author Name:</strong> " + messageDto.getAuthorName() + "<br>");
                out.println("<span class='timestamp'>" + DateTimeUtil.getFormattedDate(messageDto.getTimestamp()) + "</span>");
                out.println("</li>");
            }
            out.println("</ul>");
        } catch (SQLException e) {
            out.println("<p>Error retrieving messages: " + e.getMessage() + "</p>");
        }

        out.println("<br>");

        out.println("<br>  <a href=\"http://localhost:8080/info-board\"\">homepage</a> <br>\n");
        out.println("</html>");
    }

}