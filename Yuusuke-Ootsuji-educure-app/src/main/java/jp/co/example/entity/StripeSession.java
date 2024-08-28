package jp.co.example.entity;

import java.util.Objects;

public class StripeSession {
    private String sessionId;
    private long amount;
    private String productName;

    public StripeSession(String sessionId, long amount, String productName) {
        this.sessionId = sessionId;
        this.amount = amount;
        this.productName = productName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

	@Override
	public int hashCode() {
		return Objects.hash(amount, productName, sessionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StripeSession other = (StripeSession) obj;
		return amount == other.amount && Objects.equals(productName, other.productName)
				&& Objects.equals(sessionId, other.sessionId);
	}   
}
