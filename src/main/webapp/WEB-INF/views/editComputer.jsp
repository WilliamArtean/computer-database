<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
                        <c:out value="${ computerToEdit.id }"/>
                    </div>
                    <h1>Edit Computer</h1>
                    
                    <c:if test="${ not empty errors }">
                   		<div class="has-error">
	                    	<c:forEach var="error" items="${ errors }">
	                    		<span><c:out value="${ error.value }"></c:out></span><br/>
	                    	</c:forEach>
                   		</div>
                    </c:if>

                    <form action="editComputer" method="POST">
                        <input type="hidden" value="${ computerToEdit.id }" id="id"/> <!-- TODO: Change this value with the computer id -->
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" id="computerName" name="computerName" value="<c:out value="${ computerToEdit.name }"/>" placeholder="Computer name">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" id="introduced" name="introduced" value="${ computerToEdit.introduced }"  placeholder="Introduced date">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" value="${ computerToEdit.discontinued }" placeholder="Discontinued date">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId">
                                    <option value="0">--</option>
                                    
                                    <c:forEach var="company" items="${ requestScope.companies }">
                                    	<c:choose>
                                    	<c:when test="${ computerToEdit.company.id == company.id }">
	                                    	<option value="${ company.id }" selected="true"><c:out value="${ company.name }"></c:out></option>
                                    	</c:when>
                                    	<c:otherwise>
                                    		<option value="${ company.id }"><c:out value="${ company.name }"></c:out></option>
                                    	</c:otherwise>
                                    	</c:choose>
                                    </c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Edit" class="btn btn-primary">
                            or
                            <a href="computers" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>