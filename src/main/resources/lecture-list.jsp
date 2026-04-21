<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 목록</title>
</head>
<body>
<a href="/lecture-registration.jsp">등록</a>
<c:forEach var="lecture" items="${lectures}">
    <li>id: ${lecture.id}</li>
    <li>title: ${lecture.title}</li>
    <li>lecturer: ${lecture.lecturer}</li>
    <br>
</c:forEach>
</body>
</html>
