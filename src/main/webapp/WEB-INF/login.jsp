<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html lang=${currentLocale}>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title>Title</title>
</head>
<body>
<div align="center">
<h1>Login</h1>
<h3> <font style="color:hsl(0,100%,50%);">${doesNotExist}</font></h3>
<h3> <font style="color:hsl(0,100%,50%);">${alreadyLogged}</font></h3>
<form method="post">
                <br><label for="email"><b>E-mail</b></label><br>
                <input type="text" name="email" placeholder="E-mail"/>
                <br><c:if test="${validation.containsKey('email')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("email")}</font>
                </c:if>

                <br><label for="password"><b>Password</b></label><br>
                <input type="password" name="password" placeholder="Password"/>
                <br><c:if test="${validation.containsKey('password')}">
                        <font style="color:hsl(0,100%,50%);">${validation.get("password")}</font>
                        </c:if>
                <font style="color:hsl(0,100%,50%);">${wrongPassword}</font>
                <br><input class="btn btn-primary" type="submit" value="Login"/>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/library/" role="button">Go back</a>
</form>
<h1><font style="color:hsl(0,100%,50%);">${wasBlocked}</font></h1>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
</body>
</html>