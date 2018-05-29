<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Edit Computer</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="<c:url value="list-computer" />">
				Application - Computer Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">
						id:
						<c:out value="${computer.id}" />
					</div>
					<h1>Edit Computer</h1>

					<div class="form-error">
						<c:out value="${error}" />
					</div>

					<form action="edit-computer" method="POST">
						<input type="hidden" value="<c:out value="${computer.id}"/>"
							name="id" id="id" />
						<fieldset>
							<div class="form-group has-success">
								<label for="computerName">Computer name</label> <input
									type="text" class="form-control" name="computerName"
									id="computerName" placeholder="Computer name"
									value="<c:out value="${computer.name}"/>">
								<div class="input-error" style="display: none">The name of
									the computer is mandatory and can't be empty.</div>
							</div>
							<div class="form-group has-success">
								<label for="introduced">Introduced date (yyyy-mm-dd)</label> <input
									type="text" class="form-control" name="introduced"
									id="introduced" placeholder="Introduced date"
									value="<c:out value="${computer.introduced}"/>">
								<div class="input-error" style="display: none">The date
									should be in the format yyyy-mm-dd (for example 2000-01-24).</div>
							</div>
							<div class="form-group has-success">
								<label for="discontinued">Discontinued date (yyyy-mm-dd)</label>
								<input type="text" class="form-control" name="discontinued"
									id="discontinued" placeholder="Discontinued date"
									value="<c:out value="${computer.discontinued}"/>">
								<div class="input-error" style="display: none">The date
									should be in the format yyyy-mm-dd (for example 2000-01-24).</div>
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId" name="companyId">
									<c:forEach items="${companies}" var="company">
										<c:choose>
											<c:when test="${not empty computer.company.id}">
												<c:choose>
													<c:when test="${computer.company.id == company.id}">
														<tr>
															<option value="<c:out value="${company.id}" />" selected>
																<c:out value="${company.name}" /></option>
														</tr>
													</c:when>
													<c:otherwise>
														<option value="<c:out value="${company.id}"/>">
															<c:out value="${company.name}" /></option>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<tr>
													<option value="<c:out value="${company.id}"/>">
														<c:out value="${company.name}" /></option>
												</tr>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Edit" class="btn btn-primary"
								id="submitButton"> or <a
								href="<c:url value="list-computer" />" class="btn btn-default">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
	<script src="js/jquery.min.js"></script>
	<script src="js/computer-form.js"></script>
</body>
</html>