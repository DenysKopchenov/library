<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang=${language}>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <title>Title</title>
</head>
<body>
hello form READER page
<%@ include file="/WEB-INF/jspf/nav_bar_reader.jspf" %>
<h3><font style="color:hsl(0,100%,50%);">${errorMessage}</font></h3>
<h3><font style="color:hsl(100,100%,50%);">${successMessage}</font></h3>
<c:forEach var="book" items="${foundedBooks}">
    <li>
        ${book}
        <br> <a href="?operations=orderBook&order=readingRoom&bookId=${book.getId()}"> Order to reading room</a>
        <br> <a href="?operations=orderBook&order=home&bookId=${book.getId()}"> Order to home</a>
    </li>
</c:forEach>
</div>
<div>
    <c:out value="${userInfo}"/>
    <h3><font style="color:hsl(0,100%,50%);">${successDelete}</font></h3>
</div>
</div>
<c:if test="${operation eq 'catalog'}">
    <!--    <form>
            <select class="form-select" aria-label="Default select example"
                    name="sort" onchange="submit()">
                <option value="title" ${sort==
                "title" ? 'selected' : ''}>By title</option>
                <option value="author" ${sort==
                "author" ? 'selected' : ''}>By author</option>
                <option value="publisher" ${sort==
                "publisher" ? 'selected' : ''}>By publisher</option>
                <option value="publishing_date" ${sort==
                "publishing_date" ? 'selected' : ''}>By publishing date</option>
            </select>
        </form>-->
    <a class="nav-link" href="?operations=catalog&sort=title"> by title </a>
    <a class="nav-link" href="?operations=catalog&sort=author"> by author </a>
    <a class="nav-link" href="?operations=catalog&sort=publisher"> by publisher </a>
    <a class="nav-link" href="?operations=catalog&sort=publishing_date"> by publishing date </a>
</c:if>
    <c:forEach var="book" items="${catalog}">
        <li>
            ${book}
        <br> <a href="?operations=orderBook&order=readingRoom&bookId=${book.getId()}"> Order to reading room</a>
        <br> <a href="?operations=orderBook&order=home&bookId=${book.getId()}"> Order to home</a>
        </li>
    </c:forEach>
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