<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Computer List</title>
    </head>

    <body>
        <h3>List of computers generated through JSP:</h3>
        <ul>
	        <c:forEach var="computer" items="${ requestScope.computers }" >
	        	<li>
		        	Computer:
		        	<c:out value="${ computer.name }"></c:out>
		        	ID:
		        	<c:out value="${ computer.id }"></c:out>
		        </li>
	        </c:forEach>
		</ul>
        Computer created by user: <br/>
        Name: ${ userComputer.name } <br/>
        ID: ${ userComputer.id } <br/>
        Company ID: ${ userComputer.companyId } <br/>
        Voilààà
    </body>
</html>