<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
            <a href="?lang=en"><fmt:message key="lang.eng" /></a>
	        <a href="?lang=fr"><fmt:message key="lang.fr" /></a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1><fmt:message key="title.addComputer" /></h1>
                    
                    <c:if test="${ not empty errors }">
                   		<div class="has-error">
	                    	<c:forEach var="error" items="${ errors }">
	                    		<span><c:out value="${ error.value }"></c:out></span><br/>
	                    	</c:forEach>
                   		</div>
                    </c:if>
                    
                    <form:form action="addComputer" method="POST" modelAttribute="computerDto">
                        <fieldset>
                            <div class="form-group">
                                <form:label path="name" for="computerName"><fmt:message key="column.name" /></form:label>
                                <form:input path="name" type="text" class="form-control" id="computerName" name="computerName" value="${ computerDto.name }" placeholder="Computer name"/>
                            </div>
                            <div class="form-group">
                                <form:label path="introduced" for="introduced"><fmt:message key="column.introduced" /></form:label>
                                <form:input path="introduced" type="date" class="form-control" id="introduced" name="introduced" value="${ computerDto.introduced }" />
                            </div>
                            <div class="form-group">
                                <form:label path="discontinued" for="discontinued"><fmt:message key="column.discontinued" /></form:label>
                                <form:input path="discontinued" type="date" class="form-control" id="discontinued" name="discontinued" value="${ computerDto.discontinued }"/>
                            </div>
                            <div class="form-group">
                                <form:label path="companyId" for="companyId"><fmt:message key="column.company" /></form:label>
                                <form:select path="companyId" class="form-control" id="companyId" name="companyId">
                                    <option value="0">--</option>
                                    
                                    <c:forEach var="company" items="${ companies }">
                                    	<c:choose>
                                    	<c:when test="${ computerDto.companyId == company.id }">
	                                    	<option value="${ company.id }" selected="true"><c:out value='${ company.name }'></c:out></option>
                                    	</c:when>
                                    	<c:otherwise>
                                    		<option value="${ company.id }"><c:out value='${ company.name }'></c:out></option>
                                    	</c:otherwise>
                                    	</c:choose>
                                    </c:forEach>
                                    
                                </form:select>
                            </div>
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<fmt:message key='button.add' />" class="btn btn-primary">
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