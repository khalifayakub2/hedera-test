import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;

public class Order {

    private int id;
    private AccountId accountId;
    private String orderType;
    private String symbol;
    private long price;
    private long quantity;
    private TokenId tokenId1;
    private PrivateKey privateKey;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public TokenId getTokenId1() {
        return tokenId1;
    }

    public void setTokenId1(TokenId tokenId1) {
        this.tokenId1 = tokenId1;
    }

    public TokenId getTokenId2() {
        return tokenId2;
    }

    public void setTokenId2(TokenId tokenId2) {
        this.tokenId2 = tokenId2;
    }

    private TokenId tokenId2;
    private long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Order(int id, AccountId accountId, String orderType, String symbol, long price, long quantity, TokenId tokenId1, PrivateKey privateKey, TokenId tokenId2, long timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.orderType = orderType;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.tokenId1 = tokenId1;
        this.privateKey = privateKey;
        this.tokenId2 = tokenId2;
        this.timestamp = timestamp;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

//    public Order(int id, AccountId accountId, String orderType, String symbol, long price, long quantity, long timestamp) {
//        this.id = id;
//        this.accountId = accountId;
//        this.orderType = orderType;
//        this.symbol = symbol;
//        this.price = price;
//        this.quantity = quantity;
//        this.timestamp = timestamp;
//    }


}
