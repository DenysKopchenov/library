<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang=${language} xmlns:c="http://www.w3.org/1999/html">
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <title>Title</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/navigation_bar.jspf" %>
<h3><font style="color:hsl(0,100%,50%);">${errorMessage}</font></h3>
<h3><font style="color:hsl(100, 100%, 50%);">${successMessage}</font></h3>
<c:if test="${operation eq 'createLibrarian'}">
    <c:set var="button" value="Create librarian"/>
    <%@ include file="/WEB-INF/forms/reg_form.jspf" %>
</c:if>
<c:if test="${operation eq 'createBook'}">
    <%@ include file="/WEB-INF/forms/create_book_form.jspf" %>
</c:if>
<c:if test="${operation eq 'updateBook'}">
    <%@ include file="/WEB-INF/forms/update_book_form.jspf" %>
</c:if>
<div>
    <c:out value="${userInfo}"/>
</div>
<div>
    <c:forEach var="book" items="${catalog}">
        <li>
            ${book}
            <a href="?operations=deleteBook&bookId=${book.getId()}"> delete</a>
            <a href="?operations=updateBook&bookId=${book.getId()}"> update</a>
        </li>
    </c:forEach>
</div>
<div>
    <c:forEach var="user" items="${allLibrarians}">
        <li>
            ${user}
            <a href="?operations=deleteLibrarian&userId=${user.getId()}"> delete </a>
        </li>
    </c:forEach>
</div>
<div>
    <c:forEach var="reader" items="${allReaders}">
        <li>
            ${user}
            <c:if test="${reader.getStatus() eq 'blocked'}">
                <a href="?operations=unblockUser&userId=${user.getId()}"> unblock </a>
            </c:if>
            <c:if test="${reader.getStatus() eq 'active'}">
                <a href="?operations=blockUser&userId=${user.getId()}"> block </a>
            </c:if>
        </li>
    </c:forEach>
</div>
<div>
    <c:if test="${!booksByAuthor.isEmpty()}">
        <c:forEach var="book" items="${booksByAuthor}">
            <li>
                ${book}
                <a href="?operations=deleteBook&bookId=${book.getId()}"> delete</a>
                <a href="?operations=updateBook&bookId=${book.getId()}"> update</a>
            </li>
        </c:forEach>
    </c:if>
</div>
<div>
    <c:if test="${!booksByTitle.isEmpty()}">
        <c:forEach var="book" items="${booksByTitle}">
            <li>
                ${book}
                <a href="?operations=deleteBook&bookId=${book.getId()}"> delete</a>
                <a href="?operations=updateBook&bookId=${book.getId()}"> update</a>
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