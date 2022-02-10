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
<c:if test="${operation eq 'createBook'}">
    <div>
        <form method="post">
            <br><label for="title"><b>Title</b></label><br>
            <input type="text" name="title" placeholder="Title"/>
            <br>
            <c:if test="${validation.containsKey('title')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("title")}</font>
            </c:if>

            <br><label for="author"><b>Author</b></label><br>
            <input type="text" name="author" placeholder="Author"/>
            <br>
            <c:if test="${validation.containsKey('author')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("author")}</font>
            </c:if>

            <br><label for="publisher"><b>Publisher</b></label><br>
            <input type="text" name="publisher" placeholder="Publisher"/>
            <br>
            <c:if test="${validation.containsKey('publisher')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("publisher")}</font>
            </c:if>

            <br><label for="publishing_date"><b>Publishing date</b></label><br>
            <input type="date" name="publishing_date" placeholder="yyyy/MM/dd"/>
            <br>
            <c:if test="${validation.containsKey('publishingDate')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("publishingDate")}</font>
            </c:if>

            <br><label for="amount"><b>Amount</b></label><br>
            <input type="number" name="amount" placeholder="Amount"/>
            <br>
            <c:if test="${validation.containsKey('amount')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("amount")}</font>
            </c:if>

            <br><input class="btn btn-primary" type="submit" name="createNewBook" value="Create Book"/>
            <h3><font style="color:hsl(0,100%,50%);">${successCreate}</font></h3>
            <h3><font style="color:hsl(0,100%,50%);">${failedCreate}</font></h3>
        </form>
    </div>
</c:if>
<c:if test="${operation eq 'updateBook'}">
    <c:out value="You updating book ${updatingBook}"/>
    <div>
        <form method="post">
            <br><label for="title"><b>Title</b></label><br>
            <input type="text" name="title" value="${operation eq 'updateBook' ? updatingBook.getTitle() : ''}" placeholder="Title"/>
            <br>
            <c:if test="${validation.containsKey('title')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("title")}</font>
            </c:if>

            <br><label for="author"><b>Author</b></label><br>
            <input type="text" name="author" placeholder="${updatingBook.getAuthor()}"/>
            <br>
            <c:if test="${validation.containsKey('author')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("author")}</font>
            </c:if>

            <br><label for="publisher"><b>Publisher</b></label><br>
            <input type="text" name="publisher" placeholder="${updatingBook.getPublisher()}"/>
            <br>
            <c:if test="${validation.containsKey('publisher')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("publisher")}</font>
            </c:if>

            <br><label for="publishing_date"><b>Publishing date</b></label><br>
            <input type="date" name="publishing_date" placeholder="${updatingBook.getPublishingDate()}"/>
            <br>
            <c:if test="${validation.containsKey('publishingDate')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("publishingDate")}</font>
            </c:if>

            <br><label for="amount"><b>Amount</b></label><br>
            <input type="number" name="amount" placeholder="${updatingBook.getAmount()}"/>
            <br>
            <c:if test="${validation.containsKey('amount')}">
                <font style="color:hsl(0,100%,50%);">${validation.get("amount")}</font>
            </c:if>

            <br><input class="btn btn-primary" type="submit" name="updateCurrentBook" value="Update Book"/>
            <h3><font style="color:hsl(0,100%,50%);">${successUpdate}</font></h3>
            <h3><font style="color:hsl(0,100%,50%);">${failedUpdate}</font></h3>
        </form>
    </div>
</c:if>
<div>
    <c:out value="${userInfo}"/>
    <h3><font style="color:hsl(0,100%,50%);">${successDelete}</font></h3>
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
    <c:forEach var="user" items="${allUsers}">
        <li>
            ${user}
            <a href="?operations=blockUser&userId=${user.getId()}"> block </a>
            <a href="?operations=unblockUser&userId=${user.getId()}"> unblock </a>
            depends on status show 1 of operation
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
                ${book}<a href="?operations=deleteBook&bookId=${book.getId()}"> delete</a>
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