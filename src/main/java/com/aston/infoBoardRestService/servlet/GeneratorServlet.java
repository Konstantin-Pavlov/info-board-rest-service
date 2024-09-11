package com.aston.infoBoardRestService.servlet;

import com.aston.infoBoardRestService.util.MessageSaver;
import com.aston.infoBoardRestService.util.UserGenerator;
import com.aston.infoBoardRestService.util.UserSaver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Logger;

@WebServlet(name = "generator servlet", urlPatterns = {"/generator"})
public class GeneratorServlet  extends HttpServlet {
    private final Random random = new Random();
    private final UserSaver userSaver = new UserSaver();
    private final MessageSaver messageSaver = new MessageSaver();
    private final Logger logger = Logger.getLogger(UserSaver.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String numUsersParam = req.getParameter("numUsers");
        int numberUsersToGenerate;

        if (numUsersParam != null) {
            numberUsersToGenerate = Integer.parseInt(numUsersParam);
        } else {
            numberUsersToGenerate = random.nextInt(3) + 1;
        }

        String userGenerationMessage;
        String messageGenerationMessage;

        try {
            userSaver.generateAndSaveUsers(numberUsersToGenerate);
            userGenerationMessage = String.format("Number of users generated: %d", numberUsersToGenerate);
        } catch (Exception e) {
            userGenerationMessage = "Failed to generate users: " + e.getMessage();
            logger.severe(e.getMessage());
        }

        try {
            messageSaver.generateAndSaveMessagesForUsers();
            messageGenerationMessage = "Generated messages for all users";
        } catch (SQLException e) {
            messageGenerationMessage = "Failed to generate messages: " + e.getMessage();
            logger.severe(e.getMessage());
        }

        req.setAttribute("userGenerationMessage", userGenerationMessage);
        req.setAttribute("messageGenerationMessage", messageGenerationMessage);

        req.getRequestDispatcher("/generate.jsp").forward(req, resp);

    }
}
