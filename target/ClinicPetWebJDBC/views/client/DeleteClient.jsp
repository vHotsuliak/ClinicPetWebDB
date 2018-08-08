<%--
  Created by IntelliJ IDEA.
  User: Vasyl Gotsuliak
  Date: 26.02.2018
  Time: 21:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<html>
<head>
    <title>Delete client</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
<a href="${pageContext.servletContext.contextPath}/client/view">Main page</a><br/><br/>
<table border="2px">
    <tr>
        <th>Client name</th>
        <th>Pet name</th>
        <th>Kind of pet</th>
    </tr>

    <tr valign="top">
        <td>${client.clientName}</td>
        <td>${client.petName}</td>
        <td>${client.kindOfPet}</td>
    </tr>
</table>
<font color="red">
    Are you sure you want to delete this customer?
    <form action="${pageContext.servletContext.contextPath}/client/delete" method="post">
        <input type="submit" name="confirmation" value="YES">
        <input type="submit" name="confirmation" value="NO">
    </form>
</font>
</body>
</html>
