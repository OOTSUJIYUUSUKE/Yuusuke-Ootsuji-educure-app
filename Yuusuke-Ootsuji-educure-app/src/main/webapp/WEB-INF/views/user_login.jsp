<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ログイン画面</title>
    <link rel="stylesheet" href="css/user_login.css">
</head>

<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/top" class="back-link">&lt; 戻る</a>
        </header>
        <main>
            <h1>ログイン</h1>
            <form:form action="${pageContext.request.contextPath}/user_login"
			method="post" modelAttribute="loginForm" class="login-form">
                <div class="form-group">
                    <label for="userId">ユーザーID</label>
                    <form:input path="userId" type="text" id="userId" />
					<c:if test="${not empty userIdError}">
						<p style="color: red;">${userIdError}</p>
					</c:if>
				</div>
                <div class="form-group">
                    <label for="password">パスワード</label>
                    <form:input path="password" type="password" id="password" />
					<c:if test="${not empty passwordError}">
						<p style="color: red;">${passwordError}</p>
					</c:if>
				</div>
                <button type="submit" class="btn">ログイン</button>
            </form:form>
            <div style="color: red;">
			<c:if test="${not empty errorMessage}">
				<p>${errorMessage}</p>
			</c:if>
		    </div>
            <a href="${pageContext.request.contextPath}/password_reset" class="password-reset">パスワードを忘れた</a>
        </main>
    </div>
</body>

</html>