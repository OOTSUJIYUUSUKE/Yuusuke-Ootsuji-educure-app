<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AgriConnect. プロフィール画面</title>
<link rel="stylesheet" href="css/user_profile.css">
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
		<a href="${pageContext.request.contextPath}/mypage" class="back-link">&lt;戻る</a>
		<main>
			<h1 class="title">プロフィール</h1>
			<div class="form-container">
				<form:form modelAttribute="user" class="profile-form">
					<div class="form-group">
						<label for="userId">ユーザーID</label>
						<form:input path="userId" id="userId" name="userId" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="userName">ユーザー名</label>
						<form:input path="userName" id="userName" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="email">メールアドレス</label>
						<form:input path="email" id="email" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="address">住所</label>
						<form:input path="address" id="address" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="telephone">電話番号</label>
						<form:input path="telephone" id="telephone" readonly="true" />
					</div>
					<a href="${pageContext.request.contextPath}/edit_profile" class="btn">プロフィールを変更する</a>
				</form:form>
			</div>
		</main>
	</div>
</body>

</html>
