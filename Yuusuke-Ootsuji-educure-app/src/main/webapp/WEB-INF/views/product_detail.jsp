<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AgriConnect. 商品詳細画面</title>
<link rel="stylesheet" href="css/product_detail.css">
</head>
<body>
	<div class="container">
		<header class="header">
			<a href="${pageContext.request.contextPath}/product_lineup"> <img
				src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
			<form:form action="${pageContext.request.contextPath}/search_result"
				method="get" modelAttribute="searchForm" class="search-form">
				<form:input type="text" path="search" placeholder="何をお探しですか"
					class="search-input" />
				<button type="submit" class="btn">検索</button>
			</form:form>
			<div class="welcome-message">ようこそ、${loginUserName}さん</div>
		</header>
		<a href="${pageContext.request.contextPath}/product_lineup"
			class="back-link">&lt; 戻る</a>
		<nav class="nav">
			<ul>
				<li><a href="${pageContext.request.contextPath}/mypage">マイページ</a></li>
				<li><a href="${pageContext.request.contextPath}/sell">出品</a></li>
				<li><a href="${pageContext.request.contextPath}/contact">お問い合わせ</a></li>
				<li><a href="${pageContext.request.contextPath}/logout">ログアウト</a></li>
			</ul>
		</nav>
		<main>
			<div class="product-detail-container">
				<div class="product-detail-left">
					<div class="thumbnail-list">
						<c:forEach items="${product.imageUrls}" var="imageUrl">
							<img src="${imageUrl}" alt="${product.productName}"
								class="thumbnail" onclick="showImage(this)">
						</c:forEach>
					</div>
					<div class="main-image-container">
						<img src="${product.imageUrls[0]}" alt="${product.productName}"
							class="main-image" id="main-image">
					</div>
				</div>
				<div class="product-detail-right">
					<div class="product-name">${product.productName}</div>
					<div class="price">¥${product.price}</div>
					<c:choose>
						<c:when test="${product.soldOut}">
							<p class="sold-out-message">SOLD OUT</p>
						</c:when>
						<c:otherwise>
							<form
								action="${pageContext.request.contextPath}/product_purchase"
								method="get">
								<input type="hidden" name="product_id"
									value="${product.productId}" />
								<button class="purchase-button">購入する</button>
							</form>
						</c:otherwise>
					</c:choose>
					<div class="description">${product.description}</div>
					<div class="seller-name">出品者: ${product.user.userName}</div>
				</div>
			</div>
		</main>
	</div>
	<script src="js/image-handler.js"></script>
	<script src="js/scripts.js"></script>
</body>
</html>