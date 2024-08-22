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
<title>AgriConnect. 商品検索結果画面</title>
<link rel="stylesheet" href="css/search_result.css">
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
		<nav class="nav">
			<ul>
				<li><a href="${pageContext.request.contextPath}/mypage">マイページ</a></li>
				<li><a href="${pageContext.request.contextPath}/sell">出品</a></li>
				<li><a href="${pageContext.request.contextPath}/contact">お問い合わせ</a></li>
				<li><a href="${pageContext.request.contextPath}/logout">ログアウト</a></li>
			</ul>
		</nav>
		<main>
			<h1 class="title">
				<c:choose>
					<c:when test="${fn:length(products) == 0}">
                        お探しの商品は見つかりませんでした。
                    </c:when>
					<c:otherwise>
                        ${fn:length(products)}件の検索結果が見つかりました。
                    </c:otherwise>
				</c:choose>
			</h1>
			<div class="product-list" id="product-list">
				<c:forEach items="${products}" var="product">
					<a href="${pageContext.request.contextPath}/product_detail?id=${product.productId}" class="product-link">
						<div class="product-item">
							<div class="product-image">
								<c:if test="${not empty product.imageUrls}">
									<img src="${product.imageUrls[0]}" alt="${product.productName}">
								</c:if>
								<span class="price">¥${product.price}</span>
							</div>
							<div class="product-name">${product.productName}</div>
						</div>
					</a>
				</c:forEach>
			</div>
		</main>
	</div>
	<script src="js/scripts.js"></script>
</body>

</html>