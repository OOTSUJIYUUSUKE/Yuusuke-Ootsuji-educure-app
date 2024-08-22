<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. サービス退会画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user_delete.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <form:form action="${pageContext.request.contextPath}/search_result"
				method="get" modelAttribute="searchForm" class="search-form">
				<form:input type="text" path="search" placeholder="何をお探しですか"
					class="search-input" />
				<button type="submit" class="btn">検索</button>
			</form:form>
            <div class="welcome-message">ようこそ、${loginUserName}さん</div>
        </header>
        <a href="${pageContext.request.contextPath}/mypage" class="back-link">&lt; 戻る</a>
        <main>
            <h1>サービス退会</h1>
            <p class="delete_message">サービスを退会してもよろしいですか？<br>よろしければパスワードを入力の上、退会ボタンを押して下さい。</p>
            <form:form action="${pageContext.request.contextPath}/user_delete" method="post" modelAttribute="userDeleteForm" class="delete-form">
                <form:password path="password" placeholder="パスワード" id="password" class="password-input" required="true" />
                <button type="submit" class="btn">退会</button>
            </form:form>
        </main>
    </div>
</body>
</html>
