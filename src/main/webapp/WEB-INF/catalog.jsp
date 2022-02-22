<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="title" value="Catalog"/>


<!DOCTYPE html>
<html lang=${language}>
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/components/nav_bar_guest.jspf" %>
<form>
    <select class="form-select" style="width:auto" aria-label="Default select example"
            name="sort" onchange="submit()">
        <option value="title" ${sort==
        "title" ? 'selected' : ''}><fmt:message key="catalog.sort.title"/></option>
        <option value="author" ${sort==
        "author" ? 'selected' : ''}><fmt:message key="catalog.sort.author"/></option>
        <option value="publisher" ${sort==
        "publisher" ? 'selected' : ''}><fmt:message key="catalog.sort.publisher"/></option>
        <option value="publishing_date" ${sort==
        "publishing_date" ? 'selected' : ''}><fmt:message key="catalog.sort.publishing.date"/></option>
    </select>
</form>
<c:forEach var="book" items="${catalog}">
    <li>${book}</li>
</c:forEach>
<a class="btn btn-primary" href="${pageContext.request.contextPath}/library/" role="button"><fmt:message key="home"/></a>
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