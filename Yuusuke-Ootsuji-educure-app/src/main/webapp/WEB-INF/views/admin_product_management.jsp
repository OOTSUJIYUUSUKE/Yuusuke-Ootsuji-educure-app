<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 商品管理画面</title>
    <link rel="stylesheet" href="css/admin_product_management.css">
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
			<p>商品検索（両方空白の場合は全件表示します）</p>
            <form:form method="get" action="${pageContext.request.contextPath}/admin_product_management" modelAttribute="productSearchForm" class="search-form">
                <div class="form-group">
                    <label for="productId">商品ID</label>
                    <form:input path="productId" id="productId" />
                </div>
                <div class="form-group">
                    <label for="userId">出品者ID</label>
                    <form:input path="userId" id="userId" />
                </div>
                <button type="submit" class="btn">検索</button>
            </form:form>

            <table class="product-table">
                <thead>
                    <tr>
                        <th>商品ID</th>
                        <th>商品名</th>
                        <th>出品者ID</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${productList}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/admin_product_detail?productId=${product.productId}" class="product-detail-link">${product.productId}</a></td>
                            <td>${product.productName}</td>
                            <td>${product.userId}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
<body>

</body>
</html>