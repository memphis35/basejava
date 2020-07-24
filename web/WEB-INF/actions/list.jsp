<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link href="css/list.css" rel="stylesheet">
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table>
        <tr>
            <th>Full name</th>
            <c:forEach items="${ContactType.values()}" var="contact">
            <th>${contact.title}</th>
            </c:forEach>
        </tr>
        <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="element" class="ru.javawebinar.basejava.model.Resume"/>
        <tr>
            <td>
                <a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a>
            </td>
            <c:forEach items="${ContactType.values()}" var="contact">
            <td>
                ${resume.contacts.get(contact)}
            </td>
            </c:forEach>
            <td>
                <a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a>
            </td>
            <td>
                <a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png"></a>
            </td>
        </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
