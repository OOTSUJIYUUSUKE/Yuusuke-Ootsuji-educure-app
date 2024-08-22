<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AgriConnect. 決済エラー</title>
<link rel="stylesheet" href="css/product_purchase_error.css">
</head>

<body>
	<div class="container">
		<header class="header">
			<a href="${pageContext.request.contextPath}/product_lineup"> <img
				src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
			<form class="search-form">
				<input type="text" placeholder="何をお探しですか" name="search"> <a href="${pageContext.request.contextPath}/search_result" class="btn">検索</a>
			</form>
			<div class="welcome-message">ようこそ、${loginUserName}さん</div>
		</header>
		<a href="${pageContext.request.contextPath}/product_detail?product_id=${product.productId}"class="back-link">&lt; 戻る</a>
		<nav class="nav">
			<ul>
				<li><a href="${pageContext.request.contextPath}/mypage">マイページ</a></li>
				<li><a href="${pageContext.request.contextPath}/sell">出品</a></li>
				<li><a href="${pageContext.request.contextPath}/contact">お問い合わせ</a></li>
				<li><a href="${pageContext.request.contextPath}/logout">ログアウト</a></li>
			</ul>
		</nav>
		<main class="success-message">
			<h1>商品が購入できませんでした。</h1>
			<p>決済時にエラーが発生しました。<br>決済内容をお確かめの上、もう一度お試しください。</p>

			<a href="${pageContext.request.contextPath}/product_lineup"
				class="btn">商品一覧に戻る</a>
		</main>
		<script src="js/scripts.js"></script>
	</div>
</body>

</html>