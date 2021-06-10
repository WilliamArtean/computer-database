<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="././css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="././css/font-awesome.css" rel="stylesheet" media="screen">
<link href="././css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="computers"> Application - Computer Database </a>
        </div>
    </header>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        <c:out value="${ computerDto.id }"/>
                    </div>
                    <h1>Edit Computer</h1>
                    
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
                                <form:label path="name" for="computerName">Computer name</form:label>
                                <form:input type="text" class="form-control" path="name" id="computerName" name="computerName" value="${ computerDto.name }" placeholder="Computer name"/>
                            </div>
                            <div class="form-group">
                                <form:label path="introduced" for="introduced">Introduced date</form:label>
                                <form:input type="date" class="form-control" path="introduced" id="introduced" name="introduced" value='${ computerDto.introduced }'  placeholder="Introduced date"/>
                            </div>
                            <div class="form-group">
                                <form:label path="discontinued" for="discontinued">Discontinued date</form:label>
                                <form:input type="date" class="form-control" path="discontinued" id="discontinued" name="discontinued" value='${ computerDto.discontinued }' placeholder="Discontinued date"/>
                            </div>
                            <div class="form-group">
                                <form:label path="companyId" for="companyId">Company</form:label>
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
                            <input type="submit" value="Edit" class="btn btn-primary">
                            or
                            <a href="computers" class="btn btn-default">Cancel</a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>