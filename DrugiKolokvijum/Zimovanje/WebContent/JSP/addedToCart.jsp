<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="beans.ShoppingCart"%>
<%@page import="beans.ShoppingCartItem"%>
<%@page import="beans.Product"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<table border="1">
		<thead>
			<th>Naziv</th>
			<th>Cena</th>
			<th>Količina</th>
		</thead>
		<tbody>
		<!-- TODO 3: Izlistavanje proizvoda -->			
			<c:forEach var="product" items="${cart.getItems()}">
				<tr>		
					<td>${product.getProduct().name}</td>
					<td>${product.getProduct().price}</td>
					<td>1</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>