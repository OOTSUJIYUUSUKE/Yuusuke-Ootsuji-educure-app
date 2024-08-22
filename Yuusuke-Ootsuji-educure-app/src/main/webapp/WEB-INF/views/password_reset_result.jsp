<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>AgriConnect. パスワードリセット完了画面</title>
<link rel="stylesheet" href="css/password_reset_result.css">
</head>
<body>
	<div class="container">
		<header>
			<a href="${pageContext.request.contextPath}/product_lineup"> <img
				src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
		</header>
		<main>
			<h1>パスワードリセットが完了しました</h1>
        <p>新しいパスワードが正常に設定されました。ログイン画面に戻って、ログインしてください。</p>
        <a href="${pageContext.request.contextPath}/user_login">ログイン画面に戻る</a>
		</main>
	</div>
</body>