<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>AgriConnect. パスワードリセットエラー</title>
<link rel="stylesheet" href="css/password_reset_error.css">
</head>
<body>
	<div class="container">
		<header>
			<a href="${pageContext.request.contextPath}/product_lineup"> <img
				src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
		</header>
		<main>
			<h1>パスワードリセットエラー</h1>
			<p>申し訳ありませんが、パスワードリセットのリクエストに問題が発生しました。</p>

			<c:if test="${not empty errorMessage}">
				<div class="error-message">
					<p>${errorMessage}</p>
				</div>
			</c:if>

			<p>
				<a href="${pageContext.request.contextPath}/password_reset">再度リセットをリクエスト</a>
			</p>
			<p>
				<a href="${pageContext.request.contextPath}/user_login">ログイン画面に戻る</a>
			</p>
		</main>
	</div>
</body>
</html>
