<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${language}"/>
<fmt:message key="error.page" var="title"/>

<html>
<head>
<%@ include file="/WEB-INF/components/head.jspf" %>
</head>
    <body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="error-template">
                <h1>
                    404 <fmt:message key="error.message"/></h1>
                <h2>
                    404 <fmt:message key="error.message"/></h2>
                <div class="error-details">
                   404 <fmt:message key="error.message"/>
                </div>
                <div class="error-actions">
                    <a href="/app/library/" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-home"></span><fmt:message key="home"/></a>
                </div>
            </div>
        </div>
    </div>
</div>
    </body>
</html>