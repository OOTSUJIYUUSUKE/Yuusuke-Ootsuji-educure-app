<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 新パスワード入力画面</title>
    <link rel="stylesheet" href="css/set_new_password.css">
</head>

<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
        <main>
            <h1>新しいパスワードを入力して下さい</h1>
            <div class="password-form">
                <form:form action="${pageContext.request.contextPath}/set_new_password" method="post" modelAttribute="newPasswordForm">
                    <div class="form-group">
                        <form:label path="newPassword">新しいパスワード:</form:label>
                        <form:password path="newPassword" />
                    </div>
                    <div class="form-group">
                        <form:label path="confirmPassword">再入力パスワード:</form:label>
                        <form:password path="confirmPassword" />
                    </div>
                    <div>
                        <form:hidden path="token" value="${token}" />
                    </div>
                    <div>
                        <input type="submit" value="パスワードを設定" class="btn" />
                    </div>
                </form:form>
                <c:if test="${not empty errorMessage}">
                    <p style="color: red;">${errorMessage}</p>
                </c:if>
            </div>
        </main>
    </div>
</body>

</html>

