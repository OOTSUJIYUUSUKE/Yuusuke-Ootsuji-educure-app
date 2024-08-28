<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. サポート管理画面</title>
    <link rel="stylesheet" href="css/admin_contact_management.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/logout">ログアウト</a>
        </header>
        <a href="${pageContext.request.contextPath}/admin_dashboard" class="back-link">&lt; 戻る</a>
        <main>
			<p>サポート検索（サポートIDが空白の場合は該当の対応状況のものを全件表示します）</p>
            <form:form method="get" action="${pageContext.request.contextPath}/admin_contact_management" modelAttribute="contactSearchForm" class="search-form">
                <div class="form-group">
                    <label for="contactId">サポートID</label>
                    <form:input path="contactId" id="contactId" />
                </div>
                <div class="form-group">
                    <label for="status">対応状況</label>
					<form:select path="status" id="status">
						<form:option value="" label="すべて" />
						<form:option value="未対応" label="未対応" />
						<form:option value="対応中" label="対応中" />
						<form:option value="対応済" label="対応済" />
					</form:select>
				</div>
                <button type="submit" class="btn">検索</button>
            </form:form>
            <c:if test="${not empty errorMessage}">
				<p>${errorMessage}</p>
			</c:if>
            <table class="support-table">
                <thead>
                    <tr>
                        <th>サポートID</th>
                        <th>ユーザー名</th>
                        <th>対応状況</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="contact" items="${contactList}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/admin_contact_detail?contactId=${contact.contactId}" class="support-detail-link">${contact.contactId}</a></td>
                            <td>${contact.userName}</td>
                            <td>${contact.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
<body>

</body>
</html>