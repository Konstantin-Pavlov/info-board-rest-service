package com.aston.infoBoardRestService.servlet;

import com.aston.infoBoardRestService.util.DbUtil;
import com.aston.infoBoardRestService.util.TableUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/init")
public class InitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String createUsersTableMessage = "";
        String createMessagesTableMessage = "";
        String insertUsers = "users not inserted";
        String insertMessages = "messages not inserted";

        try (Connection connection = DbUtil.getInstance().getConnection()) {
            createUsersTableMessage = TableUtil.createUsersTableIfNotExists();
            createMessagesTableMessage = TableUtil.createMessagesTableIfNotExists();

            if (createUsersTableMessage.contains("created successfully")
                            || createMessagesTableMessage.contains("already exists")) {
                insertUsers = TableUtil.insertSampleUsers();
            }

            if (createUsersTableMessage.contains("created successfully")
                            || createMessagesTableMessage.contains("already exists")) {
                insertMessages = TableUtil.insertSampleMessages();
            }
        } catch (SQLException e) {
            createUsersTableMessage = "Error creating users table: " + e.getMessage();
            createMessagesTableMessage = "Error creating messages table: " + e.getMessage();
        }

        request.setAttribute("createUsersTableMessage", createUsersTableMessage);
        request.setAttribute("createMessagesTableMessage", createMessagesTableMessage);
        request.setAttribute("insertUsersMessage", insertUsers);
        request.setAttribute("insertMessagesMessage", insertMessages);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}