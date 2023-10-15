//import com.hedera.hashgraph.sdk.*;
//import helper.FileOperations;
//
//public class ApproachOne {
//
//    private Client client;
//    private Client regulatorClient1;
//    private Client regulatorClient2;
//    private AccountId endUserWallet1;
//    private AccountId endUserWallet2;
//    private AccountId regulator1;
//    private AccountId regulator2;
//    private AccountId fsp1;
//    private AccountId fsp2;
//    private TokenId token1;
//    private TokenId token2;
//    PrivateKey privateKey1;
//    PrivateKey privateKey2;
//    PrivateKey privateKey3;
//    PrivateKey privateKey4;
//    PrivateKey privateKey5;
//    PrivateKey privateKey6;
//    public StringBuilder output = new StringBuilder();
//    public StringBuilder data = new StringBuilder();
//
//    private OrderMatchingService orderMatchingService = new OrderMatchingService();
//    private CurrencyPair currencyPair;
//    private FileOperations fileOperations = new FileOperations();
//
//    public static String printBalance(Client client, AccountId accountId, TokenId tokenId){
//
//        AccountBalance balance = HederaSDK.getAccountBalance(client, accountId);
//        return String.format("The balance for account: %s with tokenId: %s is %s", accountId.toString(), tokenId.toString(),balance.tokens.get(tokenId));
//    }
//
//
//    public void initializeWallets() {
//
//        String content = fileOperations.readFile("hello.txt");
//        String[] contentSplit = content.split("\n");
//        this.client = HederaSDK.getClient();
//        this.output.append("####################################################################################").append("\n");
//        this.regulator1 = !contentSplit[0].isEmpty() ? AccountId.fromString(contentSplit[0]) : HederaSDK.createAccount(this.client, privateKey1);
//        this.output.append("Created Regulator A wallet with id: " + this.regulator1.toString()).append("\n");
//        this.regulator2 = !contentSplit[1].isEmpty() ? AccountId.fromString(contentSplit[1]) : HederaSDK.createAccount(this.client, privateKey2);
//        this.output.append("Created Regulator B wallet with id: " + this.regulator2.toString()).append("\n");
//        this.fsp1 =!contentSplit[2].isEmpty() ? AccountId.fromString(contentSplit[2]) :  HederaSDK.createAccount(this.client, privateKey3);
//        this.output.append("Created FSP A wallet with id: " + this.fsp1.toString()).append("\n");
//        this.fsp2 = !contentSplit[3].isEmpty() ? AccountId.fromString(contentSplit[3]) : HederaSDK.createAccount(this.client, privateKey4);
//        this.output.append("Created FSP B wallet with id: " + this.fsp2.toString()).append("\n");
//        this.endUserWallet1 = !contentSplit[4].isEmpty() ? AccountId.fromString(contentSplit[4]) : HederaSDK.createAccount(this.client, privateKey5);
//        this.output.append("Created End User A wallet with id: " + this.endUserWallet1.toString()).append("\n");
//        this.endUserWallet2 = !contentSplit[5].isEmpty() ? AccountId.fromString(contentSplit[5]) : HederaSDK.createAccount(this.client, privateKey6);
//        this.output.append("Created End User B wallet with id: " + this.endUserWallet2.toString()).append("\n");
//        this.output.append("####################################################################################").append("\n");
//
//        this.privateKey1 = !contentSplit[6].isEmpty() ? PrivateKey.fromString(contentSplit[6]) : PrivateKey.generateED25519();
//        this.privateKey2 = !contentSplit[7].isEmpty() ? PrivateKey.fromString(contentSplit[7]) : PrivateKey.generateED25519();
//        this.privateKey3 = !contentSplit[8].isEmpty() ? PrivateKey.fromString(contentSplit[8]) : PrivateKey.generateED25519();
//        this.privateKey4 = !contentSplit[9].isEmpty() ? PrivateKey.fromString(contentSplit[9]) : PrivateKey.generateED25519();
//        this.privateKey5 = !contentSplit[10].isEmpty() ? PrivateKey.fromString(contentSplit[10]) : PrivateKey.generateED25519();
//        this.privateKey6 = !contentSplit[11].isEmpty() ? PrivateKey.fromString(contentSplit[11]) : PrivateKey.generateED25519();
//
//
//        this.regulatorClient1 = HederaSDK.getDynamicClient(this.regulator1, this.privateKey1);
//        this.regulatorClient2 = HederaSDK.getDynamicClient(this.regulator2, this.privateKey2);
//
////        fileOperations.writeFile("hello.txt", data.toString());
//
//    }
//
//    public void initializeTokens() {
//
//        this.output.append("####################################################################################").append("\n");
//        this.token1 = HederaSDK.createToken(this.client, privateKey1, "ENAIRA", this.regulator1);
//        this.output.append("Created new Token with ID: " + this.token1.toString()).append("\n");
//        this.token2 = HederaSDK.createToken(this.client, privateKey2, "ECEDIS", this.regulator2);
//        this.output.append("Created new Token 2 with ID: " + this.token2.toString()).append("\n");
//        this.currencyPair = new CurrencyPair(1, this.token1, this.token2, "ENAIRA/ECEDIS");
//        this.output.append("Created currency pair " + this.currencyPair.getSymbol()).append("\n");
//        this.output.append("####################################################################################").append("\n");
//
//    }
//
//    public void initializeTokenAssociation(){
//
//        HederaSDK.tokenAssociation(regulatorClient1, this.fsp1, this.token1, privateKey1, privateKey3);
//        HederaSDK.tokenAssociation(regulatorClient1, this.fsp2, this.token1, privateKey1, privateKey4);
//        HederaSDK.tokenAssociation(regulatorClient1, this.endUserWallet1, this.token1, privateKey1, privateKey5);
//        HederaSDK.kycGrant(regulatorClient1, this.fsp1, this.token1, this.privateKey1);
//        HederaSDK.kycGrant(regulatorClient1, this.fsp2, this.token1, this.privateKey1);
//        HederaSDK.kycGrant(regulatorClient1, this.endUserWallet1, this.token1, this.privateKey1);
//
//        HederaSDK.tokenAssociation(regulatorClient2, this.fsp1, this.token2, privateKey2, privateKey3);
//        HederaSDK.tokenAssociation(regulatorClient2, this.fsp2, this.token2, privateKey2, privateKey4);
//        HederaSDK.tokenAssociation(regulatorClient2, this.endUserWallet2, this.token2, privateKey2, privateKey6);
//        HederaSDK.kycGrant(regulatorClient2, this.fsp1, this.token2, this.privateKey2);
//        HederaSDK.kycGrant(regulatorClient2, this.fsp2, this.token2, this.privateKey2);
//        HederaSDK.kycGrant(regulatorClient2, this.endUserWallet2, this.token2, this.privateKey2);
//
//    }
//
//    public void giveSomeMoneyToFSP(){
//        try {
//            long txnAmt = 1000000;
//            long txnAmt2 = 10000;
//            this.output.append("################ Balance BEfore Transactions ##################").append("\n");
//            this.output.append( "BALANCE FOR FSP A" + printBalance(client, this.fsp1, this.token1)).append("\n");
//            this.output.append("BALANCE FOR FSP B" + printBalance(client, this.fsp2, this.token2)).append("\n");
//            this.output.append("BALANCE FOR End User A" + printBalance(client, this.endUserWallet1, this.token1)).append("\n");
//            this.output.append("################ Balance BEfore Transactions ##################").append("\n");
//            this.output.append("####################################################################################").append("\n");
//            TransferTransaction txn1 = new TransferTransaction()
//                    .addTokenTransfer(this.token1, this.regulator1, -txnAmt)
//                    .addTokenTransfer(this.token1, this.fsp1, txnAmt);
//
//            txn1.freezeWith(regulatorClient1)
//                    .sign(this.privateKey1)
//                    .execute(client);
//
//            this.output.append("Transferred " + txnAmt + " from regulator A: " + this.regulator1.toString() + " to FSP A: " + this.fsp1.toString() + " on token: " + this.token1.toString()).append("\n");
//
//
//            TransferTransaction txn2 = new TransferTransaction()
//                    .addTokenTransfer(this.token2, this.regulator2, -txnAmt)
//                    .addTokenTransfer(this.token2, this.fsp2, txnAmt);
//
//            txn2.freezeWith(regulatorClient2)
//                    .sign(this.privateKey2)
//                    .execute(client);
//
//            this.output.append("Transferred " + txnAmt + " from regulator B: " + this.regulator2.toString() + " to FSP B: " + this.fsp2.toString() + " on token: " + this.token1.toString()).append("\n");
//
//            this.transfer(this.token1, this.fsp1, this.endUserWallet1, txnAmt2, this.privateKey3, regulatorClient1);
//            this.output.append("Transferred " + txnAmt2 + " from FSP A: " + this.fsp1.toString() + " to END USER A: " + this.endUserWallet1.toString() + " on token: " + this.token1.toString()).append("\n");
//
//            this.output.append("################ Balance BEfore Transactions ##################").append("\n");
//            this.output.append( "BALANCE FOR FSP A" + printBalance(client, this.fsp1, this.token1)).append("\n");
//            this.output.append("BALANCE FOR FSP B" + printBalance(client, this.fsp2, this.token2)).append("\n");
//            this.output.append("BALANCE FOR End User A" + printBalance(client, this.endUserWallet1, this.token1)).append("\n");
//            this.output.append("################ Balance BEfore Transactions ##################").append("\n");
//            this.output.append("####################################################################################").append("\n");
//
//        } catch (Exception e){
//            System.out.println(e);
//        }
//    }
//
//    public void transfer(TokenId token, AccountId user1, AccountId user2, long amount, PrivateKey privateKey, Client client){
//        try{
//        TransferTransaction txn3 = new TransferTransaction()
//                .addTokenTransfer(token, user1, -amount)
//                .addTokenTransfer(token, user2, amount);
//            txn3.freezeWith(client)
//                .sign(privateKey)
//                .execute(client);
//
//    } catch (Exception e){
//        System.out.println(e);
//    }
//    }
//
//
//    public void initiateEnduserTransfer(){
//
//        this.output.append("############### USER A REQUESTS TO SEND 100 ECEDIS TO USER B #########################").append("\n");
//        long transferAmountFromUser = 100;
//        long rate = 20;
//        this.output.append("############### USER A RECEIVES RATE AS 20 #########################").append("\n");
//        long amountToSend = transferAmountFromUser * rate;
//        this.output.append("################ BALANCE OF END USER A AND FSP A BEFORE TRANSACTION BECAUSE FSP NEEDS TO TAKE ENAIRA TO START TRANSACTION ###############").append("\n");
//        this.output.append( "BALANCE FOR FSP A" + printBalance(client, this.fsp1, this.token1)).append("\n");
//        this.output.append("BALANCE FOR END USER A" + printBalance(client, this.endUserWallet1, this.token1)).append("\n");
//        this.output.append("################ BALANCE OF END USER A AND FSP A BEFORE TRANSACTION BECAUSE FSP NEEDS TO TAKE ENAIRA TO START TRANSACTION ###############").append("\n");
//        // take money from end user to fsp
//        this.transfer(this.token1, this.endUserWallet1, this.fsp1, amountToSend, this.privateKey5, regulatorClient1);
//        this.output.append("################ BALANCE OF END USER A AND FSP A AFTER TRANSACTION BECAUSE FSP NEEDS TO TAKE ENAIRA TO START TRANSACTION ###############").append("\n");
//        this.output.append( "BALANCE FOR FSP A" + printBalance(client, this.fsp1, this.token1)).append("\n");
//        this.output.append("BALANCE FOR END USER A" + printBalance(client, this.endUserWallet1, this.token1)).append("\n");
//        this.output.append("################ BALANCE OF END USER A AND FSP A AFTER TRANSACTION BECAUSE FSP NEEDS TO TAKE ENAIRA TO START TRANSACTION ###############").append("\n");
//        this.output.append("################ FSP A WILL CREATE A BUY ORDER WHICH SHOULD BE MATCHED IMMEDIATELY BECAUSE OF THE PoC ###############").append("\n");
//
//        this.output.append("################ BALANCE OF FSP A AND FSP B BEFORE TRANSACTION ###############").append("\n");
//        this.output.append( "BALANCE FOR FSP A TOKEN 1" + printBalance(client, this.fsp1, this.token1)).append("\n");
//        this.output.append("BALANCE FOR FSP A TOKEN 2" + printBalance(client, this.fsp1, this.token2)).append("\n");
//        this.output.append( "BALANCE FOR FSP B TOKEN 1" + printBalance(client, this.fsp2, this.token1)).append("\n");
//        this.output.append("BALANCE FOR FSP B TOKEN 2" + printBalance(client, this.fsp2, this.token2)).append("\n");
//        this.output.append("################ BALANCE OF FSP A AND FSP B BEFORE TRANSACTION ###############").append("\n");
//        // after taking money
//        // order match
//        orderMatchingService.createOrder(this.fsp1, "sell", this.currencyPair.getSymbol(), transferAmountFromUser, 15, this.privateKey3, this.token1, this.token2);
//        this.output.append("################ BALANCE OF FSP A AND FSP B AFTER TRANSACTION ###############").append("\n");
//        this.output.append( "BALANCE FOR FSP A TOKEN 1" + printBalance(client, this.fsp1, this.token1)).append("\n");
//        this.output.append("BALANCE FOR FSP A TOKEN 2" + printBalance(client, this.fsp1, this.token2)).append("\n");
//        this.output.append( "BALANCE FOR FSP B TOKEN 1" + printBalance(client, this.fsp2, this.token1)).append("\n");
//        this.output.append("BALANCE FOR FSP B TOKEN 2" + printBalance(client, this.fsp2, this.token2)).append("\n");
//        this.output.append("################ BALANCE OF FSP A AND FSP B AFTER TRANSACTION ###############").append("\n");
//        this.output.append("######### NOW AFTER GETTING TOKEN 2, FSP A CAN JUST EASILY FINISH THE FLOW BY SENDING TO END USER 2 ##################").append("\n");
//        this.output.append("################ BALANCE OF END USER B AND FSP A BEFORE TRANSACTION ###############").append("\n");
//        this.output.append( "BALANCE FOR FSP A" + printBalance(client, this.fsp1, this.token2)).append("\n");
//        this.output.append("BALANCE FOR END USER B" + printBalance(client, this.endUserWallet2, this.token2)).append("\n");
//        this.output.append("################ BALANCE OF END USER B AND FSP A BEFORE TRANSACTION ###############").append("\n");
//        // transfer to enduser 2
//        this.transfer(this.token2, this.fsp1, this.endUserWallet2, transferAmountFromUser, this.privateKey3, regulatorClient1);
//        this.output.append("################ BALANCE OF END USER B AND FSP A BEFORE TRANSACTION ###############").append("\n");
//        this.output.append( "BALANCE FOR FSP A" + printBalance(client, this.fsp1, this.token2)).append("\n");
//        this.output.append("BALANCE FOR END USER B" + printBalance(client, this.endUserWallet2, this.token2)).append("\n");
//        this.output.append("################ BALANCE OF END USER B AND FSP A BEFORE TRANSACTION ###############").append("\n");
//
//
//        this.output.append("################ FINAL BALANCE ###############").append("\n");
//        this.output.append( "BALANCE FOR FSP A TOKEN 1" + printBalance(client, this.fsp1, this.token1)).append("\n");
//        this.output.append("BALANCE FOR FSP A TOKEN 2" + printBalance(client, this.fsp1, this.token2)).append("\n");
//        this.output.append( "BALANCE FOR FSP B TOKEN 1" + printBalance(client, this.fsp2, this.token1)).append("\n");
//        this.output.append("BALANCE FOR FSP B TOKEN 2" + printBalance(client, this.fsp2, this.token2)).append("\n");
//        this.output.append("BALANCE FOR END USER B" + printBalance(client, this.endUserWallet2, this.token2)).append("\n");
//        this.output.append("BALANCE FOR END USER A" + printBalance(client, this.endUserWallet1, this.token1)).append("\n");
//        this.output.append("################ FINAL BALANCE ###############").append("\n");
//
//
//    }
//
//    public void createNewOrderAndAdd(){
//        orderMatchingService.createOrder(this.fsp1, "buy", this.currencyPair.getSymbol(), 100, 15, this.privateKey3, this.token1, this.token2);
//        orderMatchingService.createOrder(this.fsp2, "buy", this.currencyPair.getSymbol(), 100, 15, this.privateKey4, this.token1, this.token2);
//    }
//
//}
