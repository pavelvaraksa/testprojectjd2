<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>
<h3>Welcome, Enter The User Details</h3>
<form:form method="POST"
           action="/cars" modelAttribute="carCreateRequest">
    <table>
        <tr>
            <td><form:label path="model">Model</form:label></td>
            <td><form:input type="text" path="model"/>${carCreateRequest.model}</td>
        </tr>
        <tr>
            <td><form:label path="color">Color</form:label></td>
            <td><form:input type="text" path="color"/></td>
        </tr>
        <tr>
            <td><form:label path="userId">UserId</form:label></td>
            <td><form:input type="text" path="userId"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>