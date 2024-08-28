<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. ユーザー管理画面</title>
    <link rel="stylesheet" href="css/admin_user_management.css">
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
			<p>ユーザー検索（両方空白の場合は全件表示します）</p>
            <form:form method="get" action="${pageContext.request.contextPath}/admin_user_management" modelAttribute="userSearchForm" class="search-form">
                <div class="form-group">
                    <label for="userId">ユーザーID</label>
                    <form:input path="userId" id="userId" />
                </div>
                <div class="form-group">
                    <label for="email">メールアドレス</label>
                    <form:input path="email" id="email" />
                </div>
                <button type="submit" class="btn">検索</button>
            </form:form>
            <table class="user-table">
                <thead>
                    <tr>
                        <th>ユーザーID</th>
                        <th>ユーザー名</th>
                        <th>メールアドレス</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${userList}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/admin_user_detail?userId=${user.userId}" class="user-detail-link">${user.userId}</a></td>
                            <td>${user.userName}</td>
                            <td>${user.email}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
<body>

</body>
</html>