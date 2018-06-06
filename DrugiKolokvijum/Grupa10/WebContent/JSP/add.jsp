<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Dodavanje proizvoda</title>
<script type="text/javascript">
	function validacija(form) {
		var error = 0;
		var proizv = form.elements['proizvodjac'];
		if (!proizv.value) {
			error = 1;
			proizv.style.borderColor = 'red';
		}

		var cenatbl = form.elements['cena'];
		if (!cenatbl.value) {
			error = 1;
			proizv.style.borderColor = 'red';
		}

		return error === 0;
	}
</script>

</head>
<body>
	<form action="AddServlet" method="POST"
		onsubmit="return validacija(this)">
		<table>
			<tr>
				<td>Proizvođač:</td>
				<td><input type="text" name="proizvodjac" /></td>
			</tr>
			<tr>
				<td>Tip:</td>
				<td><select name="tip">
						<option value="Telefon">Telefon</option>
						<option value="dodatna_oprema">Dodatna oprema</option>
				</select></td>
			</tr>
			<tr>
				<td>Model:</td>
				<td><input type="text" name="model" /></td>
			</tr>
			<tr>
				<td>Cena:</td>
				<td><input type="text" name="cena" /></td>
			</tr>
			<tr>
				<td>Punjač:</td>
				<td><input type="checkbox" name="punjac" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Dodaj" /></td>
			</tr>
		</table>
	</form>
	<c:if test="${not empty greska}">
		<p style="color: red">${greska}</p>
	</c:if>

	<c:if test="${fitrir eq 'true'}">
		<form action="BackupServlet">
	</c:if>
	<c:if test="${empty fitrir}">
		<form action="FiltarServlet">
	</c:if>
	<table border="1">

		<tr>
			<th>Proizvodi</th>
		</tr>
		<c:if test="${empty proizvodi.proizvodi}">
			<tr>
				<td><b>Lista je prazna!</b></td>
			</tr>
		</c:if>
		<c:if test="${not empty proizvodi.proizvodi}">
			<tr>
				<th>Proizvođač</th>
				<th>Tip</th>
				<th>Model</th>
				<th>Cena</th>
			</tr>
			<c:forEach var="i" items="${proizvodi.proizvodi}">
				<tr>
					<td>${i.proizvodjac}</td>
					<td>${i.tip}</td>
					<td>${i.model}</td>
					<td>${i.cena}</td>
					<td><a href="DeleteServlet?id=${i.id}&filtracija=${fitrir}">Obrisi</a><td>
				</tr>
			</c:forEach>

		</c:if>
	</table>
	<br />
	
	<input type="submit" value="Filtriraj" />
	</form>


</body>
</html>