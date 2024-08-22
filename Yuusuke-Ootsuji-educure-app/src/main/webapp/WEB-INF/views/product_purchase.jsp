<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AgriConnect. 商品購入画面</title>
<link rel="stylesheet" href="css/product_purchase.css">
</head>
<body>
	<div class="container">
		<header class="header">
			<a href="${pageContext.request.contextPath}/product_lineup">
			    <img src="images/logo.png" alt="AgriConnect. ロゴ" class="logo">
			</a>
			<form class="search-form">
				<input type="text" placeholder="何をお探しですか" name="search"> <a href="${pageContext.request.contextPath}/search_result" class="btn">検索</a>
			</form>
			<div class="welcome-message">ようこそ、${loginUserName}さん</div>
		</header>
		<a href="${pageContext.request.contextPath}/product_detail?product_id=${product.productId}" class="back-link">&lt; 戻る</a>
		<nav class="nav">
			<ul>
				<li><a href="${pageContext.request.contextPath}/mypage">マイページ</a></li>
				<li><a href="${pageContext.request.contextPath}/sell">出品</a></li>
				<li><a href="${pageContext.request.contextPath}/contact">お問い合わせ</a></li>
				<li><a href="${pageContext.request.contextPath}/logout">ログアウト</a></li>
			</ul>
		</nav>
		<main class="product-purchase">
			<form action="/create-checkout-session" method="post">
                <div class="left-side">
                    <div class="product-detail">
                        <div class="product-main-image">
                            <img src="${product.imageUrls[0]}" alt="${product.productName}" id="mainImage">
                        </div>
                        <div class="product-info">
                            <h1 id="product-name" class="product-name">${product.productName}</h1>
                            <p class="price">¥${product.price}</p>
                        </div>
                    </div>
                    <div class="user-address">
                        <div class="address-selection">
                            <p>配送先住所</p>
                            <label>
                                <input type="radio" name="address" value="registered" checked onclick="toggleAddress()">
                                登録住所を使用
                            </label>
                            <label>
                                <input type="radio" name="address" value="new" onclick="toggleAddress()"> 新規住所を入力
                            </label>
                        </div>
                        <div id="registered-address" class="address-container" style="display: block;">
                            <p class="registered-address">${registeredAddress}</p>
                        </div>
                        <div id="new-address" class="address-container" style="display: none;">
                            <input type="text" name="newAddress" placeholder="配送先住所を入力して下さい">
                        </div>
                    </div>
                </div>
                <div class="right-side">
                    <div class="payment-info">
                        <p>
                            商品代金: <span id="price" class="price">¥${product.price}</span>
                        </p>
                        <p>
                            決済手数料: <span id="payment-fee">¥300</span>
                        </p>
                        <p>
                            支払い金額: <span id="total-amount" class="total-amount">¥${product.price + 300}</span>
                        </p>
                        <input type="hidden" name="userId" value="${loginUserId}" />
                        <input type="hidden" name="productId" value="${product.productId}" />
                        <input type="hidden" name="price" value="${product.price}" />
                        <input type="hidden" name="paymentMethod" value="stripe" />
                        <button id="checkout-button" class="purchase-button" type="button">決済処理に進む</button>
                        <div class="caution">
                            ※決済処理に進むボタンをクリックすると、<br>外部決済サービスに遷移します。
                        </div>
                    </div>
                </div>
            </form>
		</main>
	</div>
	<script src="js/toggle-address.js"></script>
	<script src="js/scripts.js"></script>
	<script src="https://js.stripe.com/v3/"></script>
	<script src="js/stripe.js"></script>
</body>
</html>