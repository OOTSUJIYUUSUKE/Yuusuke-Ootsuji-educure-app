<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
	
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ログアウト画面</title>
    <link rel="stylesheet" href="css/logout.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
        <main>
			<p>ログアウトしました。</p>
			<a href="${pageContext.request.contextPath}/top">トップ画面に戻る</a>
		</main>
    </div>
</body>
</html>