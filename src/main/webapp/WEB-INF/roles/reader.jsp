<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="title" value="Reader"/>
<!DOCTYPE html>
<html lang=${language}>
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/components/nav_bar_reader.jspf" %>
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

<c:if test="${foundedBooks != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered">
    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Publisher</th>
                            <th>Publishing date</th>
                            <th colspan="2" style="text-align:center;">Operations</th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="book" items="${foundedBooks}">
            <c:if test="${book.getAmount() > 0}">
                        <tr>
                            <td>${book.getTitle()}</td>
                            <td>${book.getAuthor()}</td>
                            <td>${book.getPublisher()}</td>
                            <td>${book.getPublishingDate()}</td>
                            <td style="text-align:center;">
                            <a class="btn btn-info" href="?operations=orderBook&order=readingRoom&bookId=${book.getId()}"> Order to reading room</a>
                            <a class="btn btn-primary" href="?operations=orderBook&order=home&bookId=${book.getId()}"> Order to home</a>
                            </td>
                        </tr>
                        </c:if>
    </c:forEach>
                        </tbody>
                        </table>
</div>
</div>
</c:if>

<c:if test="${catalog != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered">
    <thead>
<div class="btn-group">
  <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    ${sortBy == null ? "By Title" : sortBy}
    </button>
  <div class="dropdown-menu">
    <a class="dropdown-item" href="?operations=catalog&sort=title&page=${i}&perPage=${perPage}">Title</a>
    <a class="dropdown-item" href="?operations=catalog&sort=author&page=${i}&perPage=${perPage}">Author</a>
    <a class="dropdown-item" href="?operations=catalog&sort=publisher&page=${i}&perPage=${perPage}">Publisher</a>
    <a class="dropdown-item" href="?operations=catalog&sort=publishing_date&page=${i}&perPage=${perPage}">Publishing date</a>
  </div>
</div>
<div class="btn-group" style="float:right">
  <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    ${perPage == null ? "5" : perPage}
    </button>
  <div class="dropdown-menu">
    <a class="dropdown-item" href="?operations=catalog&sort=${sort}&page=${i}&perPage=5">5</a>
    <a class="dropdown-item" href="?operations=catalog&sort=${sort}&page=${i}&perPage=10">10</a>
    <a class="dropdown-item" href="?operations=catalog&sort=${sort}&page=${i}&perPage=20">20</a>
  </div>
  </div>

                        <tr>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Publisher</th>
                            <th>Publishing date</th>
                            <th colspan="2" style="text-align:center;">Operations</th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="book" items="${catalog}">
            <c:if test="${book.getAmount() > 0}">
                        <tr>
                            <td>${book.getTitle()}</td>
                            <td>${book.getAuthor()}</td>
                            <td>${book.getPublisher()}</td>
                            <td>${book.getPublishingDate()}</td>
                            <td style="text-align:center;">
                            <a class="btn btn-info" href="?operations=orderBook&order=readingRoom&bookId=${book.getId()}"> Order to reading room</a>
                            <a class="btn btn-primary" href="?operations=orderBook&order=home&bookId=${book.getId()}"> Order to home</a>
                            </td>
                        </tr>
                        </c:if>
    </c:forEach>
                        </tbody>
                        </table>
                                                <nav aria-label="Page navigation example">
                                                    <ul class="pagination">
                                                        <c:if test="${currentPage > 1}">
                                                            <li class="page-item"><a class="page-link" href="?operations=catalog&sort=${sort}&page=${currentPage - 1}&perPage=${perPage}">Previous</a></li>
                                                        </c:if>
                                                        <c:forEach begin="1" end="${numberOfPages}" var="i">
                                                            <li class="active"><a class="page-link" href="?operations=catalog&sort=${sort}&page=${i}&perPage=${perPage}">${i}</a></li>
                                                        </c:forEach>
                                                        <c:if test="${currentPage < numberOfPages}">
                                                            <li class="page-item"><a class="page-link" href="?operations=catalog&sort=${sort}&page=${currentPage + 1}&perPage=${perPage}">Next</a></li>
                                                        </c:if>
                                                    </ul>
                                                </nav>
</div>
</div>
</c:if>

<c:if test="${userApprovedOrders != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered">
    <div class="btn-group" style="float:right">
      <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        ${perPage == null ? "5" : perPage}
        </button>
      <div class="dropdown-menu">
        <a class="dropdown-item" href="?operations=showApprovedOrders&page=${i}&perPage=5">5</a>
        <a class="dropdown-item" href="?operations=showApprovedOrders&page=${i}&perPage=10">10</a>
        <a class="dropdown-item" href="?operations=showApprovedOrders&page=${i}&perPage=20">20</a>
      </div>
      </div>
    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Publisher</th>
                            <th>Publishing date</th>
                            <th>Expected return date</th>
                            <th>Penalty</th>
                            <th>Operation</th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="approvedOrder" items="${userApprovedOrders}">
                        <tr>
                            <td>${approvedOrder.getBook().getTitle()}</td>
                            <td>${approvedOrder.getBook().getAuthor()}</td>
                            <td>${approvedOrder.getBook().getPublisher()}</td>
                            <td>${approvedOrder.getBook().getPublishingDate()}</td>
                            <td>${approvedOrder.getExpectedReturnDate()}</td>
                            <td>${approvedOrder.getPenalty()}</td>
                            <td style="text-align:center;">
                            <a class="btn btn-primary" href="?operations=returnBook&orderId=${approvedOrder.getOrderId()}">Return book</a></td>
                        </tr>
    </c:forEach>
                        </tbody>
                        </table>
                        <nav aria-label="Page navigation example">
                              <ul class="pagination">
                                          <c:if test="${currentPage > 1}">
                              <li class="page-item"><a class="page-link" href="?operations=showApprovedOrders&page=${currentPage - 1}&perPage=${perPage}">Previous</a></li>
                              </c:if>
                              <c:forEach begin="1" end="${numberOfPages}" var="i">
                                <li class="active"><a class="page-link" href="?operations=showApprovedOrders&page=${i}&perPage=${perPage}">${i}</a></li>
                             </c:forEach>
                            <c:if test="${currentPage < numberOfPages}">
                            <li class="page-item"><a class="page-link" href="?operations=showApprovedOrders&page=${currentPage + 1}&perPage=${perPage}">Next</a></li>
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