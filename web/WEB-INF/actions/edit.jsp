<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<html>
<head>
    <title>Title</title>
    <link href="css/edit.css" rel="stylesheet"/>
    <script src="javascript/script.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<jsp:useBean id="resume" class="ru.javawebinar.basejava.model.Resume" scope="request"/>
<section>
    <form action="resume?uuid=${resume.uuid}&action=update" method="POST">
        <fieldset class="main">
            <legend>MAIN INFO</legend>
            <div>
                <p>Full name: </p>
                <input class="name" type="text" name="fullName" value="${resume.fullName}" required/>
            </div>
            <div>
                <p>UUID: </p>
                <input class="uuid" type="text" name="uuid" value="${resume.uuid}" disabled>
            </div>
        </fieldset>
        <fieldset class="contacts">
            <legend>CONTACTS</legend>
            <c:forEach items="${ContactType.values()}" var="contact">
                <div>
                    <p>${contact.title}</p>
                    <input name="${contact}" value="${resume.contacts.get(contact)}"/>
                </div>
            </c:forEach>
        </fieldset>
        <fieldset>
            <legend>PERSONAL INFO</legend>
            <c:forEach items="${SectionType.values()}" var="section">
                <c:choose>
                    <c:when test="${section == SectionType.PERSONAL or section == SectionType.OBJECTIVE}">
                        <div>
                            <p>${section.title}</p>
                            <textarea name="${section}">${resume.personInfo.get(section)}</textarea>
                        </div>
                    </c:when>
                    <c:when test="${section == SectionType.QUALIFICATION or section == SectionType.ACHIEVEMENTS}">
                        <p>${section.title}</p>
                        <div id="${section}" class="buttons">
                        <c:forEach items="${resume.personInfo.get(section).content}" var="element">
                            <textarea name="${section}">${element}</textarea>
                        </c:forEach>
                        <textarea name="${section}"></textarea>
                        <c:choose>
                            <c:when test="${section == SectionType.ACHIEVEMENTS}">
                                <button id="add_achievement" class="inner-button" onclick="addAchievement()">
                                    Add achievement
                                </button>
                                <button id="reset_achievements" class="inner-button" onClick="resetAchievements()">
                                    Reset achievements
                                </button>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <button id="add_qualification" class="inner-button" onclick="addQualification()">
                                    Add qualification
                                </button>
                                <button id="reset_qualifications" class="inner-button" onClick="resetQualifications()">
                                    Reset qualifications
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${section == SectionType.EDUCATION or section == SectionType.EXPERIENCE}">
                        <div id="${section}-block">
                            <p>${section.title}</p>
                            <c:forEach items="${resume.personInfo.get(section).content}" var="org">
                                <div id="org-block-${org.getHomepage().getName()}" class="org-block">
                                    <div class="org-name-url">
                                        <div class="org-row">
                                            <p>Company name</p>
                                            <input id="org-name-${org.getHomepage().getName()}"
                                                   class="${org.getHomepage().getName()}" type="text" name="${section}"
                                                   value="${org.getHomepage().getName()}"
                                                   oninput="this.setAttribute('value', value)"
                                                   onchange="replaceInnerNames(this.className, this.value)">
                                        </div>
                                        <div class="org-row">
                                            <p>Company URL (if exists)</p>
                                            <input class="${org.getHomepage().getName()}" type="text" name="${section}"
                                                   value="${org.getHomepage().getUrl()}">
                                        </div>
                                    </div>
                                    <div id="positions-${org.getHomepage().getName()}">
                                        <c:forEach items="${org.getPositions()}" var="position">
                                            <div class="position-title-date">
                                                <div class="position-row">
                                                    <p>Title</p>
                                                    <input class="org-${org.getHomepage().getName()}" type="text"
                                                           name="${org.getHomepage().getName()}"
                                                           value="${position.getTitle()}">
                                                </div>
                                                <div class="position-row">
                                                    <p>Start Date</p>
                                                    <input class="org-${org.getHomepage().getName()}" type="date"
                                                           name="${org.getHomepage().getName()}"
                                                           value="${position.getStartDate()}">
                                                </div>
                                                <div class="position-row">
                                                    <p>End Date</p>
                                                    <input class="org-${org.getHomepage().getName()}" type="date"
                                                           name="${org.getHomepage().getName()}"
                                                           value="${position.getEndDate()}">
                                                </div>
                                            </div>
                                            <div class="description">
                                                <p style="text-align: center">Description</p>
                                                <textarea class="org-${org.getHomepage().getName()}"
                                                          name="${org.getHomepage().getName()}">${position.getDescription()}</textarea>
                                            </div>

                                        </c:forEach>
                                    </div>
                                    <div id="end-${org.getHomepage().getName()}"></div>
                                    <div class="buttons">
                                        <input type="button" value="Add position"
                                               class="inner-button btn-${org.getHomepage().getName()}"
                                               name="${org.getHomepage().getName()}"
                                               onclick="addPosition(this.name)">
                                        <input type="button" value="Remove positions"
                                               class="inner-button btn-${org.getHomepage().getName()}"
                                               name="${org.getHomepage().getName()}"
                                               onclick="clearPositions(this.name)">
                                    </div>
                                </div>
                            </c:forEach>
                            <div id="new-org-block-${section}"></div>
                            <div class="buttons">
                                <input id="${section}" class="inner-button" type="button" value="Add organization" onclick="addOrganization(this.id)"/>
                                <input id="${section}" class="inner-button" type="button" value="Reset organizations>" onclick="resetOrganizations(this.id)"/>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>EXPECTED BLOCK</p>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </fieldset>
        <div class="end">
            <input type="submit" value="SAVE RESUME" onclick=""/>
            <input type="reset" value="CLEAR FIELDS"/>
        </div>
    </form>
</section>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
