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
    <button onclick="editLecture(${lecture.id})">수정</button>
    <button onclick="deleteLecture(${lecture.id})">삭제</button>
    <br>
</c:forEach>

<script>
    function editLecture(id) {
        const name = prompt("새 이름:");
        const price = prompt("새 가격:");
        if (name && price) {
            fetch("/lectures", {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: String(id), name: name, price: price })
            }).then(() => location.reload());
        }
    }

    function deleteLecture(id) {
        if (confirm("삭제하시겠습니까?")) {
            fetch("/lectures", {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: String(id) })
            }).then(() => location.reload());
        }
    }
</script>
</body>
</html>
