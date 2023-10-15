import com.hedera.hashgraph.sdk.*;

public class TestApproach {

    private Client client;
    private Client regulatorClient1;
    private Client regulatorClient2;
    private Client crossborderClient;
    private AccountId endUserWallet1;
    private AccountId crossborderAccount;
    private AccountId endUserWallet2;
    private AccountId regulator1;
    private AccountId regulator2;
    private AccountId fsp1;
    private AccountId fsp2;
    private TokenId token1;
    private TokenId token2;
    PrivateKey privateKey1;
    PrivateKey privateKey2;
    PrivateKey privateKey3;
    PrivateKey privateKey4;
    PrivateKey privateKey5;
    PrivateKey privateKey6;
    PrivateKey crossborderPrivateKey;
    ScheduleId transaction1;
    ScheduleId transaction2;
    ScheduleId transaction3;



    public void run(){
        this.initializeWallets();
        this.initializeTokens();
        this.initializeTokenAssociation();
        this.giveSomeMoneyToFSP();
        this.doTransfer();
        try {
            System.out.println("CURRENTLY SLEEEPINNGGGG TO SIMULATE A PAUSEEEEEE");
            Thread.sleep(10000);
            System.out.println("DONE SLEEPING. GO SIGN GO");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.signTransactions();
    }

    private static String printBalance(Client client, AccountId accountId, TokenId tokenId){
        AccountBalance balance = HederaSDK.getAccountBalance(client, accountId);
        return String.format("The balance for account: %s with tokenId: %s is %s", accountId.toString(), tokenId.toString(),balance.tokens.get(tokenId));
    }


    private PrivateKey generateKey(){
        PrivateKey privateKey = PrivateKey.generateED25519();
        // save to file and retrieve also
        return privateKey;
    }

    private AccountId createAccount(Client client, PrivateKey privateKey, int hbar, String accountFor) {

        AccountId account = HederaSDK.createAccount(client, privateKey, hbar);
        System.out.println(HederaSDK.getAccountBalance(this.client, this.regulator1));
        System.out.println("Created " + accountFor + "wallet with id: " + this.regulator1.toString());
        // save to file and retrieve also
        return account;
    }

    private void initializeWallets() {

        this.privateKey1 = generateKey();
        this.privateKey2 = generateKey();
        this.privateKey3 = generateKey();
        this.privateKey4 = generateKey();
        this.privateKey5 = generateKey();
        this.privateKey6 = generateKey();
        this.crossborderPrivateKey = generateKey();

        System.out.println("####################################################################################");
        this.client = HederaSDK.getClient(); // default hedera client
        this.crossborderAccount = this.createAccount(this.client, this.crossborderPrivateKey, 10, "CrossBorder");
        this.crossborderClient = HederaSDK.getDynamicClient(this.crossborderAccount, this.crossborderPrivateKey);
        this.regulator1 = this.createAccount(this.client, privateKey1, 10, "Regulator A");
        this.regulator2 =  this.createAccount(this.client, privateKey2, 10, "Regulator B");
        this.regulatorClient1 = HederaSDK.getDynamicClient(this.regulator1, this.privateKey1);
        this.regulatorClient2 = HederaSDK.getDynamicClient(this.regulator2, this.privateKey2);
        this.fsp1 = this.createAccount(this.regulatorClient1, privateKey3, 1, "FSP A");
        this.fsp2 = this.createAccount(this.regulatorClient2, privateKey4, 1, "FSP B");
        this.endUserWallet1 = this.createAccount(this.regulatorClient1, privateKey5, 1, "End User A");
        this.endUserWallet2 = this.createAccount(this.regulatorClient2, privateKey6, 1, "End User B");
        System.out.println("####################################################################################");

    }
    private TokenId tokenCreation(Client client, PrivateKey privateKey, String symbol, AccountId account){
        TokenId token = HederaSDK.createToken(client, privateKey, symbol, account);
        System.out.println("Created new Token with ID: " + this.token1.toString());
        // save to file and restore tokens
        return token;
    }

    private void initializeTokens() {
        System.out.println("####################################################################################");
        this.token1 = tokenCreation(regulatorClient1, privateKey1, "CEDIS", regulator1);
        this.token2 = tokenCreation(regulatorClient2, privateKey2, "NAIRA", regulator2);
        System.out.println("####################################################################################");
    }

    private void initializeTokenAssociation(){

        HederaSDK.tokenAssociation(regulatorClient1, this.fsp1, this.token1, privateKey1, privateKey3);
        HederaSDK.tokenAssociation(regulatorClient1, this.endUserWallet1, this.token1, privateKey1, privateKey5);
        HederaSDK.kycGrant(regulatorClient1, this.fsp1, this.token1, this.privateKey1);
        HederaSDK.kycGrant(regulatorClient1, this.endUserWallet1, this.token1, this.privateKey1);

        HederaSDK.tokenAssociation(regulatorClient2, this.fsp1, this.token2, privateKey2, privateKey3);
        HederaSDK.tokenAssociation(regulatorClient2, this.fsp2, this.token2, privateKey2, privateKey4);
        HederaSDK.tokenAssociation(regulatorClient2, this.endUserWallet2, this.token2, privateKey2, privateKey6);
        HederaSDK.kycGrant(regulatorClient2, this.fsp1, this.token2, this.privateKey2);
        HederaSDK.kycGrant(regulatorClient2, this.fsp2, this.token2, this.privateKey2);
        HederaSDK.kycGrant(regulatorClient2, this.endUserWallet2, this.token2, this.privateKey2);

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
            System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
            System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
            System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
            System.out.println("################ Balance BEfore Transactions ##################");
            System.out.println("####################################################################################");
            this.transfer(this.token1, this.regulator1, this.fsp1,txnAmt, this.privateKey1, this.regulatorClient1);
            this.transfer(this.token2, this.regulator2, this.fsp2,txnAmt, this.privateKey2, this.regulatorClient2);
            System.out.println("Transferred " + txnAmt + " from regulator A: " + this.regulator1.toString() + " to FSP A: " + this.fsp1.toString() + " on token: " + this.token1.toString());
            System.out.println("Transferred " + txnAmt + " from regulator B: " + this.regulator2.toString() + " to FSP B: " + this.fsp2.toString() + " on token: " + this.token1.toString());
            this.transfer(this.token2, this.fsp2, this.endUserWallet2, txnAmt2, this.privateKey4, this.regulatorClient2);
            System.out.println("Transferred " + txnAmt2 + " from FSP A: " + this.fsp1.toString() + " to END USER A: " + this.endUserWallet1.toString() + " on token: " + this.token1.toString());
            System.out.println("################ Balance BEfore Transactions ##################");
            System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
            System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
            System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
            System.out.println("################ Balance BEfore Transactions ##################");
            System.out.println("####################################################################################");

        } catch (Exception e){
            System.out.println(e);
        }
    }

    private void doTransfer(){
        // exhange rate to user
        // exchange rate to fsp B
        final Double exchangeRateToUser = 6.5;
        final Double exchangeRateToFsp = 5.3;
        // amount user B want to transfer
        final Long amount = 100L; // cedis to send
        final Long amountForUser = (long) (amount * exchangeRateToUser); // cedis to send
        final Long amountForFSP = (long) (amount * exchangeRateToFsp); // cedis to send
        // create scheduled transfer from End User B to FSP B
        this.transaction1 = HederaSDK.createScheduleTransaction(this.endUserWallet2, this.fsp2, this.token2, amountForUser, this.crossborderClient, this.crossborderPrivateKey);
        System.out.println(this.transaction1.toString());
        // create scheduled transfer from FSP B to FSP A
        this.transaction2 = HederaSDK.createScheduleTransaction(this.fsp2, this.fsp1, this.token2, amountForFSP, this.crossborderClient, this.crossborderPrivateKey);
        System.out.println(this.transaction2.toString());
        // create scheduled transfer from FSP A to End User A
        this.transaction3 = HederaSDK.createScheduleTransaction(this.fsp1, this.endUserWallet1, this.token1, amount, this.crossborderClient, this.crossborderPrivateKey);
        System.out.println(this.transaction3.toString());
    }

    private void signTransactions(){
        System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
        System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
        System.out.println("BALANCE FOR End User A:" + printBalance(this.regulatorClient1, this.endUserWallet1, this.token1));
        System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
        System.out.println("BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token2));
        HederaSDK.signScheduledTransaction(this.transaction1, this.privateKey6, this.regulatorClient2);
        HederaSDK.signScheduledTransaction(this.transaction2, this.privateKey4, this.regulatorClient2);
        HederaSDK.signScheduledTransaction(this.transaction3, this.privateKey3, this.regulatorClient1);
        System.out.println("####################################################################################");
        System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
        System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
        System.out.println("BALANCE FOR End User A:" + printBalance(this.regulatorClient1, this.endUserWallet1, this.token1));
        System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
        System.out.println("BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token2));
//        HederaSDK.executeTransaction(this.crossborderClient, this.transaction1);
//        HederaSDK.executeTransaction(this.crossborderClient, this.transaction2);
//        HederaSDK.executeTransaction(this.crossborderClient, this.transaction3);
    }

}
