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
    <div class="lecture-item" data-id="${lecture.id}">
        <li>id: ${lecture.id}</li>
        <li>name: <input type="text" class="name-input" value="${lecture.name}"></li>
        <li>price: <input type="number" class="price-input" value="${lecture.price}"></li>
        <button class="edit-btn">수정</button>
        <button class="delete-btn">삭제</button>
    </div>
    <br>
</c:forEach>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        document.querySelectorAll(".edit-btn").forEach(function(btn) {
            btn.addEventListener("click", function() {
                const item = btn.closest(".lecture-item");
                const id = item.dataset.id;
                const name = item.querySelector(".name-input").value;
                const price = item.querySelector(".price-input").value;

                const lecture = { id: id, name: name, price: price };

                fetch("http://localhost:8080/lectures", {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(lecture)
                }).then(function(response) {
                    if (response.ok) {
                        window.location.reload();
                    }
                });
            });
        });

        document.querySelectorAll(".delete-btn").forEach(function(btn) {
            btn.addEventListener("click", function() {
                const item = btn.closest(".lecture-item");
                const id = item.dataset.id;

                fetch("http://localhost:8080/lectures", {
                    method: "DELETE",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ id: id })
                }).then(function(response) {
                    if (response.ok) {
                        window.location.reload();
                    }
                });
            });
        });
    });
</script>
</body>
</html>
