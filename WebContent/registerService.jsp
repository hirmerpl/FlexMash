<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/json" />
<title>Register a new Service</title>
</head>
<body>

	<form
		action="${pageContext.request.contextPath}/RegisterServiceServlet"
		method="post" >
		<table>
			<tr >
				<td>Service Name:</td>
				<td><input type="text" name="servicename" size="420px" style="width:420px"></td>
			</tr>
			<tr>
				<td>Service Description:</td>
				<td><input type="text" name="description" size="420px" style="width:420px"></td>
			</tr>
			<tr>
				<td>Service Parameters:</td>
				<td><input type="text" name="parameters" size="420px" style="width:420px"></td>
			</tr>
			<tr>
				<td>Service Tags:</td>
				<td><input type="text" name="tags" size="420px" style="width:420px"></td>
			</tr>
			<tr>
				<td>Service Healthcheck address:</td>
				<td><input type="text" name="healthcheck" size="420px" style="width:420px"></td>
			</tr>
			<tr>
				<td>Service Address:</td>
				<td><input type="text" name="serviceaddress" size="420px" style="width:420px"></td>
			</tr>
			<tr>
				<td>Service Port:</td>
				<td><input type="text" name="port" size="420px" style="width:420px"></td>
			</tr>
			<tr>
				<td>Service Healthcheck TTL:</td>
				<td><input type="text" name="ttl" size="420px" style="width:420px"></td>
			</tr>
		</table>
		<input type="submit" value="submit">
	</form>
</body>
</html>