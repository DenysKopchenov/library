<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang=${currentLocale}>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <title>Title</title>
</head>
<body>
hello form READER page
<%@ include file="/WEB-INF/jspf/navigation_bar.jspf" %>
<div>
    <c:if test="${!booksByAuthor.isEmpty()}">
        <c:forEach var="books" items="${booksByAuthor}">
            <li>
                ${books}
                <br> <a href="?orderBook=reading-room"> Order to reading room</a>
                <br> <a href="?orderBook=home"> Order to home</a>
            </li>
        </c:forEach>
    </c:if>
</div>
<div>
    <c:if test="${!booksByTitle.isEmpty()}">
        <c:forEach var="books" items="${booksByTitle}">
            <li>
                ${books}
                <br> <a href="?orderBook=reading-room"> Order to reading room</a>
                <br> <a href="?orderBook=home"> Order to home</a>
            </li>
        </c:forEach>
    </c:if>
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