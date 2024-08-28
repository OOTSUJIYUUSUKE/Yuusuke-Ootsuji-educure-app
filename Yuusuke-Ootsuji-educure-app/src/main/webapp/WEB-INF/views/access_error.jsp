<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>AgriConnect. アクセスエラー画面</title>
	<link rel="stylesheet" href="css/access_error.css">
</head>
<body>
	<div class="container">
		<header>
		    <a href="product_lineup">
			    <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
		    </a>
		</header>
		<main>
			<p>不正なアクセスです。</p>
			<a href="${pageContext.request.contextPath}/top" class="btn">トップ画面に戻る</a>
		</main>
	</div>
</body>

</html>
