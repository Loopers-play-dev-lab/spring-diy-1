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
    <div id = "${lecture.lectureId}">
        <li>id: <a href="/lecture-registration?lectureId=${lecture.lectureId}">${lecture.lectureId}</a></li>
        <li>name: ${lecture.name}</li>
        <li>price: ${lecture.price}</li>
        <button id=`del-${lecture.lectureId}`>delete</button>
    </div>
    <br>
</c:forEach>

<script>
    buttons = document.querySelectorAll("button");
    buttons.forEach(button => {
        button.addEventListener("click", () => {
            const lectureId = button.parentElement.id;
            fetch('/lectures?lectureId=' + lectureId, {
                method: "DELETE"
            }).then(res => {
                console.log(res)
                if (res.ok) {
                    window.location.href = "/lectures";
                }
            })
        })
    })
</script>
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