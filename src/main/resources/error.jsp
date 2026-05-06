<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sum.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang='en'>
<head>
    <meta charset="UTF-8">
    <title>에러 발생</title>
</head>
<body>
<h1>시스템에 문제가 생겼습니다.</h1>
<hr>
<p><strong>>발생한 에러:</strong>
    <c:choose>
        <c:when test="${not empty pageContext.exception}">
            <p><span class="label">상세 내용: </span> <c:out value="${pageContext.exception.message}" /> </p>
        </c:when>
        <c:when test="${not empty requestScope['javax.servlet.error.message']}">
            <p><span class="label">상세 내용: </span>
                <c:out value="${requestScope['javax.servlet.error.message']}" />
            </p>
        </c:when>
        <c:otherwise>
            <p><span class="label">알 수 없는 에러가 발생했습니다.</span></p>
        </c:otherwise>
    </c:choose>
</p>
<a href="/lectures">조회 페이지로 돌아가기</a>
</body>
</html>

