<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="tab.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="././css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="././css/font-awesome.css" rel="stylesheet" media="screen">
<link href="././css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="computers"> <fmt:message key="title" /> </a>
            <a href="?id=${ computerDto.id }&lang=en"><fmt:message key="lang.eng" /></a>
	        <a href="?id=${ computerDto.id }&lang=fr"><fmt:message key="lang.fr" /></a>
        </div>
    </header>
    
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        <c:out value="${ computerDto.id }"/>
                    </div>
                    <h1><fmt:message key="title.editComputer" /></h1>
                    
                    <c:if test="${ not empty errors }">
                   		<div class="has-error">
	                    	<c:forEach var="error" items="${ errors }">
	                    		<span><c:out value="${ error.value }"></c:out></span><br/>
	                    	</c:forEach>
                   		</div>
                    </c:if>

                    <form:form action="editComputer" method="POST" modelAttribute="computerDto">
                        <form:input type="hidden" value="${ computerToEdit.id }" path="id" id="id" name="id"/> <!-- TODO: Change this value with the computer id -->
                        <fieldset>
                            <div class="form-group">
                                <form:label path="name" for="computerName"><fmt:message key="column.name" /></form:label>
                                <form:input type="text" class="form-control" path="name" id="computerName" name="computerName" value="${ computerDto.name }" placeholder="Computer Name"/>
                            </div>
                            <div class="form-group">
                                <form:label path="introduced" for="introduced"><fmt:message key="column.introduced" /></form:label>
                                <form:input type="date" class="form-control" path="introduced" id="introduced" name="introduced" value='${ computerDto.introduced }'  placeholder="<fmt:message key='placeholder.introduced' />"/>
                            </div>
                            <div class="form-group">
                                <form:label path="discontinued" for="discontinued"><fmt:message key="column.discontinued" /></form:label>
                                <form:input type="date" class="form-control" path="discontinued" id="discontinued" name="discontinued" value='${ computerDto.discontinued }' placeholder="<fmt:message key='placeholder.discontinued' />"/>
                            </div>
                            <div class="form-group">
                                <form:label path="companyId" for="companyId"><fmt:message key="column.company" /></form:label>
                                <form:select class="form-control" path="companyId" id="companyId" name="companyId">
                                    <option value="0">--</option>
                                    
                                    <c:forEach var="company" items="${ requestScope.companies }">
                                    	<c:choose>
                                    	<c:when test="${ computerDto.companyId == company.id }">
	                                    	<option value="${ company.id }" selected="true"><c:out value="${ company.name }"></c:out></option>
                                    	</c:when>
                                    	<c:otherwise>
                                    		<option value="${ company.id }"><c:out value="${ company.name }"></c:out></option>
                                    	</c:otherwise>
                                    	</c:choose>
                                    </c:forEach>
                                </form:select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<fmt:message key='button.edit' />" class="btn btn-primary">
                            or
                            <a href="computers" class="btn btn-default"><fmt:message key="button.cancel" /></a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>