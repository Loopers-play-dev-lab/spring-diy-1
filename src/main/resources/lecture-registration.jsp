<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 등록</title>
</head>
<body>
<form id="registrationForm">
    <label for="name">이름:</label>
    <input type="hidden" id="lectureId" name="lectureId" value="${lectureId}"/>
    <input type="text" id="name" name="name" value="${name}" required>
    <label for="price">가격:</label>
    <input type="number" id="price" name="price" value="${price}" required>
    <button type="submit">등록</button>
</form>

<script>
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const lectureId = urlParams.get('lectureId');
        if (lectureId == null || lectureId === '') {
            return;
        }

        fetch('http://localhost:8080/lecture?lectureId=' + lectureId)
            .then(response => {
                return response.json()
            })
            .then(data => {
                console.log(data);
                document.getElementById("lectureId").value = data.lectureId;
                document.getElementById("name").value = data.name;
                document.getElementById("price").value = data.price;
            }
            );
    }

    document.addEventListener("DOMContentLoaded", function() {
        const registrationForm = document.getElementById("registrationForm");
        const resultDiv = document.getElementById("result");

        registrationForm.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(registrationForm);

            const lecture = {};
            formData.forEach((value, key) => {
                if (value !== '' && value !== null) {
                    console.log(key, value);
                    lecture[key] = value;
                }
            });

            const jsonData = JSON.stringify(lecture);

            fetch("http://localhost:8080/lectures", {
                method: ('lectureId' in lecture) ? "PUT" : "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: jsonData
            }).then(response => {
                console.log(response);
                if (response.redirected) {
                    window.location.href = response.url;
                }
            });
        });
    });
</script>
</body>
</html>
