<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">
	
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. お問い合わせフォーム画面</title>
    <link rel="stylesheet" href="css/contact.css">
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
		<nav class="nav">
			<ul>
				<li><a href="${pageContext.request.contextPath}/mypage">マイページ</a></li>
				<li><a href="${pageContext.request.contextPath}/sell">出品</a></li>
				<li><a href="${pageContext.request.contextPath}/contact">お問い合わせ</a></li>
				<li><a href="${pageContext.request.contextPath}/logout">ログアウト</a></li>
			</ul>
		</nav>
        <main class="contact-form">
            <h1>お問い合わせ</h1>
            <form:form action="${pageContext.request.contextPath}/contact" method="post" modelAttribute="contactForm">
                <form:label path="userName">名前:</form:label>
                <form:input type="text" path="userName" id="userName" required="required"/>
                
                <form:label path="email">メールアドレス:</form:label>
                <form:input type="email" path="email" id="email" required="required"/>
                
                <form:label path="subject">件名:</form:label>
                <form:input type="text" path="subject" id="subject" required="required"/>
                
                <form:label path="message">お問い合わせ内容:</form:label>
                <form:textarea path="message" id="message" rows="6" required="required"></form:textarea>
                
                <button type="submit" class="btn">送信</button>
            </form:form>
        </main>
    </div>
</body>
</html>