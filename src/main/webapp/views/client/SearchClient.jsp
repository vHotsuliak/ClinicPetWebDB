<%--
  Created by IntelliJ IDEA.
  User: Vasyl Gotsuliak
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<%--@elvariable id="clients" type="java.util.List"--%>
<html>
<head>
    <title>Search client</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Style.css">
    <script src="/js/client/ClientSearcch.js"></script>
</head>
<body>
    <div>
        <a href="${pageContext.servletContext.contextPath}/client/view">Main page</a><br/><br/>
    </div>

    <div>
        <form action="${pageContext.servletContext.contextPath}/client/search" method="get" onsubmit="submitCheck()">
            Client name:<br>
            <input type="text" name="clientName"><br>
            Pet name:<br>
            <input type="text" name="petName"><br><br>
            Kind of pet:<br>
            <input type="text" name="kindOfPet"><br><br>
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
            <h3 id="forResult">Not found</h3>
        </c:otherwise>
    </c:choose>
</body>
</html>
