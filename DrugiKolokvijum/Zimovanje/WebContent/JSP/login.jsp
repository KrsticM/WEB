<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
</head>
<body>
<form action="LoginServlet" method="POST">
<table>
	<tr>
		<td>Username</td>
		<td><input type="text" name ="username"/></td>
	</tr>
	<tr>
		<td>Password</td>
		<td><input type="password" name ="password"/></td>
	</tr>
	<tr><td><input type="submit" value="Login"></td></tr>
</table>
</form>
	<!-- Prikaži grešku, ako je bilo -->
	<c:if test="${not empty err}">
	<p style="color: red">${err}></p>
	</c:if>
</body>
</html>