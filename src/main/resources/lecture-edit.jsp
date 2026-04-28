<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 수정</title>
</head>
<body>
<form id="editForm">
    <input type="hidden" id="id" name="id">
    <label for="name">이름:</label>
    <input type="text" id="name" name="name" required>
    <label for="price">가격:</label>
    <input type="number" id="price" name="price" required>
    <button type="submit">수정</button>
    <a href="/lectures">취소</a>
</form>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const editForm = document.getElementById("editForm");
        const resultDiv = document.getElementById("result");

        const params = new URLSearchParams(window.location.search);
        document.getElementById("id").value = params.get("id");
        document.getElementById("name").value = params.get("name");
        document.getElementById("price").value = params.get("price");

        editForm.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(editForm);

            const lecture = {
                id: parseInt(document.getElementById("id").value),
                name: document.getElementById("name").value,
                price: parseInt(document.getElementById("price").value)
            };

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
                if (response.redirected) {
                    window.location.href = response.url;
                }
                else {
                    window.location.href = "/lectures";
                }
            });
        });
    });
</script>
</body>
</html>
