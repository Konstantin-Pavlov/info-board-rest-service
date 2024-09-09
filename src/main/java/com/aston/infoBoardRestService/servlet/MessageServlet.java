package com.aston.infoBoardRestService.servlet;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.service.impl.MessageServiceImpl;

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

@WebServlet("/api/messages/*")
public class MessageServlet extends HttpServlet {

    private final MessageService messageService = new MessageServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<MessageDto> messageDtos = null;
            try {
                messageDtos = messageService.getAllMessages();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(messageDtos);
            out.flush();
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                Long id = Long.parseLong(splits[1]);
                MessageDto messageDTO = null;
                try {
                    messageDTO = messageService.getMessage(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (messageDTO != null) {
                    resp.setContentType("application/json");
                    PrintWriter out = resp.getWriter();
                    out.print(messageDTO);
                    out.flush();
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MessageDto messageDTO = new MessageDto();
        messageDTO.setAuthorId(Long.parseLong(req.getParameter("authorId")));
        messageDTO.setContent(req.getParameter("content"));
        messageDTO.setAuthorName(req.getParameter("authorName"));
        messageDTO.setTimestamp(LocalDateTime.now());

        try {
            messageService.saveMessage(messageDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(messageDTO);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long id = Long.parseLong(splits[1]);
        MessageDto messageDTO = new MessageDto();
        messageDTO.setId(id);
        messageDTO.setAuthorId(Long.parseLong(req.getParameter("authorId")));
        messageDTO.setContent(req.getParameter("content"));
        messageDTO.setAuthorName(req.getParameter("authorName"));
        messageDTO.setTimestamp(LocalDateTime.now());

        try {
            messageService.updateMessage(messageDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(messageDTO);
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long id = Long.parseLong(splits[1]);
        try {
            messageService.deleteMessage(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}