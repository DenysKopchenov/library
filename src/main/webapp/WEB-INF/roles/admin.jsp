<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="title" value="Admin"/>

<!DOCTYPE html>
<html lang=${language} xmlns:c="http://www.w3.org/1999/html">
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/components/nav_bar_admin.jspf" %>
<h3 style="text-align:center"><font style="color:hsl(0,100%,50%);">${errorMessage}</font></h3>
<h3 style="text-align:center"><font style="color:hsl(100, 100%, 50%);">${successMessage}</font></h3>
<c:if test="${operation eq 'createLibrarian'}">
    <c:set var="button" value="Create librarian"/>
    <%@ include file="/WEB-INF/forms/create_librarian_form.jspf" %>
</c:if>
<c:if test="${operation eq 'createBook'}">
    <%@ include file="/WEB-INF/forms/create_book_form.jspf" %>
</c:if>
<c:if test="${operation eq 'updateBook'}">
    <%@ include file="/WEB-INF/forms/update_book_form.jspf" %>
</c:if>
<c:if test="${user != null}">
<div class="row">
    <div class="col-4 mx-auto">
    <table class="table table-bordered table-light">
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
<c:if test="${catalog != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered table-light">
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
                            <th>id</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Publisher</th>
                            <th>Publishing date</th>
                            <th>Amount</th>
                            <th>On order</th>
                            <th colspan="2" style="text-align:center;">Operations</th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="book" items="${catalog}">
                        <tr>
                            <td>${book.getId()}</td>
                            <td>${book.getTitle()}</td>
                            <td>${book.getAuthor()}</td>
                            <td>${book.getPublisher()}</td>
                            <td>${book.getPublishingDate()}</td>
                            <td>${book.getAmount()}</td>
                            <td>${book.getOnOrder()}</td>
                            <td style="text-align:center;"><a class="btn btn-danger" href="?operations=deleteBook&bookId=${book.getId()}&sort=${sort}&page=${currentPage}&perPage=${perPage}"> delete</a></td>
                            <td style="text-align:center;"><a class="btn btn-info" href="?operations=updateBook&bookId=${book.getId()}"> update</a></td>
                        </tr>
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
<c:if test="${allLibrarians != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered table-light">
    <thead>
        <div class="btn-group" style="float:right">
          <button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            ${perPage == null ? "5" : perPage}
            </button>
          <div class="dropdown-menu">
            <a class="dropdown-item" href="?operations=showAllLibrarians&page=${i}&perPage=5">5</a>
            <a class="dropdown-item" href="?operations=showAllLibrarians&page=${i}&perPage=10">10</a>
            <a class="dropdown-item" href="?operations=showAllLibrarians&page=${i}&perPage=20">20</a>
          </div>
          </div>
                        <tr>
                            <th>Id</th>
                            <th>First name</th>
                            <th>Last name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th colspan="2" style="text-align:center;">Operations</th>
                        </tr>
                        </thead>
                        <tbody>
    <c:forEach var="user" items="${allLibrarians}">
                        <tr>
                            <td>${user.getId()}</td>
                            <td>${user.getFirstName()}</td>
                            <td>${user.getLastName()}</td>
                            <td>${user.getEmail()}</td>
                            <td>${user.getRole()}</td>
                            <td style="text-align:center;"><a class="btn btn-danger" href="?operations=deleteLibrarian&userId=${user.getId()}&page=${currentPage}&perPage=${perPage}"> delete </a></td>
                        </tr>
    </c:forEach>
                        </tbody>
                        </table>
                        <nav aria-label="Page navigation example">
                            <ul class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item"><a class="page-link" href="?operations=showAllLibrarians&page=${currentPage - 1}&perPage=${perPage}">Previous</a></li>
                                </c:if>
                                <c:forEach begin="1" end="${numberOfPages}" var="i">
                                    <li class="active"><a class="page-link" href="?operations=showAllLibrarians&page=${i}&perPage=${perPage}">${i}</a></li>
                                </c:forEach>
                                <c:if test="${currentPage < numberOfPages}">
                                    <li class="page-item"><a class="page-link" href="?operations=showAllLibrarians&page=${currentPage + 1}&perPage=${perPage}">Next</a></li>
                                </c:if>
                            </ul>
                        </nav>
</div>
</div>
</c:if>
<c:if test="${allReaders != null}">
<div class="row" >
<div class="col-6 mx-auto">
<table class="table table-bordered table-light">
    <div class="dropdown" style="float:right">
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
                            <th>Id</th>
                            <th>First name</th>
                            <th>Last name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Operations</th>                                           
                </form>
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
                            <c:if test="${reader.getStatus() eq 'blocked'}">
                                <a class="btn btn-success" href="?operations=unblockUser&userId=${reader.getId()}&page=${currentPage}&perPage=${perPage}"> unblock </a>
                            </c:if>
                            <c:if test="${reader.getStatus() eq 'active'}">
                                <a class="btn btn-warning" href="?operations=blockUser&userId=${reader.getId()}&page=${currentPage}&perPage=${perPage}"> block </a>
                            </c:if>
                            </td>
                        </tr>
    </c:forEach>
                        </tbody>
</table>
<nav aria-label="Page navigation example">
    <ul class="pagination">
        <c:if test="${currentPage > 1}">
            <li class="page-item"><a class="page-link" href="?operations=showAllReaders&page=${currentPage - 1}&perPage=${perPage}">Previous</a></li>
        </c:if>
        <c:forEach begin="1" end="${numberOfPages}" var="i">
            <li class="active"><a class="page-link" href="?operations=showAllReaders&page=${i}&perPage=${perPage}">${i}</a></li>
        </c:forEach>
        <c:if test="${currentPage < numberOfPages}">
            <li class="page-item"><a class="page-link" href="?operations=showAllReaders&page=${currentPage + 1}&perPage=${perPage}">Next</a></li>
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