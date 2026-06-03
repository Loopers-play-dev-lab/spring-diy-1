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
<button id="rest-button">rest get test</button>
<button id="rest-post-button">rest post test</button>
<c:forEach var="lecture" items="${lectures}">
    <li>id: ${lecture.id}</li>
    <li>pw: ${lecture.name}</li>
    <li>pw: ${lecture.price}</li>
    <br>
</c:forEach>
</body>
<script>
    document.getElementById("rest-button").addEventListener("click", function () {
        fetch("http://localhost:8080/lecture?id=test").then(res => res.json()).then(data => console.log(data));
    });
    document.getElementById("rest-post-button").addEventListener("click", function () {
        fetch("http://localhost:8080/lecture", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: "test"
            })
        }).then(res => res.json()).then(data => console.log(data));
    });
</script>
</html>