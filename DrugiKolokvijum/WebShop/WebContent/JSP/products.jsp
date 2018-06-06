<%@page import="beans.Product"%>
<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="products" class="dao.ProductDAO" scope="application"/>
<!-- Koristi objekat registrovan pod ključem "user" iz sesije -->
<jsp:useBean id="user" class="beans.User" scope="session"/>
<html>
<head>
</head>
<body>
	<p>Dobrodošli, <%=user.getFirstName()%> <a href="LogoutServlet">Logout</a></p>
	<table border="1">
		<thead>
			<th>Naziv</th>
			<th>Cena</th>
			<th colspan="2">Količina</th>
		</thead>
		<tbody>
		<!-- TODO 3: Izlistavanje proizvoda -->
			<c:forEach var="product" items="${products.findAll()}">
				<tr>		
					<td>${product.name}</td>
					<td>${product.price}</td>
					<td><a href="AddProductServlet?itemID=${product.id}">Dodaj!</a>
					<td>1</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>