<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${language}"/>
<fmt:message key="login" var="title"/>

<!DOCTYPE html>
<html lang=${language}>
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/components/nav_bar_guest.jspf" %>
<div>
<div class="col-2 mx-auto">
    <h2>
        <fmt:message key="login"/>
    </h2>
<form method="post">
                <br><label for="email"><b><fmt:message key="email"/></b></label><br>
                <input type="text" name="email" placeholder="<fmt:message key="email"/>" value="${email}"/>
                <br><c:if test="${validation.containsKey('email')}">    
                <font style="color:hsl(0,100%,50%);">${validation.get("email")}</font>
                </c:if>
                <br><label for="password"><b><fmt:message key="password"/></b></label><br>
                <input type="password" name="password" placeholder="<fmt:message key="password"/>"/>
                <br><c:if test="${validation.containsKey('password')}">
                      <font style="color:hsl(0,100%,50%);">${validation.get("password")}</font>
                        </c:if>
                        <br><input class="btn btn-primary" type="submit" value="<fmt:message key="login"/>"/>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/library/" role="button"><fmt:message key="home"/></a>
</form>
<h3> <font style="color:hsl(0,100%,50%);">${errorMessage}</font></h3>
</div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>
</body>
</html>