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
				<spring:message code="dashboard.cdb" /> </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1><spring:message code="add.title" /></h1>
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
											<spring:message code="add.computer_name" var="computer_name" />
											<label> <spring:message code="add.name" /> </label>
											<form:input path="name" type="text" class="form-control"
												id="name" placeholder="${computer_name}" />
											<form:errors path="name" class="control-label" />
										</div>
									</div>
								</spring:bind>

							</div>

							<div class="form-group">

								<spring:bind path="introduced">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<div class="col-sm-12">
											<spring:message code="add.introduced_date" var="introduced_date"/>
											<label><spring:message code="add.introduced" /></label>
											<form:input path="introduced" type="text"
												class="form-control" id="introduced"
												placeholder="${introduced_date}" />
											<form:errors path="introduced" class="control-label" />
										</div>
									</div>
								</spring:bind>

							</div>

							<div class="form-group">

								<spring:bind path="discontinued">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<div class="col-sm-12">
											<spring:message code="add.discontinued_date" var="discontinued_date"/>
											<label><spring:message code="add.discontinued" /></label>
											<form:input path="discontinued" type="text"
												class="form-control" id="discontinued"
												placeholder="${discontinued_date}" />
											<form:errors path="discontinued" class="control-label" />
										</div>
									</div>
								</spring:bind>

							</div>

							<div class="form-group">

								<spring:bind path="companyId">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<div class="col-sm-12">
											<spring:message code="add.select" var="select"/>
											<label><spring:message code="add.company" /></label>

											<form:select path="companyId" class="form-control">
												<form:option value="-1" label="--- ${select} ---" />
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
									<input type="submit" value="<spring:message code="add.add" />" class="btn btn-primary"
										id="submitButton"> <spring:message code="add.or" /> <a href="${computersUrl}"
										class="btn btn-default"><spring:message code="add.cancel" /></a>
								</div>
							</div>
						</fieldset>
					</form:form>
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