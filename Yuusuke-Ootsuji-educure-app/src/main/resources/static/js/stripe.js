document.getElementById("checkout-button").addEventListener("click", function(event) {
	event.preventDefault();
	
	const stripe = Stripe('pk_test_51Pp7lEAXlNOV9Dj2kKlflelIcn3MglKUqrrnVCvo2bujkQfcreMIIEPl0Fejy7YIcJxErZikgupFo3evs7O5mWOi00LfCASu1f');
	const userId = document.querySelector('input[name="userId"]').value;
	const productId = document.querySelector('input[name="productId"]').value;
	const price = document.querySelector('input[name="price"]').value;
	const paymentMethod = document.querySelector('input[name="paymentMethod"]').value;
	const shippingAddress = document.querySelector('input[name="address"]:checked').value === 'new'
    ? document.querySelector('input[name="newAddress"]').value
    : document.querySelector('.registered-address').innerText;


	fetch("/Yuusuke-Ootsuji-educure-app/create-checkout-session", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify({
			userId: userId,
			productId: productId,
			price: price,
			paymentMethod: paymentMethod,
			shippingAddress: shippingAddress
		})
	})
		.then(response => response.json())  // レスポンスをJSONとして処理
		.then(data => {
			console.log("Response data:", data);
			if (data.sessionId) {
				stripe.redirectToCheckout({ sessionId: data.sessionId });
			} else {
				alert("決済セッションの作成に失敗しました。");
			}
		})
		.catch(error => {
			console.error("Error:", error);
			alert("エラーが発生しました。");
		});
});
