<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
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
            <h1 id="homeTitle">
                ${ sessionScope.page.getCount() } Computers found
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" value="<c:out value="${ sessionScope.searchTerm }"/>" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="computers" method="POST">
            <input type="hidden" name="selection" value="">

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                            <a href="?orderBy=computerName">Computer name</a>
                        </th>
                        <th>
                            <a href="?orderBy=introduced">Introduced date</a>
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                        	<a href="?orderBy=discontinued">Discontinued date</a>
                        </th>
                        <!-- Table header for Company -->
                        <th>
                        	<a href="?orderBy=companyName">Company</a>
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                
                	<c:forEach var="computer" items="${ requestScope.computerList }">
	                	<tr>
	                        <td class="editMode">
	                            <input type="checkbox" name="cb" class="cb" value="${ computer.id }">
	                        </td>
	                        <td>
	                            <a href="editComputer?id=${ computer.id }" onclick=""><c:out value="${ computer.name }"/></a>
	                        </td>
	                        <td><c:out value="${ computer.introduced }"/></td>
	                        <td><c:out value="${ computer.discontinued }"/></td>
	                        <td><c:out value="${ computer.companyName }"/></td>
	                    </tr>
	                </c:forEach>
	                
                </tbody>
            </table>
        </div>
        </form>
        
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <ul class="pagination">
            	
            	<c:if test="${ sessionScope.page.currentPageIndex > 0 }">
	              	<li>
	                	<a href="?page=${ sessionScope.page.currentPageIndex }" aria-label="Previous">
	                    	<span aria-hidden="true">&laquo;</span>
	                	</a>
	              	</li>
            	</c:if>
            	
              	<c:choose>
              	<c:when test="${ sessionScope.page.numberOfPages > 5 }">
              	
              		<c:choose>
              			<c:when test="${ sessionScope.page.currentPageIndex < 2 }">
              				<c:forEach var="p" begin="1" end="5">
		              			<li><a href="?page=${ p }"><c:out value="${ p }"></c:out></a></li>
		              		</c:forEach>
              			</c:when>
              			
              			<c:when test="${ sessionScope.page.currentPageIndex > (sessionScope.page.numberOfPages - 3) }">
              				<c:forEach var="p" begin="${ sessionScope.page.numberOfPages - 5 }" end="${ sessionScope.page.numberOfPages }">
		              			<li><a href="?page=${ p }"><c:out value="${ p }"></c:out></a></li>
		              		</c:forEach>
              			</c:when>
              			
              			<c:otherwise>
              				<c:forEach var="p" begin="${ sessionScope.page.currentPageIndex - 2 }" end="${ sessionScope.page.currentPageIndex + 2 }">
		              			<li><a href="?page=${ p + 1 }"><c:out value="${ p + 1 }"></c:out></a></li>
		              		</c:forEach>
              			</c:otherwise>
              		</c:choose>
              	
              	</c:when>
              	
              	<c:otherwise>
              		<c:forEach var="p" begin="1" end="${ sessionScope.page.numberOfPages }">
              			<li><a href="?page=${ p }"><c:out value="${ p }"></c:out></a></li>
              		</c:forEach>
              	</c:otherwise>
              	</c:choose>
              	
              	<c:if test="${ sessionScope.page.currentPageIndex < (sessionScope.page.numberOfPages - 1) }">
	              	<li>
		                <a href="?page=${ sessionScope.page.currentPageIndex + 2 }" aria-label="Next">
		                    <span aria-hidden="true">&raquo;</span>
		                </a>
	            	</li>
              	</c:if>
        	</ul>

	        <div class="btn-group btn-group-sm pull-right" role="group" >
	            <a href="?itemsPerPage=10" type="button" class="btn btn-default">10</a>
	            <a href="?itemsPerPage=50" type="button" class="btn btn-default">50</a>
	            <a href="?itemsPerPage=100" type="button" class="btn btn-default">100</a>
	        </div>
		</div>
    </footer>
<script src="././js/jquery.min.js"></script>
<script src="././js/bootstrap.min.js"></script>
<script src="././js/dashboard.js"></script>

</body>
</html>