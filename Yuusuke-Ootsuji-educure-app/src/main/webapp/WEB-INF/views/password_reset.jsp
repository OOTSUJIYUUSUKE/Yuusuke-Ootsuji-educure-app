<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. パスワードリセット画面</title>
    <link rel="stylesheet" href="css/password_reset.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
        <a href="${pageContext.request.contextPath}/user_login" class="back-link">&lt; 戻る</a>
        <main>
            <h1 class="password-reset">パスワードリセット</h1>
            <p>パスワードのリセットリンクを送信するメールアドレスを入力してください。</p>
            <form:form action="${pageContext.request.contextPath}/password_reset" method="post" modelAttribute="passwordResetForm" class="reset-form">
                <div class="form-group">
                    <form:label path="email">メールアドレス</form:label>
                    <form:input path="email" id="email"/>
                    <c:if test="${not empty emailError}">
						<div class="error-message">${emailError}</span>
					</c:if>
					<c:if test="${not empty errorMessage}">
						<div class="error-message">${errorMessage}</div>
					</c:if>
				</div>
                <button type="submit" class="btn">送信</button>
            </form:form>
        </main>
    </div>
</body>
</html>
