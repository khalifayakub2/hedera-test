import com.google.protobuf.InvalidProtocolBufferException;
import com.hedera.hashgraph.sdk.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class HederaSDK {

    public static Client getClient(){
        String host = "23.96.28.136";
//        String host = "20.102.77.57";
//        String host = "127.0.0.1";

        Client client = null;
//        try {
//            client = Client.forNetwork(Collections.singletonMap(host+":50211", AccountId.fromString("0.0.3")))
//    .setMirrorNetwork(List.of(host+":5600"));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        client = Client.forTestnet();

//        client.setOperator(AccountId.fromString("0.0.2"), PrivateKey.fromString("302e020100300506032b65700422042091132178e72057a1d7528025956fe39b0b847f200ab59b2fdd367017f3087137"));
        client.setOperator(AccountId.fromString("0.0.433094"), PrivateKey.fromString("302e020100300506032b65700422042011b1c21f10aafebb2fcd2ded374cfe2559ad18ad1eb4b51c124905e71c2e58d3"));
        client.setMaxBackoff(Duration.ofMinutes(5));
        client.setMinBackoff(Duration.ofSeconds(15));
        return client;
    }

    public static Client getDynamicClient(AccountId accountId, PrivateKey privateKey){
        String host = "23.96.28.136";
//        String host = "192.168.1.124";
//        String host = "127.0.0.1";
        Client client = null;
//        try {
//            client = Client.forNetwork(Collections.singletonMap(host+":50211", AccountId.fromString("0.0.3")))
//            .setMirrorNetwork(List.of(host+":5600"));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        client = Client.forTestnet();
        client.setOperator(accountId, privateKey);
        client.setMaxBackoff(Duration.ofMinutes(5));
        client.setMinBackoff(Duration.ofSeconds(15));
        return client;
    }

    public static TransactionReceipt tokenAssociation(Client client, AccountId account, TokenId tokenId, PrivateKey privateKey1, PrivateKey privateKey2) {
        TokenAssociateTransaction tkTransaction = new TokenAssociateTransaction()
                .setAccountId(account)
                .setTokenIds(Collections.singletonList(tokenId))
                .freezeWith(client)
                .sign(privateKey1);


        TransactionResponse response = null;
        try {
            byte[] tran = tkTransaction.toBytes();
            Transaction t = TokenAssociateTransaction.fromBytes(tran);
            response = (TransactionResponse) t.sign(privateKey2).execute(client);
            return response.getReceipt(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ScheduleId createScheduleTransaction(AccountId sourceAccount, AccountId destinationAccount, TokenId tokenId, Long amount, Client client, PrivateKey privateKey ){
        TransferTransaction transactionToSchedule = new TransferTransaction()
                .addTokenTransfer(tokenId, sourceAccount, -amount)
                .addTokenTransfer(tokenId, destinationAccount, amount);
        TransactionResponse transaction = null;
        try {
            transaction = new ScheduleCreateTransaction()
                    .setScheduledTransaction(transactionToSchedule)
                    .freezeWith(client)
                    .sign(privateKey)
                    .execute(client);
            TransactionReceipt receipt = transaction.getReceipt(client);
            return receipt.scheduleId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TransactionResponse signScheduledTransaction(ScheduleId scheduleId, PrivateKey privateKey, Client client) {
        //Create the transaction
        try {
            TransactionResponse transaction = new ScheduleSignTransaction()
                    .setScheduleId(scheduleId)
                    .freezeWith(client)
                    .sign(privateKey)
                    .execute(client);
            return transaction;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Status executeTransaction(Client client, ScheduleId scheduleId){

        ScheduleSignTransaction transaction = new ScheduleSignTransaction()
                .setScheduleId(scheduleId);
        TransactionResponse txResponse = null;
        TransactionReceipt receipt = null;
        try {
            txResponse = transaction.execute(client);
            receipt = txResponse.getReceipt(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return receipt.status;
    }

    public static AccountId createAccount(Client client, PrivateKey key, int amount){
        PublicKey newAccountPublicKey = key.getPublicKey();
        System.out.println("pub key:" +key.toString());
        System.out.println("priv key:" +newAccountPublicKey.toString());
        TransactionResponse response = null;
        try {
            response = new AccountCreateTransaction()
                    .setKey(key)
                    .setInitialBalance(new Hbar(amount))

                    .execute(client);

            return response.getReceipt(client).accountId;
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static TransactionResponse transfer(TokenId token, AccountId user1, AccountId user2, long amount, PrivateKey privateKey, Client client){
        try{
            TransferTransaction txn = new TransferTransaction()
                    .addTokenTransfer(token, user1, -amount)
                    .addTokenTransfer(token, user2, amount);
            TransactionResponse response = txn.freezeWith(client)
                    .sign(privateKey)
                    .execute(client);

            return response;
        } catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

    public static TokenId createToken(Client client, PrivateKey privateKey, String symbol, AccountId account) {
        TransactionResponse response = null;
        try {
            PublicKey key = privateKey.getPublicKey();

            TokenCreateTransaction transaction = new TokenCreateTransaction()
                    .setTokenName(symbol + "MMMM")
                    .setTokenSymbol(symbol)
                    .setDecimals(2)
                    .setInitialSupply(20)
                    .setTreasuryAccountId(account)
                    .setAdminKey(key)
                    .setFreezeKey(key)
                    .setWipeKey(key)
                    .setKycKey(key)
                    .setSupplyKey(key)
                    .setAdminKey(key);
            response = transaction.freezeWith(client).sign(privateKey).execute(client);
            TokenId tokenId = response.getReceipt(client).tokenId;
            return tokenId;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static TransactionReceipt kycGrant(Client client, AccountId account, TokenId tokenId, PrivateKey privateKey){
        TokenGrantKycTransaction tkgTransaction = new TokenGrantKycTransaction()
                .setAccountId(account)
                .setTokenId(tokenId);
        TransactionResponse response = null;
        try {
            response = tkgTransaction.freezeWith(client).sign(privateKey).execute(client);
            return response.getReceipt(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // get balance function
    public static AccountBalance getAccountBalance(final Client client, final AccountId accountId) {
        try {
            return  new AccountBalanceQuery()
                    .setAccountId(accountId)
                    .execute(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TransferTransaction atomicSwap(Client client, AccountId account1, AccountId account2, Long amount1, Long amount2, TokenId tokenId1, TokenId tokenId2){

        final TransferTransaction atomicSwap = new TransferTransaction()
                .addTokenTransfer(tokenId1, account1, -1 * amount2)
                .addTokenTransfer(tokenId1, account2, amount2)
                .addTokenTransfer(tokenId2, account2, -1 * amount1)
                .addTokenTransfer(tokenId2, account1, amount1)
                .freezeWith(client);

        return atomicSwap;

    }

    public static TransferTransaction signSwap(TransferTransaction transaction, PrivateKey privateKey) {
        return transaction.sign(privateKey);
    }

    public static TransactionResponse executeSwap(TransferTransaction transaction, Client client){
        try {
            return transaction.execute(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
