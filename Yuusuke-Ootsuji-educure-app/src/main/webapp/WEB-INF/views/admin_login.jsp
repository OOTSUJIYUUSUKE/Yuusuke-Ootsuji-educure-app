<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 管理者ログイン画面</title>
    <link rel="stylesheet" href="css/admin_login.css">
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
            <h1>管理者ログイン</h1>
            <form:form action="${pageContext.request.contextPath}/admin_login" method="post" modelAttribute="loginForm" class="login-form">
                <div class="form-group">
                    <label for="userId">ユーザーID</label>
                    <form:input path="userId" type="text" id="userId" />
                </div>
                <div class="form-group">
                    <label for="password">パスワード</label>
                    <form:input path="passwordHash" type="password" id="passwordHash" />
                </div>
                <button type="submit" class="btn">ログイン</button>
            </form:form>
            <div style="color: red;">
			<c:if test="${not empty errorMessage}">
				<p>${errorMessage}</p>
			</c:if>
		    </div>
        </main>
    </div>
</body>

</html>
