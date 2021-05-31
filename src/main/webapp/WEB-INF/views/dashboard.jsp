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
            <a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                ${ sessionScope.WebPageController.getCount() } Computers found
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
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

        <form id="deleteForm" action="#" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

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
                            Computer name
                        </th>
                        <th>
                            Introduced date
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            Discontinued date
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            Company
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                
                	<c:forEach var="computer" items="${ requestScope.computerList }">
	                	<tr>
	                        <td class="editMode">
	                            <input type="checkbox" name="cb" class="cb" value="0">
	                        </td>
	                        <td>
	                            <a href="editComputer.html" onclick=""><c:out value="${computer.getName() }"/></a>
	                        </td>
	                        <td><c:out value="${computer.getIntroduced() }"/></td>
	                        <td><c:out value="${computer.getDiscontinued() }"/></td>
	                        <td><c:out value="${computer.getCompany().getName() }"/></td>
	                    </tr>
	                </c:forEach>
	                
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <ul class="pagination">
            	
            	<c:if test="${ sessionScope.webPageController.getCurrentPageIndex() > 0 }">
	              	<li>
	                	<a href="?page=${ sessionScope.webPageController.getCurrentPageIndex() }" aria-label="Previous">
	                    	<span aria-hidden="true">&laquo;</span>
	                	</a>
	              	</li>
            	</c:if>
            	
              	<c:choose>
              	<c:when test="${ sessionScope.webPageController.getNumberOfPages() >= 5 }">
              	
              		<c:choose>
              			<c:when test="${ sessionScope.webPageController.getCurrentPageIndex() < 2 }">
              				<c:forEach var="p" begin="1" end="5">
		              			<li><a href="?page=${ p }"><c:out value="${ p }"></c:out></a></li>
		              		</c:forEach>
              			</c:when>
              			
              			<c:when test="${ sessionScope.webPageController.getCurrentPageIndex() > (sessionScope.webPageController.getNumberOfPages() - 3) }">
              				<c:forEach var="p" begin="${ sessionScope.webPageController.getNumberOfPages() - 6 }" end="${ sessionScope.webPageController.getNumberOfPages() - 1 }">
		              			<li><a href="?page=${ p + 1 }"><c:out value="${ p + 1 }"></c:out></a></li>
		              		</c:forEach>
              			</c:when>
              			
              			<c:otherwise>
              				<c:forEach var="p" begin="${ sessionScope.webPageController.getCurrentPageIndex() - 2 }" end="${ sessionScope.webPageController.getCurrentPageIndex() + 2 }">
		              			<li><a href="?page=${ p + 1 }"><c:out value="${ p + 1 }"></c:out></a></li>
		              		</c:forEach>
              			</c:otherwise>
              		</c:choose>
              	
              	</c:when>
              	
              	<c:otherwise>
              		<c:forEach var="p" begin="1" end="${ sessionScope.webPageController.getNumberOfPages() }">
              			<li><a href="?page=${ p }"><c:out value="${ p }"></c:out></a></li>
              		</c:forEach>
              	</c:otherwise>
              	</c:choose>
              	
              	<c:if test="${ sessionScope.webPageController.getCurrentPageIndex() < (sessionScope.webPageController.getNumberOfPages() - 1) }">
	              	<li>
		                <a href="?page=${ sessionScope.webPageController.getCurrentPageIndex() + 2 }" aria-label="Next">
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

    </footer>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/dashboard.js"></script>

</body>
</html>