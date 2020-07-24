package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResumeServlet extends HttpServlet {

    private final Storage storage = new SqlStorage();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameters = request.getParameterMap();
        String action = request.getParameter("action");
        String fullName = request.getParameter("fullName");
        final Resume resume = action.equals("update") ? new Resume(request.getParameter("uuid"), fullName) : new Resume(fullName);
        formResume(parameters, resume);
        switch (action) {
            case "update": {
                storage.update(resume);
                response.sendRedirect("resume");
                break;
            }
            case "save": {
                storage.save(resume);
                break;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/actions/list.jsp").forward(request, response);
            return;
        }
        Resume resume = new Resume();
        switch (action) {
            case "create": {
                request.getRequestDispatcher("/WEB-INF/actions/create.jsp").forward(request, response);
                break;
            }
            case "delete": {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view":
            case "edit": {
                resume = storage.get(uuid);
                break;
            }
            default:
                throw new IllegalArgumentException(action);
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                action.equals("edit") ? "/WEB-INF/actions/edit.jsp" : "/WEB-INF/actions/view.jsp").forward(request, response);
    }

    private void formResume(Map<String, String[]> parameters, Resume resume) {
        for (ContactType type : ContactType.values()) {
            String value = parameters.get(type.name())[0];
            if (!value.trim().isEmpty()) resume.addContact(type, value);
        }
        for (SectionType type : SectionType.values()) {
            switch (type) {
                case OBJECTIVE:
                case PERSONAL: {
                    String value = parameters.get(type.name())[0];
                    if (!value.trim().isEmpty()) resume.getPersonInfo().put(type, new StringSection(value));
                    break;
                }
                case ACHIEVEMENTS:
                case QUALIFICATION: {
                    String[] values = parameters.get(type.name());
                    List<String> content = new ArrayList<>();
                    for (String element : values) {
                        if (!(element.trim().length() == 0)) content.add(element);
                    }
                    if (content.size() > 0) resume.getPersonInfo().put(type, new ListSection(content));
                    break;
                }
                case EDUCATION:
                case EXPERIENCE: {
                    break;
                }
                default:
                    throw new IllegalArgumentException(type.toString());
            }
        }
    }
}
