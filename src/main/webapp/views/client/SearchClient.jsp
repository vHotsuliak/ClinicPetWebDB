<%--
  Created by IntelliJ IDEA.
  User: Vasyl Gotsuliak
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<%--@elvariable id="clients" type="java.util.List"--%>
<%--suppress HtmlFormInputWithoutLabel --%>
<html>
<head>
    <title>Search client</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Style.css">
    <%-- If don't use jstl then script not working--%>
    <script src="<c:url value="/js/client/ClientSearch.js"/>"></script>
</head>
<body>
    <div>
        <a href="${pageContext.servletContext.contextPath}/client/view">Main page</a><br/><br/>
    </div>

    <div>
        <form action="${pageContext.servletContext.contextPath}/client/search" method="get" onsubmit="submitCheck()">
            Client name:<br>
            <input type="text" name="clientName" id="clientName"><br>
            Pet name:<br>
            <input type="text" name="petName" id="petName"><br><br>
            Kind of pet:<br>
            <input type="text" name="kindOfPet" id="kindOfPet"><br><br>
            <input type="submit" value="Search">
        </form>
    </div>

    <c:choose>
        <c:when test="${clients != null && clients.size() > 0}">
            <table border="2px" id="table_1">
                <tr>
                    <th>Client name</th>
                    <th>Pet name</th>
                    <th>Kind of pet</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${clients}" var="client" varStatus="status">
                    <tr valign="top">
                        <td>${client.clientName}</td>
                        <td>${client.petName}</td>
                        <td>${client.kindOfPet}</td>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/client/edit?id=${client.id}">Edit</a>
                            <a href="${pageContext.servletContext.contextPath}/client/delete?id=${client.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <h3 id="forResult" style="background-color: yellowgreen">
                <c:if test="${clients.size() == 0}">Not found</c:if>
            </h3>
        </c:otherwise>
    </c:choose>
</body>
</html>
