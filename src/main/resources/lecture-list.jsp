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
    <button type="button"
        class="btn-delete"
        data-id="${lecture.id}">삭제</button>
    <br>
</c:forEach>
<form id="registrationForm">
    <label for="id">id:</label>
    <input type="number" id="id" name="id" required>
    <label for="name">이름:</label>
    <input type="text" id="name" name="name" required>
    <label for="price">가격:</label>
    <input type="number" id="price" name="price" required>
    <button type="submit">수정</button>
</form>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const registrationForm = document.getElementById("registrationForm");
        const resultDiv = document.getElementById("result");

        registrationForm.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(registrationForm);

            const lecture = {};
            formData.forEach((value, key) => {
                lecture[key] = value;
            });

            const jsonData = JSON.stringify(lecture);

            fetch("http://localhost:8080/lectures", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: jsonData
            }).then(response => {
                location.reload(true);
            });
        });

        const buttons = document.getElementsByClassName("btn-delete");
        for (let btn of buttons) {
            btn.addEventListener("click", function(event) {
                event.preventDefault();
                const body = { "id" : btn.dataset.id };
                const jsonData = JSON.stringify(body);

                fetch("http://localhost:8080/lectures", {
                    method: "DELETE",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: jsonData
                }).then(response => {
                    location.reload(true);
                });
            });
        }
    });
</script>
</body>
</html>
