import com.hedera.hashgraph.sdk.TokenId;

public class CurrencyPair {

    private int id;
    private TokenId baseToken;
    private TokenId quoteToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TokenId getBaseToken() {
        return baseToken;
    }

    public void setBaseToken(TokenId baseToken) {
        this.baseToken = baseToken;
    }

    public TokenId getQuoteToken() {
        return quoteToken;
    }

    public void setQuoteToken(TokenId quoteToken) {
        this.quoteToken = quoteToken;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String symbol;

    public CurrencyPair(int id, TokenId baseToken, TokenId quoteToken, String symbol) {
        this.id = id;
        this.baseToken = baseToken;
        this.quoteToken = quoteToken;
        this.symbol = symbol;
    }

}
