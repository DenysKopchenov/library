<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${language}"/>
<fmt:message key="navbar.catalog" var="title"/>

<!DOCTYPE html>
<html lang=${language}>
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/components/nav_bar_guest.jspf" %>
<c:if test="${catalog != null}">
<div class="row" >
<div class="col-4 mx-auto">
    <table class="table table-bordered mx-auto">
    <thead>
<div class="btn-group">
  <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
   <fmt:message key="catalog.sort.title" var="sortButton"/>
    ${sortBy == null ? sortButton : sortBy}
    </button>
  <div class="dropdown-menu">
    <a class="dropdown-item" href="?sort=title&page=${i}&perPage=${perPage}"><fmt:message key="catalog.title"/></a>
    <a class="dropdown-item" href="?sort=author&page=${i}&perPage=${perPage}"><fmt:message key="catalog.author"/></a>
    <a class="dropdown-item" href="?sort=publisher&page=${i}&perPage=${perPage}"><fmt:message key="catalog.publisher"/></a>
    <a class="dropdown-item" href="?sort=publishing_date&page=${i}&perPage=${perPage}"><fmt:message key="catalog.publishing.date"/></a>
  </div>
</div>
<div class="btn-group" style="float:right">
  <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
    ${perPage == null ? "5" : perPage}
    </button>
  <div class="dropdown-menu">
    <a class="dropdown-item" href="?sort=${sort}&page=${i}&perPage=5">5</a>
    <a class="dropdown-item" href="?sort=${sort}&page=${i}&perPage=10">10</a>
    <a class="dropdown-item" href="?sort=${sort}&page=${i}&perPage=20">20</a>
  </div>
  </div>
                        <tr>
                            <th><fmt:message key="catalog.title"/></th>
                            <th><fmt:message key="catalog.author"/></th>
                            <th><fmt:message key="catalog.publisher"/></th>
                            <th><fmt:message key="catalog.publishing.date"/></th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="book" items="${catalog}">
                        <tr>
                            <td>${book.getTitle()}</td>
                            <td>${book.getAuthor()}</td>
                            <td>${book.getPublisher()}</td>
                            <td>${book.getPublishingDate()}</td>
                        </tr>
    </c:forEach>
                        </tbody>
                        </table>
                        <nav aria-label="Page navigation example">
                            <ul class="pagination">
                                <c:if test="${currentPage != 1}">
                                    <li class="page-item"><a class="page-link" href="?sort=${sort}&page=${currentPage - 1}&perPage=${perPage}"><fmt:message key="pagination.previous"/></a></li>
                                </c:if>
                                <c:forEach begin="1" end="${numberOfPages}" var="i">
                                    <li class="active"><a class="page-link" href="?sort=${sort}&page=${i}&perPage=${perPage}">${i}</a></li>
                                </c:forEach>
                                <c:if test="${currentPage < numberOfPages}">
                                    <li class="page-item"><a class="page-link" href="?sort=${sort}&page=${currentPage + 1}&perPage=${perPage}"><fmt:message key="pagination.next"/></a></li>
                                </c:if>
                            </ul>
                        </nav>
</div>
</div>

</c:if>
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