import com.hedera.hashgraph.sdk.*;

public class TestApproach2 {

    private Client client;
    private Client regulatorClient1;
    private Client regulatorClient2;
    private Client regulatorClient3;
    private Client crossborderClient;
    private AccountId endUserWallet1;
    private AccountId crossborderAccount;
    private AccountId endUserWallet2;
    private AccountId regulator1;
    private AccountId regulator2;
    private AccountId regulator3;
    private AccountId fsp1;
    private AccountId fsp2;
    private TokenId token1;
    private TokenId token2;
    private TokenId token3;
    PrivateKey privateKey1;
    PrivateKey privateKey2;
    PrivateKey privateKey3;
    PrivateKey privateKey4;
    PrivateKey privateKey5;
    PrivateKey privateKey6;
    PrivateKey regulator3PrivateKey;
    PrivateKey crossborderPrivateKey;
    ScheduleId transaction1;
    ScheduleId transaction2;
    ScheduleId transaction3;



    public void run(){
        initializeWallets();
        initializeTokens();
        initializeTokenAssociation();
        giveSomeMoneyToFSP();
        doTransfer();
        signTransactions();
    }

    private static String printBalance(Client client, AccountId accountId, TokenId tokenId){
        AccountBalance balance = HederaSDK.getAccountBalance(client, accountId);
        return String.format("The balance for account: %s with tokenId: %s is %s", accountId.toString(), tokenId.toString(),balance.tokens.get(tokenId));
    }

    private void initializeWallets() {

        privateKey1 = PrivateKey.generateED25519();
        privateKey2 = PrivateKey.generateED25519();
        privateKey3 = PrivateKey.generateED25519();
        privateKey4 = PrivateKey.generateED25519();
        privateKey5 = PrivateKey.generateED25519();
        privateKey6 = PrivateKey.generateED25519();
        crossborderPrivateKey = PrivateKey.generateED25519();
        regulator3PrivateKey = PrivateKey.generateED25519();

        client = HederaSDK.getClient(); // default hedera client
        crossborderAccount = HederaSDK.createAccount(client, crossborderPrivateKey);
        System.out.println(crossborderAccount.toString());
        System.out.println(crossborderPrivateKey);
        crossborderClient = HederaSDK.getDynamicClient(crossborderAccount, crossborderPrivateKey);
        System.out.println("####################################################################################");
        regulator1 = HederaSDK.createAccount(client, privateKey1);
        System.out.println(HederaSDK.getAccountBalance(client, regulator1));
        System.out.println("Created Regulator A wallet with id: " + regulator1.toString());
        regulator2 =  HederaSDK.createAccount(client, privateKey2);
        System.out.println("Created Regulator B wallet with id: " + regulator2.toString());
        regulatorClient1 = HederaSDK.getDynamicClient(regulator1, privateKey1);
        regulatorClient2 = HederaSDK.getDynamicClient(regulator2, privateKey2);
        fsp1 = HederaSDK.createAccountUser(regulatorClient1, privateKey3);
        System.out.println("Created FSP A wallet with id: " + fsp1.toString());
        fsp2 =  HederaSDK.createAccountUser(regulatorClient2, privateKey4);
        System.out.println("Created FSP B wallet with id: " + fsp2.toString());
        endUserWallet1 = HederaSDK.createAccountUser(regulatorClient1, privateKey5);
        System.out.println("Created End User A wallet with id: " + endUserWallet1.toString());
        endUserWallet2 = HederaSDK.createAccountUser(regulatorClient2, privateKey6);
        regulator3 = HederaSDK.createAccount(client, regulator3PrivateKey);
        regulatorClient3 = HederaSDK.getDynamicClient(regulator3, regulator3PrivateKey);
        System.out.println("Created End User B wallet with id: " + endUserWallet2.toString());
        System.out.println("####################################################################################");
    }

    private void initializeTokens() {
        System.out.println("####################################################################################");
        token1 = HederaSDK.createToken(regulatorClient1, privateKey1, "CEDIS", regulator1);
        System.out.println("Created new Token with ID: " + token1.toString());
        token2 = HederaSDK.createToken(regulatorClient2, privateKey2, "NAIRA", regulator2);
        System.out.println("Created new Token 2 with ID: " + token2.toString());
        token3 = HederaSDK.createToken(regulatorClient3, regulator3PrivateKey, "USD", regulator3);
        System.out.println("####################################################################################");
    }

    private void initializeTokenAssociation(){

        HederaSDK.tokenAssociation(regulatorClient1, fsp1, token1, privateKey1, privateKey3);
        HederaSDK.tokenAssociation(regulatorClient1, endUserWallet1, token1, privateKey1, privateKey5);
        HederaSDK.kycGrant(regulatorClient1, fsp1, token1, privateKey1);
        HederaSDK.kycGrant(regulatorClient1, endUserWallet1, token1, privateKey1);

        HederaSDK.tokenAssociation(regulatorClient2, fsp1, token2, privateKey2, privateKey3);
        HederaSDK.tokenAssociation(regulatorClient2, fsp2, token2, privateKey2, privateKey4);
        HederaSDK.tokenAssociation(regulatorClient2, endUserWallet2, token2, privateKey2, privateKey6);
        HederaSDK.kycGrant(regulatorClient2, fsp1, token2, privateKey2);
        HederaSDK.kycGrant(regulatorClient2, fsp2, token2, privateKey2);
        HederaSDK.kycGrant(regulatorClient2, endUserWallet2, token2, privateKey2);

        HederaSDK.tokenAssociation(regulatorClient3, fsp1, token3, regulator3PrivateKey, privateKey3);
        HederaSDK.tokenAssociation(regulatorClient3, fsp2, token3, regulator3PrivateKey, privateKey4);
        HederaSDK.kycGrant(regulatorClient3, fsp1, token3, regulator3PrivateKey);
        HederaSDK.kycGrant(regulatorClient3, fsp2, token3, regulator3PrivateKey);

    }

    private void transfer(TokenId token, AccountId user1, AccountId user2, long amount, PrivateKey privateKey, Client client){
        try{
            TransferTransaction txn = new TransferTransaction()
                    .addTokenTransfer(token, user1, -amount)
                    .addTokenTransfer(token, user2, amount);
            txn.freezeWith(client)
                    .sign(privateKey)
                    .execute(client);

        } catch (Exception e){
            System.out.println(e);
        }
    }

    private void giveSomeMoneyToFSP(){
        try {
            long txnAmt = 1000000;
            long txnAmt2 = 10000;
            System.out.println("################ Balance BEfore Transactions ##################");
            System.out.println( "BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token1));
            System.out.println("BALANCE FOR FSP B:" + printBalance(regulatorClient2, fsp2, token2));
            System.out.println("BALANCE FOR End User B:" + printBalance(regulatorClient2, endUserWallet2, token2));
            System.out.println("################ Balance BEfore Transactions ##################");
            System.out.println("####################################################################################");
            transfer(token1, regulator1, fsp1,txnAmt, privateKey1, regulatorClient1);
            transfer(token2, regulator2, fsp2,txnAmt, privateKey2, regulatorClient2);
            transfer(token3, regulator3, fsp2,txnAmt, regulator3PrivateKey, regulatorClient3);
            System.out.println("Transferred " + txnAmt + " from regulator A: " + regulator1.toString() + " to FSP A: " + fsp1.toString() + " on token: " + token1.toString());
            System.out.println("Transferred " + txnAmt + " from regulator B: " + regulator2.toString() + " to FSP B: " + fsp2.toString() + " on token: " + token1.toString());
            transfer(token2, fsp2, endUserWallet2, txnAmt2, privateKey4, regulatorClient2);
            System.out.println("Transferred " + txnAmt2 + " from FSP A: " + fsp1.toString() + " to END USER A: " + endUserWallet1.toString() + " on token: " + token1.toString());
            System.out.println("################ Balance BEfore Transactions ##################");
            System.out.println( "BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token1));
            System.out.println("BALANCE FOR FSP B:" + printBalance(regulatorClient2, fsp2, token2));
            System.out.println("BALANCE FOR End User B:" + printBalance(regulatorClient2, endUserWallet2, token2));
            System.out.println("################ Balance BEfore Transactions ##################");
            System.out.println("####################################################################################");

        } catch (Exception e){
            System.out.println(e);
        }
    }

    private void doTransfer(){
        // scenario, fsp B has dollar already in wallet
        // exhange rate to user
        // exchange rate to fsp B
        // another scenario is stable Naira to Usd rate and stable cedis to usd rate
        final Double exchangeRateToFsp = 15.0;
        final Double exchangeRateToUser = 20.5;
        // amount user B want to transfer
        final Long amount = 100L; // cedis to send
        final Long amountForUser = (long) (amount * exchangeRateToUser); // cedis to send
        final Long amountForFSP = (long) (amount * exchangeRateToFsp); // cedis to send
        // create scheduled transfer from End User B to FSP B
        transaction1 = HederaSDK.createScheduleTransaction(endUserWallet2, fsp2, token2, amountForUser, crossborderClient, crossborderPrivateKey);
        System.out.println(transaction1.toString());
        // create scheduled transfer from FSP B to FSP A
        transaction2 = HederaSDK.createScheduleTransaction(fsp2, fsp1, token3, amountForFSP, crossborderClient, crossborderPrivateKey);
        System.out.println(transaction2.toString());
        // create scheduled transfer from FSP A to End User A
        transaction3 = HederaSDK.createScheduleTransaction(fsp1, endUserWallet1, token1, amount, crossborderClient, crossborderPrivateKey);
        System.out.println(transaction3.toString());
    }

    private void signTransactions(){
        System.out.println( "BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token1));
        System.out.println("BALANCE FOR FSP B:" + printBalance(regulatorClient2, fsp2, token2));
        System.out.println( "BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token3));
        System.out.println("BALANCE FOR FSP B:" + printBalance(regulatorClient2, fsp2, token3));
        System.out.println("BALANCE FOR End User A:" + printBalance(regulatorClient1, endUserWallet1, token1));
        System.out.println("BALANCE FOR End User B:" + printBalance(regulatorClient2, endUserWallet2, token2));
        System.out.println("BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token2));
        HederaSDK.signScheduledTransaction(transaction1, privateKey6, regulatorClient2);
        HederaSDK.signScheduledTransaction(transaction2, privateKey4, regulatorClient2);
        HederaSDK.signScheduledTransaction(transaction3, privateKey3, regulatorClient1);
        System.out.println("####################################################################################");
        System.out.println( "BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token1));
        System.out.println("BALANCE FOR FSP B:" + printBalance(regulatorClient2, fsp2, token2));
        System.out.println("BALANCE FOR End User A:" + printBalance(regulatorClient1, endUserWallet1, token1));
        System.out.println("BALANCE FOR End User B:" + printBalance(regulatorClient2, endUserWallet2, token2));
        System.out.println("BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token2));
        System.out.println( "BALANCE FOR FSP A:" + printBalance(regulatorClient1, fsp1, token3));
        System.out.println("BALANCE FOR FSP B:" + printBalance(regulatorClient2, fsp2, token3));
    }

}
