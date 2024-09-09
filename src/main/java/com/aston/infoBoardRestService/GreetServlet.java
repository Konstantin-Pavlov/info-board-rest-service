package com.aston.infoBoardRestService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GreetServlet", value = "/GreetServlet")
public class GreetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");

        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>greet page</title>\n" +
                "</head>");

        out.println("<body>\n");
        out.printf("<h1>hello %s %s!</h1>%n", name, surname);
        out.println("<br>");
        out.println("<p>code example to get vars from url</p>");
        out.println("<pre><code>" +
                "public class GreetServlet extends HttpServlet {\n" +
                "    @Override\n" +
                "    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {\n" +
                "        String name = req.getParameter(\"name\");\n" +
                "        String surname = req.getParameter(\"surname\");\n" +
                "        PrintWriter out = resp.getWriter();" +
                "\n}" +
                "</code></pre>");
        out.println("<p>example of url with params: http://localhost:8080/GreetServlet?name=Tom&surname=Hanks</p>");
        out.println("</body>");
        out.println("</html>");
        out.println("<br>  <a href=\"http://localhost:8080/\">homepage</a> <br>\n");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
