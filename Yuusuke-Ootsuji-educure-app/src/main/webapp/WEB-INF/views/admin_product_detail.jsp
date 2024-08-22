<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 商品詳細画面</title>
    <link rel="stylesheet" href="css/admin_product_detail.css">
</head>

<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/admin_product_management" class="back-link">&lt; 戻る</a>
        </header>
        <main>
            <h1 class="title">商品情報</h1>
            <form:form modelAttribute="product" class="product-form">
					<div class="form-group">
						<label for="productId">商品ID</label>
						<form:input path="productId" id="productId" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="productName">商品名</label>
						<form:input path="productName" id="productName" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="price">価格</label>
						<form:input path="price" id="price" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="userId">出品者ID</label>
						<form:input path="userId" id="userId" readonly="true"/>
					</div>
					<div class="form-group">
						<label for="description">商品説明</label>
						<form:input path="description" id="description" readonly="true" />
					</div>
					<a href="${pageContext.request.contextPath}/admin_product_delete_confirm?productId=${product.productId}" class="btn">商品を削除する</a>
				</form:form>
        </main>
        <script src="js/scripts.js"></script>
    </div>
</body>

</html>
