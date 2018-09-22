<%--
  Created by IntelliJ IDEA.
  User: Vasyl Gotsuliak
  Date: 10.02.2018
  Time: 9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<html>
<head>
    <title>Add client</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
    <div class="center-div">
        <a href="${pageContext.servletContext.contextPath}/client/view">Main page</a><br/><br/>
    </div>
    <div class="div-form-center">
        <form action="${pageContext.servletContext.contextPath}/client/add" method="post">
        Client name:<br>
        <input type="text" name="clientName" value="Client's name" required><br>
        Pet name:<br>
        <input type="text" name="petName" begraundV="Pet's name" required><br><br>
        <select size = "1" required size = "1" name = "kindOfPet">
            <option value = "Pet">Pet</option>
            <option value = "Cat">Cat</option>
            <option value = "Dog">Dog</option>
        </select><br/>
        <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
