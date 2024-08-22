<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AgriConnect. ユーザー新規登録画面</title>
<link rel="stylesheet" href="css/user_register.css">
</head>

<body>
	<div class="container">
		<header>
			<a href="${pageContext.request.contextPath}/product_lineup"> <img
				src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a> <a href="${pageContext.request.contextPath}/top" class="back-link">&lt;
				戻る</a>
		</header>
		<main>
			<h1 class="title">新規登録</h1>
			<c:if test="${not empty errorMessage}">
				<div class="error-message">
					<c:out value="${errorMessage}" />
				</div>
			</c:if>
			<form:form method="post"
				action="${pageContext.request.contextPath}/user_register"
				modelAttribute="registerForm" class="register-form">
				<div class="form-group">
					<label for="userId">ユーザーID</label>
					<form:input type="text" id="userId" path="userId" />
					<c:choose>
                        <c:when test="${not empty userIdExistsError}">
                            <span class="error-message">${userIdExistsError}</span>
                        </c:when>
                        <c:when test="${not empty userIdError}">
                            <span class="error-message">${userIdError}</span>
                        </c:when>
                    </c:choose>
				</div>
				<div class="form-group">
					<label for="username">ユーザー名</label>
					<form:input type="text" id="userName" path="userName" />
					<c:if test="${not empty userNameError}">
						<span class="error-message">${userNameError}</span>
					</c:if>
				</div>
				<div class="form-group">
					<label for="email">メールアドレス</label>
					<form:input type="email" id="email" path="email" />
					<c:if test="${not empty emailError}">
						<span class="error-message">${emailError}</span>
					</c:if>
				</div>
				<div class="form-group">
					<label for="address">住所</label>
					<form:input type="text" id="address" path="address" />
					<c:if test="${not empty addressError}">
						<span class="error-message">${addressError}</span>
					</c:if>
				</div>
				<div class="form-group">
					<label for="phone">電話番号</label>
					<form:input type="tel" id="telephone" path="telephone" />
					<c:choose>
                        <c:when test="${not empty telephoneExistsError}">
                            <span class="error-message">${telephoneExistsError}</span>
                        </c:when>
                        <c:when test="${not empty telephoneError}">
                            <span class="error-message">${telephoneError}</span>
                        </c:when>
                    </c:choose>
				</div>
				<div class="form-group">
					<label for="password">パスワード</label>
					<form:password id="passwordHash" path="passwordHash" />
					<c:if test="${not empty passwordError}">
						<span class="error-message">${passwordError}</span>
					</c:if>
				</div>
				<div class="form-group">
					<label for="confirmPassword">パスワード（再入力）</label>
					<form:password id="confirmPassword" path="confirmPassword" />
					<c:if test="${not empty confirmPasswordError}">
						<span class="error-message">${confirmPasswordError}</span>
					</c:if>
					<c:if test="${not empty passwordUnmatchError}">
						<span class="error-message">${passwordUnmatchError}</span>
					</c:if>
				</div>
				<button type="submit" class="btn">登録</button>
			</form:form>
		</main>
	</div>
</body>

</html>
