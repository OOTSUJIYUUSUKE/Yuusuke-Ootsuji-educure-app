<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. サポート詳細画面</title>
    <link rel="stylesheet" href="css/admin_contact_detail.css">
</head>

<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/admin_contact_management" class="back-link">&lt; 戻る</a>
        </header>
        <main>
            <h1 class="title">サポート情報</h1>
			<form:form method="post" modelAttribute="contact" class="support-form">
				<div class="form-group">
					<label for="contactId">サポートID</label>
					<form:input path="contactId" id="contactId" readonly="true"/>
				</div>
				<div class="form-group">
					<label for="userName">ユーザー名</label>
					<form:input path="userName" id="userName" readonly="true"/>
				</div>
				<div class="form-group">
					<label for="subject">件名</label>
					<form:input path="subject" id="subject" readonly="true"/>
				</div>
				<div class="form-group">
					<label for="message">問い合わせ内容詳細</label>
					<form:textarea path="message" id="message" rows="4" readonly="true"></form:textarea>
				</div>
				<div class="form-group">
					<label for="status">対応状況</label>
					<form:input path="status" id="status" readonly="true"/>
				</div>
			</form:form>
		</main>
    </div>
</body>

</html>
