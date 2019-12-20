<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 13.12.2019
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Form</title>
</head>
<body>
<form action="login" method="POST">
    Login:    <input name="username" />
    <br><br>
    Password: <input type="password" name="password" />
    <br><br>
    <input type="submit" value="Login" align="center" />
    <h4 style="color: firebrick">User not found</h4>
    <a href="register.jsp"><h4 style="color: #7bb29b">Registration</h4></a>
</form>
</body>
</html>
