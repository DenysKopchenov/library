<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html lang=${language}>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title>Catalog</title>
</head>
<body>
                <form>
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
                </form>
                <h3><font style="color:hsl(0,100%,50%);">${errorMessage}</font></h3>
        <c:forEach var="book" items="${catalog}">
        <li>${book}</li>
        </c:forEach>
<a class="btn btn-primary" href="${pageContext.request.contextPath}/library/" role="button">Go back</a>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
</body>
</html>