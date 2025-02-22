<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/userInfo.tld" prefix="table" %>

<fmt:setLocale value="${language}"/>
<fmt:message key="title.librarian" var="title"/>

<!DOCTYPE html>
<html lang=${language}>
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/components/nav_bar_librarian.jspf" %>
<h3 style="text-align:center"><font style="color:hsl(0,100%,50%);">${errorMessage}</font></h3>
<h3 style="text-align:center"><font style="color:hsl(100, 100%, 50%);">${successMessage}</font></h3>
<c:if test="${user != null}">
<table:info/>
</c:if>
<div>
<c:if test="${pendingOrders != null}">
<div class="row" >
<div class="col-8 mx-auto">
<table class="table table-bordered">
    <div class="btn-group" style="float:right">
      <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        ${perPage == null ? "5" : perPage}
        </button>
      <div class="dropdown-menu">
        <a class="dropdown-item" href="?operations=showPendingOrders&page=${i}&perPage=5">5</a>
        <a class="dropdown-item" href="?operations=showPendingOrders&page=${i}&perPage=10">10</a>
        <a class="dropdown-item" href="?operations=showPendingOrders&page=${i}&perPage=20">20</a>
      </div>
      </div>
    <thead>
                        <tr>
                    <th><fmt:message key="order.create.date"/></th>
                    <th><fmt:message key="catalog.title"/></th>
                    <th><fmt:message key="catalog.author"/></th>
                    <th><fmt:message key="catalog.publisher"/></th>
                    <th><fmt:message key="catalog.publishing.date"/></th>
                    <th><fmt:message key="reader.full.name"/></th>
                    <th><fmt:message key="type"/></th>
                    <th><fmt:message key="status"/></th>
                    <th colspan="3" style="text-align:center;"><fmt:message key="operations"/></th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="pendingOrder" items="${pendingOrders}">
                        <tr>
                            <td>${pendingOrder.getCreateDate()}</td>
                            <td>${pendingOrder.getBook().getTitle()}</td>
                            <td>${pendingOrder.getBook().getAuthor()}</td>
                            <td>${pendingOrder.getBook().getPublisher()}</td>
                            <td>${pendingOrder.getBook().getPublishingDate()}</td>
                            <td>${pendingOrder.getUser().getFirstName()} ${pendingOrder.getUser().getLastName()}</td>
                            <td>${pendingOrder.getType()}</td>
                            <td>${pendingOrder.getStatus()}</td>
                            <td style="text-align:center;">
                            <a class="btn btn-primary" href="?operations=acceptOrder&orderId=${pendingOrder.getOrderId()}&page=${currentPage}&perPage=${perPage}"><fmt:message key="accept.button"/></a>
                            <a class="btn btn-danger" href="?operations=rejectOrder&orderId=${pendingOrder.getOrderId()}&page=${currentPage}&perPage=${perPage}"><fmt:message key="reject.button"/></a>
                            <a class="btn btn-primary" href="?operations=showReadersApprovedOrders&userId=${pendingOrder.getUser().getId()}"><fmt:message key="details.button"/></a>
                            </td>
                        </tr>
    </c:forEach>
                        </tbody>
                        </table>
                                                <nav aria-label="Page navigation example">
                                                <ul class="pagination">
                                                <c:if test="${currentPage > 1}">
                                                <li class="page-item"><a class="page-link" href="?operations=showPendingOrders&page=${currentPage - 1}&perPage=${perPage}"><fmt:message key="pagination.previous"/></a></li>
                                                </c:if>
                                                 <c:forEach begin="1" end="${numberOfPages}" var="i">
                                                   <li class="active"><a class="page-link" href="?operations=showPendingOrders&page=${i}&perPage=${perPage}">${i}</a></li>
                                                  </c:forEach>
                                                   <c:if test="${currentPage < numberOfPages}">
                                                     <li class="page-item"><a class="page-link" href="?operations=showPendingOrders&page=${currentPage + 1}&perPage=${perPage}"><fmt:message key="pagination.next"/></a></li>
                                                    </c:if>
                                                    </ul>
                                                 </nav>
</div>
</div>
</c:if>

<c:if test="${allReaders != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered">
    <div class="btn-group" style="float:right">
      <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        ${perPage == null ? "5" : perPage}
        </button>
      <div class="dropdown-menu">
        <a class="dropdown-item" href="?operations=showAllReaders&page=${i}&perPage=5">5</a>
        <a class="dropdown-item" href="?operations=showAllReaders&page=${i}&perPage=10">10</a>
        <a class="dropdown-item" href="?operations=showAllReaders&page=${i}&perPage=20">20</a>
      </div>
      </div>
    <thead>
                        <tr>
                            <th><fmt:message key="id"/></th>
                            <th><fmt:message key="first.name"/></th>
                            <th><fmt:message key="last.name"/></th>
                            <th><fmt:message key="email"/></th>
                            <th><fmt:message key="role"/></th>
                            <th><fmt:message key="status"/></th>
                            <th style="text-align:center;"><fmt:message key="operations"/></th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="reader" items="${allReaders}">
                        <tr>
                            <td>${reader.getId()}</td>
                            <td>${reader.getFirstName()}</td>
                            <td>${reader.getLastName()}</td>
                            <td>${reader.getEmail()}</td>
                            <td>${reader.getRole()}</td>
                            <td>${reader.getStatus()}</td>
                            <td style="text-align:center;">
                            <a class="btn btn-primary" href="?operations=showReadersApprovedOrders&userId=${reader.getId()}"><fmt:message key="show.approved.orders"/></a>
                            </td>
                        </tr>
    </c:forEach>
                        </tbody>
                        </table>
                                                <nav aria-label="Page navigation example">
                                                <ul class="pagination">
                                                <c:if test="${currentPage > 1}">
                                                <li class="page-item"><a class="page-link" href="?operations=showAllReaders&page=${currentPage - 1}&perPage=${perPage}"><fmt:message key="pagination.previous"/></a></li>
                                                </c:if>
                                                 <c:forEach begin="1" end="${numberOfPages}" var="i">
                                                   <li class="active"><a class="page-link" href="?operations=showAllReaders&page=${i}&perPage=${perPage}">${i}</a></li>
                                                  </c:forEach>
                                                   <c:if test="${currentPage < numberOfPages}">
                                                     <li class="page-item"><a class="page-link" href="?operations=showAllReaders&page=${currentPage + 1}&perPage=${perPage}"><fmt:message key="pagination.next"/></a></li>
                                                    </c:if>
                                                    </ul>
                                                 </nav>
</div>
</div>
</c:if>
<c:if test="${userApprovedOrders != null}">
<div class="row" >
<div class="col-8 mx-auto">
<table class="table table-bordered">
    <div class="btn-group" style="float:right">
      <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        ${perPage == null ? "5" : perPage}
        </button>
      <div class="dropdown-menu">
        <a class="dropdown-item" href="?operations=showReadersApprovedOrders&userId=${userId}&page=${i}&perPage=5">5</a>
        <a class="dropdown-item" href="?operations=showReadersApprovedOrders&userId=${userId}&page=${i}&perPage=10">10</a>
        <a class="dropdown-item" href="?operations=showReadersApprovedOrders&userId=${userId}&page=${i}&perPage=20">20</a>
      </div>
      </div>
    <thead>
                        <tr>
                        <th><fmt:message key="reader.full.name"/></th>
                        <th><fmt:message key="catalog.title"/></th>
                        <th><fmt:message key="catalog.author"/></th>
                        <th><fmt:message key="catalog.publisher"/></th>
                        <th><fmt:message key="catalog.publishing.date"/></th>
                        <th><fmt:message key="expected.return.date"/></th>
                        <th><fmt:message key="type"/></th>
                        <th><fmt:message key="status"/></th>
                        <th><fmt:message key="penalty"/></th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="approvedOrder" items="${userApprovedOrders}">
                        <tr>
                            <td>${approvedOrder.getUser().getFirstName()} ${approvedOrder.getUser().getLastName()}</td>
                            <td>${approvedOrder.getBook().getTitle()}</td>
                            <td>${approvedOrder.getBook().getAuthor()}</td>
                            <td>${approvedOrder.getBook().getPublisher()}</td>
                            <td>${approvedOrder.getBook().getPublishingDate()}</td>
                            <td>${approvedOrder.getExpectedReturnDate()}</td>
                            <td>${approvedOrder.getType()}</td>
                            <td>${approvedOrder.getStatus()}</td>
                            <td>${approvedOrder.getPenalty()}</td>
                        </tr>
    </c:forEach>
                        </tbody>
                        </table>
                              <nav aria-label="Page navigation example">
                                             <ul class="pagination">
                                        <c:if test="${currentPage > 1}">
                                 <li class="page-item"><a class="page-link" href="?operations=showReadersApprovedOrders&userId=${userId}&page=${currentPage - 1}&perPage=${perPage}"><fmt:message key="pagination.зкумшщгі"/></a></li>
                          </c:if>
                                   <c:forEach begin="1" end="${numberOfPages}" var="i">
                                 <li class="active"><a class="page-link" href="?operations=showReadersApprovedOrders&userId=${userId}&page=${i}&perPage=${perPage}">${i}</a></li>
                                                 </c:forEach>
                                          <c:if test="${currentPage < numberOfPages}">
                                           <li class="page-item"><a class="page-link" href="?operations=showReadersApprovedOrders&userId=${userId}&page=${currentPage + 1}&perPage=${perPage}"><fmt:message key="pagination.next"/></a></li>
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