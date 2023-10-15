//import com.hedera.hashgraph.sdk.*;
//import helper.FileOperations;
//
//public class TestApproachOneWithFile {
//
//        private Client client;
//        private Client regulatorClient1;
//        private Client regulatorClient2;
//        private Client crossborderClient;
//        private AccountId endUserWallet1;
//        private AccountId crossborderAccount;
//        private AccountId endUserWallet2;
//        private AccountId regulator1;
//        private AccountId regulator2;
//        private AccountId fsp1;
//        private AccountId fsp2;
//        private TokenId token1;
//        private TokenId token2;
//        PrivateKey privateKey1;
//        PrivateKey privateKey2;
//        PrivateKey privateKey3;
//        PrivateKey privateKey4;
//        PrivateKey privateKey5;
//        PrivateKey privateKey6;
//        PrivateKey crossborderPrivateKey;
//        ScheduleId transaction1;
//        ScheduleId transaction2;
//        ScheduleId transaction3;
//
//        private FileOperations fileOperations = new FileOperations();
//
//
//
//
//        public void run(){
//            this.initializeWallets();
//            this.initializeTokens();
//            this.initializeTokenAssociation();
//            this.giveSomeMoneyToFSP();
//            this.doTransfer();
//            this.signTransactions();
//        }
//
//        private static String printBalance(Client client, AccountId accountId, TokenId tokenId){
//            AccountBalance balance = HederaSDK.getAccountBalance(client, accountId);
//            return String.format("The balance for account: %s with tokenId: %s is %s", accountId.toString(), tokenId.toString(),balance.tokens.get(tokenId));
//        }
//
//        private void initializeWallets() {
//            this.privateKey1 = PrivateKey.generateED25519();
//            this.privateKey2 = PrivateKey.generateED25519();
//            this.privateKey3 = PrivateKey.generateED25519();
//            this.privateKey4 = PrivateKey.generateED25519();
//            this.privateKey5 = PrivateKey.generateED25519();
//            this.privateKey6 = PrivateKey.generateED25519();
//            this.crossborderPrivateKey = PrivateKey.generateED25519();
//
//            this.client = HederaSDK.getClient(); // default hedera client
//            this.crossborderAccount = HederaSDK.createAccount(this.client, this.crossborderPrivateKey);
//            System.out.println(this.crossborderAccount.toString());
//            System.out.println(this.crossborderPrivateKey);
//            this.crossborderClient = HederaSDK.getDynamicClient(this.crossborderAccount, this.crossborderPrivateKey);
//            System.out.println("####################################################################################");
//            this.regulator1 = HederaSDK.createAccount(this.client, privateKey1);
//            System.out.println(HederaSDK.getAccountBalance(this.client, this.regulator1));
//            System.out.println("Created Regulator A wallet with id: " + this.regulator1.toString());
//            this.regulator2 =  HederaSDK.createAccount(this.client, privateKey2);
//            System.out.println("Created Regulator B wallet with id: " + this.regulator2.toString());
//            this.regulatorClient1 = HederaSDK.getDynamicClient(this.regulator1, this.privateKey1);
//            this.regulatorClient2 = HederaSDK.getDynamicClient(this.regulator2, this.privateKey2);
//            this.fsp1 = HederaSDK.createAccountUser(this.regulatorClient1, privateKey3);
//            System.out.println("Created FSP A wallet with id: " + this.fsp1.toString());
//            this.fsp2 =  HederaSDK.createAccountUser(this.regulatorClient2, privateKey4);
//            System.out.println("Created FSP B wallet with id: " + this.fsp2.toString());
//            this.endUserWallet1 = HederaSDK.createAccountUser(this.regulatorClient1, privateKey5);
//            System.out.println("Created End User A wallet with id: " + this.endUserWallet1.toString());
//            this.endUserWallet2 = HederaSDK.createAccountUser(this.regulatorClient2, privateKey6);
//            System.out.println("Created End User B wallet with id: " + this.endUserWallet2.toString());
//            System.out.println("####################################################################################");
//
//            System.out.println(this.privateKey1);
//            System.out.println(this.privateKey2);
//            System.out.println(this.privateKey3);
//            System.out.println(this.privateKey4);
//            System.out.println(this.privateKey5);
//            System.out.println(this.privateKey6);
//        }
//
//        private void initializeTokens() {
//            System.out.println("####################################################################################");
//            this.token1 = HederaSDK.createToken(this.regulatorClient1, privateKey1, "CEDIS", this.regulator1);
//            System.out.println("Created new Token with ID: " + this.token1.toString());
//            this.token2 = HederaSDK.createToken(this.regulatorClient2, privateKey2, "NAIRA", this.regulator2);
//            System.out.println("Created new Token 2 with ID: " + this.token2.toString());
//            System.out.println("####################################################################################");
//        }
//
//        private void initializeTokenAssociation(){
//
//            HederaSDK.tokenAssociation(regulatorClient1, this.fsp1, this.token1, privateKey1, privateKey3);
//            HederaSDK.tokenAssociation(regulatorClient1, this.endUserWallet1, this.token1, privateKey1, privateKey5);
//            HederaSDK.kycGrant(regulatorClient1, this.fsp1, this.token1, this.privateKey1);
//            HederaSDK.kycGrant(regulatorClient1, this.endUserWallet1, this.token1, this.privateKey1);
//
//            HederaSDK.tokenAssociation(regulatorClient2, this.fsp1, this.token2, privateKey2, privateKey3);
//            HederaSDK.tokenAssociation(regulatorClient2, this.fsp2, this.token2, privateKey2, privateKey4);
//            HederaSDK.tokenAssociation(regulatorClient2, this.endUserWallet2, this.token2, privateKey2, privateKey6);
//            HederaSDK.kycGrant(regulatorClient2, this.fsp1, this.token2, this.privateKey2);
//            HederaSDK.kycGrant(regulatorClient2, this.fsp2, this.token2, this.privateKey2);
//            HederaSDK.kycGrant(regulatorClient2, this.endUserWallet2, this.token2, this.privateKey2);
//
//        }
//
//        private void transfer(TokenId token, AccountId user1, AccountId user2, long amount, PrivateKey privateKey, Client client){
//            try{
//                TransferTransaction txn = new TransferTransaction()
//                        .addTokenTransfer(token, user1, -amount)
//                        .addTokenTransfer(token, user2, amount);
//                txn.freezeWith(client)
//                        .sign(privateKey)
//                        .execute(client);
//
//            } catch (Exception e){
//                System.out.println(e);
//            }
//        }
//
//        private void giveSomeMoneyToFSP(){
//            try {
//                long txnAmt = 1000000;
//                long txnAmt2 = 10000;
//                System.out.println("################ Balance BEfore Transactions ##################");
//                System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
//                System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
//                System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
//                System.out.println("################ Balance BEfore Transactions ##################");
//                System.out.println("####################################################################################");
//                this.transfer(this.token1, this.regulator1, this.fsp1,txnAmt, this.privateKey1, this.regulatorClient1);
//                this.transfer(this.token2, this.regulator2, this.fsp2,txnAmt, this.privateKey2, this.regulatorClient2);
//                System.out.println("Transferred " + txnAmt + " from regulator A: " + this.regulator1.toString() + " to FSP A: " + this.fsp1.toString() + " on token: " + this.token1.toString());
//                System.out.println("Transferred " + txnAmt + " from regulator B: " + this.regulator2.toString() + " to FSP B: " + this.fsp2.toString() + " on token: " + this.token1.toString());
//                this.transfer(this.token2, this.fsp2, this.endUserWallet2, txnAmt2, this.privateKey4, this.regulatorClient2);
//                System.out.println("Transferred " + txnAmt2 + " from FSP A: " + this.fsp1.toString() + " to END USER A: " + this.endUserWallet1.toString() + " on token: " + this.token1.toString());
//                System.out.println("################ Balance BEfore Transactions ##################");
//                System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
//                System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
//                System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
//                System.out.println("################ Balance BEfore Transactions ##################");
//                System.out.println("####################################################################################");
//
//            } catch (Exception e){
//                System.out.println(e);
//            }
//        }
//
//        private void doTransfer(){
//            // exhange rate to user
//            // exchange rate to fsp B
//            final Double exchangeRateToUser = 6.5;
//            final Double exchangeRateToFsp = 5.3;
//            // amount user B want to transfer
//            final Long amount = 100L; // cedis to send
//            final Long amountForUser = (long) (amount * exchangeRateToUser); // cedis to send
//            final Long amountForFSP = (long) (amount * exchangeRateToFsp); // cedis to send
//            // create scheduled transfer from End User B to FSP B
//            this.transaction1 = HederaSDK.createScheduleTransaction(this.endUserWallet2, this.fsp2, this.token2, amountForUser, this.crossborderClient, this.crossborderPrivateKey);
//            System.out.println(this.transaction1.toString());
//            // create scheduled transfer from FSP B to FSP A
//            this.transaction2 = HederaSDK.createScheduleTransaction(this.fsp2, this.fsp1, this.token2, amountForFSP, this.crossborderClient, this.crossborderPrivateKey);
//            System.out.println(this.transaction2.toString());
//            // create scheduled transfer from FSP A to End User A
//            this.transaction3 = HederaSDK.createScheduleTransaction(this.fsp1, this.endUserWallet1, this.token1, amount, this.crossborderClient, this.crossborderPrivateKey);
//            System.out.println(this.transaction3.toString());
//        }
//
//        private void signTransactions(){
//            System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
//            System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
//            System.out.println("BALANCE FOR End User A:" + printBalance(this.regulatorClient1, this.endUserWallet1, this.token1));
//            System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
//            System.out.println("BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token2));
//            HederaSDK.signScheduledTransaction(this.transaction1, this.privateKey6, this.regulatorClient2);
//            HederaSDK.signScheduledTransaction(this.transaction2, this.privateKey4, this.regulatorClient2);
//            HederaSDK.signScheduledTransaction(this.transaction3, this.privateKey3, this.regulatorClient1);
//            System.out.println("####################################################################################");
//            System.out.println( "BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token1));
//            System.out.println("BALANCE FOR FSP B:" + printBalance(this.regulatorClient2, this.fsp2, this.token2));
//            System.out.println("BALANCE FOR End User A:" + printBalance(this.regulatorClient1, this.endUserWallet1, this.token1));
//            System.out.println("BALANCE FOR End User B:" + printBalance(this.regulatorClient2, this.endUserWallet2, this.token2));
//            System.out.println("BALANCE FOR FSP A:" + printBalance(this.regulatorClient1, this.fsp1, this.token2));
////        HederaSDK.executeTransaction(this.crossborderClient, this.transaction1);
////        HederaSDK.executeTransaction(this.crossborderClient, this.transaction2);
////        HederaSDK.executeTransaction(this.crossborderClient, this.transaction3);
//        }
//
//    }
