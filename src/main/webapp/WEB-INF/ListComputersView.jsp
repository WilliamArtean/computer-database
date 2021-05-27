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
        <p>List of computers generated through JSP.</p>
        <c:out value="userComputer" /> <br/>
        Computer created by user: <br/>
        Name: ${ userComputer.name } <br/>
        ID: ${ userComputer.id } <br/>
        Company ID: ${ userComputer.companyId } <br/>
        Voilààà
    </body>
</html>