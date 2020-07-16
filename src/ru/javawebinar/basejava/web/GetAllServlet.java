package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetAllServlet extends HttpServlet {

    private final Storage storage = new SqlStorage();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Resume> result = storage.getAllSorted();
        resp.getWriter().write("<table style=\"margin: 10px auto\">");
        resp.getWriter().write("<tr><td>UUID</td><td>FULL NAME</td></tr>");
        for (Resume resume : result) {
            resp.getWriter().write("<tr>");
            resp.getWriter().write("<td style=\"background: lightblue; padding: 5px; margin: 5px;\">" + resume.getUuid() + "</td>");
            resp.getWriter().write("<td style=\"background: lightblue; padding: 5px; margin: 5px;\">" + resume.getFullName() + "</td>");
            resp.getWriter().write("</tr>");
        }
        resp.getWriter().write("</table>");
    }
}
