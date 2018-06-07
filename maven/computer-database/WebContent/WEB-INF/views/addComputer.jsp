<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>Add Computer</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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

<body onload="checkFields()">
	<spring:url value="/computers" var="computersUrl" />
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="${computersUrl}"> Application -
				Computer Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Add Computer</h1>
					<br>
					<div class="form-error">
						<c:out value="${error}" />
					</div>
					<br>

					<spring:url value="/computers/create" var="computerActionUrl" />

					<form:form class="form-horizontal" method="post"
						modelAttribute="computerForm" action="${computerActionUrl}">

						<form:hidden path="id" />
						<fieldset>
							<div class="form-group">

								<spring:bind path="name">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<div class="col-sm-12">
											<label>Name</label>
											<form:input path="name" type="text" class="form-control"
												id="name" placeholder="Computer name" />
											<form:errors path="name" class="control-label" />
										</div>
									</div>
								</spring:bind>

							</div>

							<div class="form-group">

								<spring:bind path="introduced">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<div class="col-sm-12">
											<label>Introduced date (yyyy-MM-dd)</label>
											<form:input path="introduced" type="text"
												class="form-control" id="introduced"
												placeholder="Introduced date" />
											<form:errors path="introduced" class="control-label" />
										</div>
									</div>
								</spring:bind>

							</div>

							<div class="form-group">

								<spring:bind path="discontinued">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<div class="col-sm-12">
											<label>Discontinued date (yyyy-MM-dd)</label>
											<form:input path="discontinued" type="text"
												class="form-control" id="discontinued"
												placeholder="Discontinued date" />
											<form:errors path="discontinued" class="control-label" />
										</div>
									</div>
								</spring:bind>

							</div>

							<div class="form-group">

								<spring:bind path="companyId">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<div class="col-sm-12">
											<label>Company</label>

											<form:select path="companyId" class="form-control">
												<form:option value="-1" label="--- Select ---" />
												<form:options items="${companies}" itemValue="id"
													itemLabel="name" />
											</form:select>
											<form:errors path="companyId" class="control-label" />

										</div>
									</div>
								</spring:bind>

							</div>
							<div class="col-sm-12">
								<div class="actions pull-right">
									<input type="submit" value="Add" class="btn btn-primary"
										id="submitButton"> or <a href="${computersUrl}"
										class="btn btn-default">Cancel</a>
								</div>
							</div>
						</fieldset>
					</form:form>


					<%-- 					<form action="add-computer" method="POST"> --%>
					<!-- 						<fieldset> -->
					<!-- 							<div class="form-group has-error"> -->
					<!-- 								<label for="computerName">Computer name</label> <input -->
					<!-- 									type="text" class="form-control" id="computerName" -->
					<!-- 									name="computerName" placeholder="Computer name" -->
					<%-- 									value="<c:out value="${param.computerName}"/>"> --%>
					<!-- 								<div class="input-error" style="display: none">The name of -->
					<!-- 									the computer is mandatory and can't be empty.</div> -->
					<!-- 							</div> -->
					<!-- 							<div class="form-group has-success"> -->
					<!-- 								<label for="introduced">Introduced date (yyyy-mm-dd)</label> <input -->
					<!-- 									type="text" class="form-control" id="introduced" -->
					<!-- 									name="introduced" placeholder="Introduced date" -->
					<%-- 									value="<c:out value="${param.introduced}"/>"> --%>
					<!-- 								<div class="input-error" style="display: none">The date -->
					<!-- 									should be in the format yyyy-mm-dd (for example 2000-01-24).</div> -->
					<!-- 							</div> -->
					<!-- 							<div class="form-group has-success"> -->
					<!-- 								<label for="discontinued">Discontinued date (yyyy-mm-dd)</label> -->
					<!-- 								<input type="text" class="form-control" id="discontinued" -->
					<!-- 									name="discontinued" placeholder="Discontinued date" -->
					<%-- 									value="<c:out value="${param.discontinued}"/>"> --%>
					<!-- 								<div class="input-error" style="display: none">The date -->
					<!-- 									should be in the format yyyy-mm-dd (for example 2000-01-24).</div> -->
					<!-- 							</div> -->
					<!-- 							<div class="form-group"> -->
					<!-- 								<label for="companyId">Company</label> <select -->
					<!-- 									class="form-control" id="companyId" name="companyId"> -->
					<%-- 									<c:forEach items="${companies}" var="company"> --%>
					<%-- 										<c:choose> --%>
					<%-- 											<c:when test="${not empty param.companyId}"> --%>
					<%-- 												<c:choose> --%>
					<%-- 													<c:when test="${param.companyId ==company.id}"> --%>
					<!-- 														<tr> -->
					<%-- 															<option value="<c:out value="${company.id}" />" selected> --%>
					<%-- 																<c:out value="${company.name}" /></option> --%>
					<!-- 														</tr> -->
					<%-- 													</c:when> --%>
					<%-- 													<c:otherwise> --%>
					<%-- 														<option value="<c:out value="${company.id}"/>"> --%>
					<%-- 															<c:out value="${company.name}" /></option> --%>
					<%-- 													</c:otherwise> --%>
					<%-- 												</c:choose> --%>
					<%-- 											</c:when> --%>
					<%-- 											<c:otherwise> --%>
					<!-- 												<tr> -->
					<%-- 													<option value="<c:out value="${company.id}"/>"> --%>
					<%-- 														<c:out value="${company.name}" /></option> --%>
					<!-- 												</tr> -->
					<%-- 											</c:otherwise> --%>
					<%-- 										</c:choose> --%>
					<%-- 									</c:forEach> --%>
					<!-- 								</select> -->
					<!-- 							</div> -->
					<!-- 						</fieldset> -->
					<!-- 											<div class="actions pull-right"> -->
					<!-- 												<input type="submit" value="Add" class="btn btn-primary" -->
					<!-- 													id="submitButton"> or <a -->
					<!-- 													href=<c:url value="list-computer" /> class="btn btn-default">Cancel</a> -->
					<!-- 											</div> -->
					<%-- 					</form> --%>
				</div>
			</div>
		</div>
	</section>
	<script
		src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/computer-form.js"></script>
</body>
</html>