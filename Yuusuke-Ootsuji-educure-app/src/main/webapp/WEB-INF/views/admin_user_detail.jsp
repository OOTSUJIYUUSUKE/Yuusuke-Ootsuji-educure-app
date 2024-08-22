<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ユーザー詳細画面</title>
    <link rel="stylesheet" href="css/admin_user_detail.css">
</head>

<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/admin_user_management" class="back-link">&lt; 戻る</a>
        </header>
        <main>
            <h1 class="title">ユーザー情報</h1>
            <form:form modelAttribute="user" class="profile-form">
					<div class="form-group">
						<label for="userId">ユーザーID</label>
						<form:input path="userId" id="userId" readonly="true"/>
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
					<div class="form-group">
                        <label for="roleName">権限</label>
                        <form:input path="roleName" id="roleName" readonly="true" value="${user.roleName}" />
                    </div>
					<a href="${pageContext.request.contextPath}/admin_edit_profile?userId=${user.userId}" class="btn">プロフィールを変更する</a>
					<a href="${pageContext.request.contextPath}/admin_user_delete_confirm?userId=${user.userId}" class="btn">プロフィールを削除する</a>
				</form:form>
        </main>
    </div>
</body>

</html>
