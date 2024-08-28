package jp.co.example.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import jp.co.example.entity.Order;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

	@Value("${stripe.secret.key}")
	private String stripeSecretKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeSecretKey;
	}

	public String createCheckoutSession(Order order, String productName) throws StripeException {
		long totalAmount = order.getPrice().add(BigDecimal.valueOf(300)).longValue(); // 配送料も含めた合計額
		try {
			SessionCreateParams params = SessionCreateParams.builder()
					.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl(
							"http://localhost:8080/Yuusuke-Ootsuji-educure-app/product_purchase_success?session_id={CHECKOUT_SESSION_ID}")
					.setCancelUrl(
							"http://localhost:8080/Yuusuke-Ootsuji-educure-app/product_purchase_error?session_id={CHECKOUT_SESSION_ID}")
					.addLineItem(
							SessionCreateParams.LineItem.builder()
									.setPriceData(
											SessionCreateParams.LineItem.PriceData.builder()
													.setCurrency("jpy")
													.setUnitAmount(totalAmount)
													.setProductData(
															SessionCreateParams.LineItem.PriceData.ProductData.builder()
																	.setName(productName)
																	.build())
													.build())
									.setQuantity(1L)
									.build())
					.build();
			Session session = Session.create(params);
			return session.getId();
		} catch (StripeException e) {
			throw e;
		}
	}
}
