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

@WebServlet(name = "generator servlet", value = "/generator")
public class GeneratorServlet  extends HttpServlet {
    private final Random random = new Random();
    private final UserSaver userSaver = new UserSaver();
    private final MessageSaver messageSaver = new MessageSaver();
    private final Logger logger = Logger.getLogger(UserSaver.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<h1>generator servlet</h1>");

        int numberUsersToGenerate = random.nextInt(3) + 1;

        userSaver.generateAndSaveUsers(numberUsersToGenerate);
        out.println(String.format("<h2>number of users generated: %d</h2>", numberUsersToGenerate));
        try {
            messageSaver.generateAndSaveMessagesForUsers();
            out.println("<h2>generated messages for all users</h2>");
        } catch (SQLException e) {
            out.println("<h2>fail to generate messages</h2>");
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }


        out.println("<br>  <a href=\"http://localhost:8080/\"\">homepage</a> <br>\n");
        out.println("</html>");

    }
}
