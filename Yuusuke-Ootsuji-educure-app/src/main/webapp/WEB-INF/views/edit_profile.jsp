<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AgriConnect. プロフィール変更画面</title>
<link rel="stylesheet" href="css/edit_profile.css">
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
            <h1 class="title">プロフィール変更</h1>
            <div class="form-container">
                <form:form method="post" action="${pageContext.request.contextPath}/edit_profile" modelAttribute="userEditForm" class="profile-form">
					<div class="form-group">
						<label for="userId">ユーザーID <span class="note">※IDは変更できません</span></label>
						<form:input path="userId" id="userId" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="userName">ユーザー名</label>
						<form:input path="userName" id="userName"/>
						<c:if test="${not empty userNameError}">
							<span class="error-message">${userNameError}</span>
						</c:if>
					</div>
					<div class="form-group">
						<label for="email">メールアドレス</label>
						<form:input path="email" id="email"/>
						<c:if test="${not empty emailError}">
							<span class="error-message">${emailError}</span>
						</c:if>
					</div>
					<div class="form-group">
						<label for="address">住所</label>
						<form:input path="address" id="address"/>
						<c:if test="${not empty addressError}">
							<span class="error-message">${addressError}</span>
						</c:if>
					</div>
					<div class="form-group">
						<label for="telephone">電話番号</label>
						<form:input path="telephone" id="telephone"/>
						<c:choose>
							<c:when test="${not empty telephoneExistsError}">
								<span class="error-message">${telephoneExistsError}</span>
							</c:when>
							<c:when test="${not empty telephoneError}">
								<span class="error-message">${telephoneError}</span>
							</c:when>
						</c:choose>
					</div>
                    <button type="submit" class="btn">変更</button>
                </form:form>
            </div>
        </main>
    </div>
</body>

</html>