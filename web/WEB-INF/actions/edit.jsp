<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<html>
<head>
    <title>Title</title>
    <link href="css/edit.css" rel="stylesheet"/>
    <script src="javascript/button.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<jsp:useBean id="resume" class="ru.javawebinar.basejava.model.Resume" scope="request"/>
<section>
    <form action="resume?uuid=${resume.uuid}&action=update" method="POST">
        <fieldset class="main">
            <legend>MAIN INFO</legend>
            <table>
                <tr>
                    <td class="title">FULL NAME</td>
                    <td><input class="name" type="text" name="fullName" value="${resume.fullName}"/></td>
                </tr>
                <tr>
                    <td class="title">UUID</td>
                    <td><input class="uuid" type="text" name="uuid" value="${resume.uuid}" disabled></td>
                </tr>
            </table>
        </fieldset>
        <fieldset class="contacts">
            <legend>CONTACTS</legend>
            <table>
                <c:forEach items="${ContactType.values()}" var="contact">
                    <tr>
                        <td class="title">${contact.title}</td>
                        <td><input name="${contact}" value="${resume.contacts.get(contact)}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </fieldset>
        <fieldset>
            <legend>PERSONAL INFO</legend>
            <c:forEach items="${SectionType.values()}" var="section">
                <c:choose>
                    <c:when test="${section == SectionType.PERSONAL or section == SectionType.OBJECTIVE}">
                        <p class="info">${section.title}</p>
                        <textarea class="info" name="${section}">${resume.personInfo.get(section)}</textarea>
                    </c:when>
                    <c:when test="${section == SectionType.QUALIFICATION or section == SectionType.ACHIEVEMENTS}">
                        <p class="info">${section.title}</p>
                        <div id="${section}" class="buttons">
                        <c:forEach items="${resume.personInfo.get(section).content}" var="element">
                            <textarea class="info" name="${section}">${element}</textarea>
                        </c:forEach>
                        <textarea class="info" name="${section}"></textarea>
                        <c:choose>
                            <c:when test="${section == SectionType.ACHIEVEMENTS}">
                                <input id="add_achievements"   class="info_button" type="button" value="Add field"    onclick="addAchievements()">
                                <input id="reset_achievements" class="info_button" type="button" value="Reset fields" onClick="resetAchievements()">
                            </c:when>
                            <c:otherwise>
                                <input id="add_qualifications"   class="info_button" type="button" value="Add field"    onclick="addQualifications()">
                                <input id="reset_qualifications" class="info_button" type="button" value="Reset fields" onClick="resetQualifications()">
                            </c:otherwise>
                        </c:choose>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>EXPECTED BLOCK</p>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </fieldset>
        <p class="end">
            <input type="submit" value="SAVE RESUME"/>
            <input type="reset" value="CLEAR FIELDS"/>
        </p>
    </form>
</section>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
