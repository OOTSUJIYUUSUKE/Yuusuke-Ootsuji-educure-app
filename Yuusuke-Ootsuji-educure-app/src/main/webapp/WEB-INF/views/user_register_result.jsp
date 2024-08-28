<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ユーザー登録完了画面</title>
    <link rel="stylesheet" href="css/user_register_result.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/product_lineup">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
        </header>
       <main>
			<p>ユーザーを新規登録しました。</p>
			<c:if test="${not empty emailError}">
				<div class="error-message">${emailError}</div>
			</c:if>
			<a href="${pageContext.request.contextPath}/product_lineup" class="btn">商品を見に行く</a>
       </main>
    </div>
</body>
</html>
