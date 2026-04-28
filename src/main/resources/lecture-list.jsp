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
    <li>name: ${lecture.name}</li>
    <li>price: ${lecture.price}</li>
    <a href="/lecture-edit.jsp?id=${lecture.id}&name=${lecture.name}&price=${lecture.price}">수 정</a>
    <button onclick="deleteLecture(${lecture.id})">삭 제</button>
    <br>
</c:forEach>

<script>
    function deleteLecture(id) {
        fetch("http://localhost:8080/lectures", {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: id })
        }).then(()=> {
            window.location.reload();
        });
    }
</script>
</body>
</html>
