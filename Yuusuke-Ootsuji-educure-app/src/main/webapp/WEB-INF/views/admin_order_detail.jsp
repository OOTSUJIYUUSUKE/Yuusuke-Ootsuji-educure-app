<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AgriConnect. 注文詳細画面</title>
    <link rel="stylesheet" href="css/admin_order_detail.css">
</head>

<body>
    <div class="container">
        <header>
            <a href="${pageContext.request.contextPath}/admin_dashboard">
                <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
            </a>
            <a href="${pageContext.request.contextPath}/admin_order_management" class="back-link">&lt; 戻る</a>
        </header>
        <main>
            <h1 class="title">注文情報</h1>
			<form:form modelAttribute="order" class="order-form">
				<div class="form-group">
					<label for="orderId">注文ID</label>
					<form:input path="orderId" id="orderId" readonly="true" />
				</div>
				<div class="form-group">
					<label for="productId">商品ID</label>
					<form:input path="productId" id="productId" readonly="true" />
				</div>
				<div class="form-group">
					<label for="productName">商品名</label> <input type="text"
						id="productName" readonly="true"
						value="${productNames[order.orderId]}" />
				</div>
				<div class="form-group">
					<label for="userId">ユーザーID</label>
					<form:input path="userId" id="userId" readonly="true" />
				</div>
				<div class="form-group">
					<label for="createdAt">注文日時</label>
					<form:input path="createdAt" id="createdAt" readonly="true" />
				</div>
				<div class="form-group">
					<label for="price">価格</label>
					<form:input path="price" id="price" readonly="true" />
				</div>
			</form:form>
		</main>
    </div>
</body>

</html>
