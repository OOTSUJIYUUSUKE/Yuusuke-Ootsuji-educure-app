<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 注文管理画面</title>
    <link rel="stylesheet" href="css/admin_order_management.css">
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
			<p>注文検索（空白の場合は全件表示します）</p>
            <form:form method="get" action="${pageContext.request.contextPath}/admin_order_management" modelAttribute="orderSearchForm" class="search-form">
                <div class="form-group">
                    <label for="orderId">注文ID</label>
                    <form:input path="orderId" id="orderId" />
                </div>
                <button type="submit" class="btn">検索</button>
            </form:form>

            <table class="order-table">
                <thead>
                    <tr>
                        <th>注文ID</th>
                        <th>商品ID</th>
                        <th>商品名</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orderList}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/admin_order_detail?orderId=${order.orderId}" class="order-detail-link">${order.orderId}</a></td>
                            <td>${order.productId}</td>
                            <td>${productNames[order.orderId]}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
<body>

</body>
</html>