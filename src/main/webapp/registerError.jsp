<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 17.12.2019
  Time: 22:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<form action="registration" method="POST">
    <h2>Registration</h2>
    Enter new login:    <input name="username" />
    <br><br>
    Enter new password: <input type="password" name="password" />
    <br><br>
    <input type="submit" value="Confirm" />
    <h4 style="color: firebrick">All fields must be filled</h4>
</form>
</body>
</html>
