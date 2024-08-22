package jp.co.example.entity;

public class StripeSession {
    private String sessionId;
    private long amount;
    private String productName;

    // コンストラクタ
    public StripeSession(String sessionId, long amount, String productName) {
        this.sessionId = sessionId;
        this.amount = amount;
        this.productName = productName;
    }

    // ゲッターとセッター
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
}
