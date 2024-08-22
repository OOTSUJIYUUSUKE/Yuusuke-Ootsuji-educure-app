<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. マイページ画面</title>
    <link rel="stylesheet" href="css/mypage.css">
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
        <a href="${pageContext.request.contextPath}/product_lineup" class="back-link">&lt; 戻る</a>
        <main>
            <h1 class="title">マイページ</h1>
            <nav class="menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/user_profile" class="menu-link">プロフィールの表示・編集</a></li>
                    <li><a href="${pageContext.request.contextPath}/buy_history" class="menu-link">購入履歴の表示</a></li>
                    <li><a href="${pageContext.request.contextPath}/sell_history" class="menu-link">出品履歴の表示</a></li>
                    <li><a href="${pageContext.request.contextPath}/user_delete" class="menu-link">サービスの退会</a></li>
                </ul>
            </nav>
        </main>
    </div>
</body>
</html>