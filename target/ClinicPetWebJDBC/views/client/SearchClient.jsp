<%--
  Created by IntelliJ IDEA.
  User: Vasyl Gotsuliak
  Date: 11.03.2018
  Time: 18:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<%--@elvariable id="clients" type="java.util.List"--%>
<html>
<head>
    <title>Search client</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
    <div>
        <a href="${pageContext.servletContext.contextPath}/client/view">Main page</a><br/><br/>
    </div>

    <div>
        <form action="${pageContext.servletContext.contextPath}/client/search" method="get">
            Client name:<br>
            <input type="text" name="clientName"><br>
            Pet name:<br>
            <input type="text" name="petName"><br><br>
            Kind of pet:<br>
            <input type="text" name="kindOfPet"><br><br>
            <input type="submit" value="Search">
        </form>
    </div>

    <%--
    <c:if test="${clients != null}">
        <c:if test="${clients.size() > 0}">
        <table border="2px">
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
        </c:if>
    </c:if>

    Аналагічний но зручніший і кращий варіант
    --%>





    <c:choose>
        <%--<c:when test="${clients == null}"></c:when>--%>
        <c:when test="${clients.size() > 0}">
            <table border="2px">
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
            <h3>Not found</h3>
        </c:otherwise>
    </c:choose>
</body>
</html>
