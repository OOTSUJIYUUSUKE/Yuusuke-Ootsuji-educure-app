<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AgriConnect. 商品出品画面</title>
<link rel="stylesheet" href="css/sell.css">
</head>
<body>
	<div class="container">
		<header class="header">
			<a href="${pageContext.request.contextPath}/product_lineup"> <img
				src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
			<form class="search-form">
				<input type="text" placeholder="何をお探しですか" name="search"> <a
					href="${pageContext.request.contextPath}/search_result" class="btn">検索</a>
			</form>
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
			<h1 class="sell-product">商品の出品</h1>
			<form:form action="${pageContext.request.contextPath}/sell"
				method="post" enctype="multipart/form-data"
				modelAttribute="productForm">
				<div class="form-group">
					<label for="product-images">出品画像（最大5枚,1枚あたり最大10MBまで）</label>
					<div id="drop-area">
						<p>ここに画像をドラッグ＆ドロップするか、クリックして選択</p>
						<input type="file" id="product-images" name="imageData" accept="image/*" multiple />
						<div id="image-preview"></div>
						<c:if test="${not empty imageError}">
							<span class="error-message">${imageError}</span>
						</c:if>
						<c:if test="${not empty imageSizeError}">
							<span class="error-message">${imageSizeError}</span>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<label for="product-name">商品名</label>
					<form:input id="product-name" path="productName"/>
					<c:if test="${not empty productNameError}">
						<span class="error-message">${productNameError}</span>
					</c:if>
				</div>
				<div class="form-group">
					<label for="product-description">商品説明</label>
					<form:textarea id="product-description" path="description" rows="5"></form:textarea>
					<c:if test="${not empty descriptionError}">
						<span class="error-message">${descriptionError}</span>
					</c:if>
				</div>
				<div class="form-group">
					<label for="product-price">価格</label>
					<form:input id="product-price" path="price" type="number" min="0"/>
					<c:if test="${not empty priceError}">
						<span class="error-message">${priceError}</span>
					</c:if>
				</div>
				<div class="button-container">
					<button type="submit" class="submit-btn">出品する</button>
				</div>
			</form:form>
			<c:if test="${not empty error}">
				<span class="error-message">${error}</span>
			</c:if>
		</main>
	</div>
	<script src="js/drag-and-drop.js"></script>
	<script src="js/remove-images.js"></script>
</body>
</html>