<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tagLibs/linkLib.tld" prefix="linkLib"%>
<%@ taglib uri="/WEB-INF/tagLibs/pageLib.tld" prefix="pagination"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<!DOCTYPE html>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link
	href="${pageContext.request.contextPath}/resources/css/font-awesome.css"
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/resources/css/main.css"
	rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand"
				href="${pageContext.request.contextPath}/computers"> Application
				- <spring:message code="dashboard.cdb" />
			</a>
			<c:url value="/logout" var="logoutUrl" />
			<form id="logout" action="${logoutUrl}" method="post" >
			  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
			<c:if test="${pageContext.request.userPrincipal.name != null}">
				<a class="navbar-brand" href="javascript:document.getElementById('logout').submit()">Logout</a>
			</c:if>
			<div style="float:right">
				<a class="navbar-brand"
					href="${pageContext.request.contextPath}/computers?lang=en">English
				</a> | <a class="navbar-brand"
					href="${pageContext.request.contextPath}/computers?lang=fr">Fran�ais
				</a>
			</div>
		</div>
	</header>
	<c:url var="uri" value="/computers" />

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">

				<c:out value="${numberOfComputers}" />
				<spring:message code="dashboard.computers_found" />
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control"
							placeholder="<spring:message code="dashboard.search"/>"
							value="<c:out value="${search}" />" /> <input type="submit"
							id="searchsubmit"
							value="<spring:message code="dashboard.filter"/>"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer"
						href="${pageContext.request.contextPath}/computers/add"><spring:message
							code="dashboard.add_computer" /></a> <a class="btn btn-default"
						id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message
							code="dashboard.edit" /></a>
				</div>
			</div>
		</div>

		<form id="deleteForm"
			action="${pageContext.request.contextPath}/computers/delete"
			method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a id="deleteSelected"
								onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th><a
							href=<linkLib:display uri="${uri}" 
							currentPage="${currentPage}" 
							numberOfItemPerPage="${numberOfItemPerPage}" 
							search="${search}" 
							orderBy="computerName" 
							ascOrDesc="${orderBy == 'computerName' ? ascOrDesc == 'asc' ? 'desc' : 'asc' : 'asc'}"/>>
								<spring:message code="dashboard.computer_name" /> <i
								class="fa fa-fw ${orderBy == 'computerName' ? ascOrDesc == 'asc' ? 'fa-sort-asc' : 'fa-sort-desc' : 'fa-sort'}"></i>
						</a></th>
						<!-- Table header for Introduced Date -->
						<th><a
							href=<linkLib:display uri="${uri}" 
							currentPage="${currentPage}" 
							numberOfItemPerPage="${numberOfItemPerPage}" 
							search="${search}" 
							orderBy="introduced" 
							ascOrDesc="${orderBy == 'introduced' ? ascOrDesc == 'asc' ? 'desc' : 'asc' : 'asc'}"/>><spring:message
									code="dashboard.introduced" /> <i
								class="fa fa-fw ${orderBy == 'introduced' ? ascOrDesc == 'asc' ? 'fa-sort-asc' : 'fa-sort-desc' : 'fa-sort'}"></i>
						</a></th>
						<!-- Table header for Discontinued Date -->
						<th><a
							href=<linkLib:display uri="${uri}" 
							currentPage="${currentPage}" 
							numberOfItemPerPage="${numberOfItemPerPage}" 
							search="${search}" 
							orderBy="discontinued" 
							ascOrDesc="${orderBy == 'discontinued' ? ascOrDesc == 'asc' ? 'desc' : 'asc' : 'asc'}"/>><spring:message
									code="dashboard.discontinued" /> <i
								class="fa fa-fw ${orderBy == 'discontinued' ? ascOrDesc == 'asc' ? 'fa-sort-asc' : 'fa-sort-desc' : 'fa-sort'}"></i>
						</a></th>
						<!-- Table header for Company -->
						<th><a
							href=<linkLib:display uri="${uri}" 
							currentPage="${currentPage}" 
							numberOfItemPerPage="${numberOfItemPerPage}" 
							search="${search}" 
							orderBy="companyName" 
							ascOrDesc="${orderBy == 'companyName' ? ascOrDesc == 'asc' ? 'desc' : 'asc' : 'asc'}"/>><spring:message
									code="dashboard.company" /> <i
								class="fa fa-fw ${orderBy == 'companyName' ? ascOrDesc == 'asc' ? 'fa-sort-asc' : 'fa-sort-desc' : 'fa-sort'}"></i>
						</a></th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach items="${computers}" var="computer">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${computer.id}"></td>
							<td><a
								href="${pageContext.request.contextPath}/computers/${computer.id}/update"
								onclick=""><c:out value="${computer.name}" /></a></td>
							<td><c:out value="${computer.introduced}" /></td>
							<td><c:out value="${computer.discontinued}" /></td>
							<td><c:out value="${computer.companyName}" /></td>

						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<spring:message code="dashboard.next" var="next" />
			<spring:message code="dashboard.prev" var="prev" />
			<pagination:display maxLinks="5" currentPage="${currentPage}"
				numberOfPages="${numberOfPages}" uri="${uri}"
				numberOfItemPerPage="${numberOfItemPerPage}" search="${search}"
				orderBy="${orderBy}" ascOrDesc="${ascOrDesc}" next="${next}"
				prev="${prev}" />
			<div class="btn-group btn-group-sm pull-right" role="group">
				<a type="button" class="btn btn-default"
					href=<linkLib:display uri="${uri}" currentPage="${currentPage}" numberOfItemPerPage="10" search="${search}" orderBy="${orderBy}" ascOrDesc="${ascOrDesc}"/>>10</a>
				<a type="button" class="btn btn-default"
					href=<linkLib:display uri="${uri}" currentPage="${currentPage}" numberOfItemPerPage="50" search="${search}" orderBy="${orderBy}" ascOrDesc="${ascOrDesc}"/>>50</a>
				<a type="button" class="btn btn-default"
					href=<linkLib:display uri="${uri}" currentPage="${currentPage}" numberOfItemPerPage="100" search="${search}" orderBy="${orderBy}" ascOrDesc="${ascOrDesc}"/>>100</a>
			</div>
		</div>
	</footer>
	<script
		src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/dashboard.js"></script>

</body>
</html>