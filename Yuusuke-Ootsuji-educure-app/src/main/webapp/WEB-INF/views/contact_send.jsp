<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. お問い合わせ完了画面</title>
    <link rel="stylesheet" href="css/contact_send.css">
</head>
<body>
    <div class="container">
        <header class="header">
			<a href="${pageContext.request.contextPath}/product_lineup"> <img
				src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
			<form:form action="${pageContext.request.contextPath}/search_result"
				method="get" modelAttribute="searchForm" class="search-form">
				<form:input type="text" path="search" placeholder="何をお探しですか"
					class="search-input" />
				<button type="submit" class="btn">検索</button>
			</form:form>
			<div class="welcome-message">ようこそ、${loginUserName}さん</div>
		</header>
		<nav class="nav">
			<ul>
				<li><a href="${pageContext.request.contextPath}/mypage">マイページ</a></li>
				<li><a href="${pageContext.request.contextPath}/sell">出品</a></li>
				<li><a href="${pageContext.request.contextPath}/contact">お問い合わせ</a></li>
				<li><a href="${pageContext.request.contextPath}/logout">ログアウト</a></li>
			</ul>
        <main>
			<p>お問い合わせ内容を受け付けました。<br>返信までしばらくお待ちください。</p>
			<a href="${pageContext.request.contextPath}/product_lineup">商品一覧に戻る</a>
		</main>
    </div>
</body>
</html>