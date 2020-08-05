<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.Organization" %>

<html>
<head>
    <title>Resume Info</title>
    <link href="css/view.css" rel="stylesheet">
</head>
<body>
<jsp:useBean id="resume" class="ru.javawebinar.basejava.model.Resume" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName} <a href="resume?uuid=${resume.uuid}&action=edit">EDIT</a></h2>
    <div id="contacts">
        <h3>Contacts</h3>
        <c:forEach items="${resume.contacts}" var="contact">
            <p>${contact.key.title}: ${contact.value}</p>
        </c:forEach>
    </div>
    <div id="person_info">
        <c:forEach items="${resume.personInfo}" var="info_element">
            <h3>${info_element.key.title}</h3>
            <c:choose>
                <c:when test="${info_element.key.equals(SectionType.OBJECTIVE) or info_element.key.equals(SectionType.PERSONAL)}">
                        <p>${info_element.value}</p>
                </c:when>
                <c:when test="${info_element.key.equals(SectionType.ACHIEVEMENTS) or info_element.key.equals(SectionType.QUALIFICATION)}">
                        <ul>
                            <c:forEach items="${info_element.value.content}" var="element">
                                <li>${element}</li>
                            </c:forEach>
                        </ul>
                </c:when>
                <c:when test="${info_element.key.equals(SectionType.EDUCATION) or info_element.key.equals(SectionType.EXPERIENCE)}">
                    <c:forEach items="${info_element.value.content}" var="org">
                        <p>
                            <a href="${org.getHomepage().getUrl()}">${org.getHomepage().getName()}</a><br>
                            <ul style="list-style-type: none"></ul>
                            <c:forEach items="${org.getPositions()}" var="position">
                                <li>
                                    ${position.getStartDate().toString()} - ${position.getEndDate().toString()}<br>
                                        <strong>${position.getTitle()}.</strong><br>
                                    ${position.getDescription()}
                                </li>
                            </c:forEach>
                        </p>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>EXPECTED BLOCK</p>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>