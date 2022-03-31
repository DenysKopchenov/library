<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="title" value="Librarian"/>

<!DOCTYPE html>
<html lang=${language}>
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/components/nav_bar_librarian.jspf" %>
<h3><font style="color:hsl(0,100%,50%);">${errorMessage}</font></h3>
<h3><font style="color:hsl(100,100%,50%);">${successMessage}</font></h3>
<c:if test="${user != null}">
<div class="row">
    <div class="col-4 mx-auto">
    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>First name</th>
                            <th>Last name</th>
                            <th>Email</th>
                            <th>Role</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${user.getFirstName()}</td>
                            <td>${user.getLastName()}</td>
                            <td>${user.getEmail()}</td>
                            <td>${user.getRole()}</td>
                        </tr>
                        </tbody>
                    </table>
    </div>
</div>
</c:if>
<div>
    <c:forEach var="pendingOrder" items="${pendingOrders}">
        <li>
        ${pendingOrder}
        <a class="btn btn-primary" href="?operations=acceptOrder&orderId=${pendingOrder.getOrderId()}"> Accept </a>
        <a class="btn btn-primary" href="?operations=showReadersApprovedOrders&userId=${pendingOrder.getUserId()}"> Show user approved orders</a>
        <a class="btn btn-danger" href="?operations=rejectOrder&orderId=${pendingOrder.getOrderId()}"> Reject </a>
        </li>
    </c:forEach>
</div>

<c:if test="${allReaders != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered">
    <thead>
                        <tr>
                            <th>Id</th>
                            <th>First name</th>
                            <th>Last name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Operations</th>
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
                            <a class="btn btn-primary" href="?operations=showReadersApprovedOrders&userId=${reader.getId()}"> Show approved orders </a>
                            </td>
                        </tr>
    </c:forEach>
                        </tbody>
</div>
</div>
</c:if>

<div>
    <c:forEach var="approvedOrder" items="${userApprovedOrders}">
        <li>
        ${approvedOrder}
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