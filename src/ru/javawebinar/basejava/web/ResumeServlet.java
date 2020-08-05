package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                break;
            }
            case "save": {
                storage.save(resume);
                break;
            }
        }
        response.sendRedirect("resume");
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
            String[] values = parameters.get(type.name());
            switch (type) {
                case OBJECTIVE:
                case PERSONAL: {
                    if (!values[0].trim().isEmpty()) resume.getPersonInfo().put(type, new StringSection(values[0]));
                    break;
                }
                case ACHIEVEMENTS:
                case QUALIFICATION: {
                    List<String> content = new ArrayList<>();
                    for (String element : values) {
                        if (!(element.trim().length() == 0)) content.add(element);
                    }
                    if (content.size() > 0) resume.getPersonInfo().put(type, new ListSection(content));
                    break;
                }
                case EDUCATION:
                case EXPERIENCE: {
                    if (values == null) break;
                    List<Organization> orgs = new ArrayList<>();
                    for (int i = 0, org_id = 0; i < values.length; i += 2) {
                        if (values[i].trim().isEmpty()) continue;
                        orgs.add(new Organization(new Link(values[i], values[i+1].trim().isEmpty() ? null : values[i+1])));
                        String[] positions = parameters.get(orgs.get(org_id).getHomepage().getName());
                        if (positions == null) continue;
                        for (int j = 0; j < positions.length; j = j + 4) {
                            String title = positions[j];
                            LocalDate start_date = LocalDate.parse(positions[j+1], DateTimeFormatter.ISO_LOCAL_DATE);
                            LocalDate end_date = LocalDate.parse(positions[j+2], DateTimeFormatter.ISO_LOCAL_DATE);
                            String description = positions[j+3];
                            orgs.get(org_id).addPosition(title, description.trim().isEmpty() ? null : description, start_date, end_date);
                        }
                        org_id++;
                    }
                    if (orgs.size() > 0) resume.getPersonInfo().put(type, new OrganizationSection(orgs));
                break;
                }
                default:
                    throw new IllegalArgumentException(type.toString());
            }
        }
    }
}
