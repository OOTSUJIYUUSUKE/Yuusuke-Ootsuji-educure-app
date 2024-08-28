<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ユーザー情報変更画面</title>
    <link rel="stylesheet" href="css/admin_edit_profile.css">
</head>

<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/admin_user_detail?userId=${user.userId}" class="back-link">&lt; 戻る</a>
        </header>
        <main>
            <h1 class="title">ユーザー情報</h1>
            <form:form method="post" action="${pageContext.request.contextPath}/admin_edit_profile" modelAttribute="user" class="profile-form">
					<div class="form-group">
						<label for="userId">ユーザーID <span class="note">※IDは変更できません</span></label>
						<form:input path="userId" id="userId" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="userName">ユーザー名</label>
						<form:input path="userName" id="userName" />
					</div>
					<div class="form-group">
						<label for="email">メールアドレス</label>
						<form:input type="email" path="email" id="email" />
					</div>
					<div class="form-group">
						<label for="address">住所</label>
						<form:input path="address" id="address" />
					</div>
					<div class="form-group">
						<label for="telephone">電話番号</label>
						<form:input type="tel" path="telephone" id="telephone" />
					</div>
					<div class="form-group">
                        <label for="roleName">権限</label>
                        <form:select path="roleName" id="roleName">
                            <form:option value="一般" label="一般" />
                            <form:option value="管理者" label="管理者" />
                        </form:select>
                    </div>
                    <button type="submit" class="btn">変更</button>
                </form:form>
        </main>
    </div>
</body>
</html>
