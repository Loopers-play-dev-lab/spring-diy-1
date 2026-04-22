<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 수정</title>
</head>
<body>
<form id="updateForm">
    ID:${lecture.id}
    <label for="name">이름:</label>
    <input type="text" id="name" name="name" value="${lecture.name}" required>
    <label for="price">가격:</label>
    <input type="number" id="price" name="price" value="${lecture.price}" required>
    <button type="submit">수정</button>
</form>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const updateForm = document.getElementById("updateForm");

        updateForm.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(updateForm);

            const lecture = {};
            formData.forEach((value, key) => {
                lecture[key] = value;
            });

            const jsonData = JSON.stringify(lecture);

            fetch("http://localhost:8080/lectures/${lecture.id}", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: jsonData
            }).then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                }
            });
        });
    });
</script>
</body>
</html>
