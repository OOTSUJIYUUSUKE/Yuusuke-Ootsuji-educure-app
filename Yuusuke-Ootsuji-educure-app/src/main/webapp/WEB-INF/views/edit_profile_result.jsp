<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. プロフィール変更完了画面</title>
    <link rel="stylesheet" href="css/edit_profile_result.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/product_lineup">
			    <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
			<form:form action="${pageContext.request.contextPath}/search_result" method="get" modelAttribute="searchForm" class="search-form">
				<form:input type="text" path="search" placeholder="何をお探しですか" class="search-input" />
				<button type="submit" class="btn">検索</button>
			</form:form>
			<div class="welcome-message">ようこそ、${loginUserName}さん</div>
        </header>
       <main>
		   <p>プロフィールを変更しました。</p>
           <a href="${pageContext.request.contextPath}/mypage">マイページに戻る</a>
       </main>
    </div>
</body>
</html>