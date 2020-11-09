<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>System Cars</title>
</head>
<body>
<div>
    <h1>System Cars</h1>
</div>
<div>
    <table>
        <tr>
            <td>Car Id</td>
            <td>Model</td>
            <td>Year</td>
            <td>Color</td>
            <td>Price</td>
            <td>UserId</td>
            <td>Edit</td>
            <td>Delete</td>
        </tr>
        <c:forEach var="car" items="${cars}">
            <tr>
                <td>${car.id}</td>
                <td>${car.model}</td>
                <td>${car.year}</td>
                <td>${car.color}</td>
                <td>${car.price}</td>
                <td>${car.userId}</td>
                <td><button>Edit</button></td>
                <td><button>Delete</button></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>