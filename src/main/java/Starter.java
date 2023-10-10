import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.hedera.hashgraph.sdk.*;
import helper.FileOperations;


public class Starter {





    public static void main(String[] args){

        final TestApproach approach = new TestApproach();
        final TestApproach2 approach2 = new TestApproach2();
        final TestApproachOneWithFile approach3 = new TestApproachOneWithFile();

        approach.run();
//          approach2.run();
//        final FileOperations fileOperations = new FileOperations();
//        final ApproachOne approach = new ApproachOne();
//
//        approach.initializeWallets();
//        System.out.println("DONE WITH WALLETS");
//        approach.initializeTokens();
//        System.out.println("DONE WITH TOKENS");
//        approach.initializeTokenAssociation();
//        System.out.println("DONE WITH TOKEN ASSOCIATION");
//        approach.giveSomeMoneyToFSP();
//        System.out.println("DONE WITH MONEY");
//
//
//        approach.createNewOrderAndAdd();
//        approach.initiateEnduserTransfer();
//
//        fileOperations.writeFile("output.txt", approach.output.toString());


//        SameOperator();
//        DiffOperator();
    }




//    public static void SameOperator(){
//
//        Client client = getClient();
//
//        AccountId account1 = createAccount(client, privateKey1);
//        AccountId account2 = createAccount(client, privateKey2);
//
//        TransactionResponse response = null;
//        TransactionReceipt receipt = null;
//        try {
//            TokenId tokenId1 = createToken(client, privateKey1, "NTP", account1);
//            TokenId tokenId2 = createToken(client, privateKey2, "NFP", account2);
//            System.out.println(String.format("The account: %s has a token Id of %s.", account1.toString(), tokenId1.toString()));
//            System.out.println(String.format("The account: %s has a token Id of %s.", account2.toString(), tokenId2.toString()));
//
//            receipt = tokenAssociation(client, account2, tokenId1, privateKey1, privateKey2);
//            System.out.println("The token =" + tokenId1 + " is associated with " + receipt.accountId + " , status="+ receipt.status);
//            System.out.println(receipt);
//            receipt = tokenAssociation(client, account1, tokenId2, privateKey2, privateKey1);
//            System.out.println("The token =" + tokenId1 + " is associated with " + receipt.accountId + " , status="+ receipt.status);
//            System.out.println(receipt);
//
//            receipt = kycGrant(client, account2, tokenId1, privateKey1);
//            System.out.println("The kyc grant token =" + tokenId1 + "  with " + receipt.accountId + " , status="+ receipt.status);
//            receipt = kycGrant(client, account1, tokenId2, privateKey2);
//            System.out.println("The kyc grant token =" + tokenId2 + "  with " + receipt.accountId + " , status="+ receipt.status);
//
//            // before Transaction
//            printBalance(client, account1, tokenId1);
//            printBalance(client, account2, tokenId1);
//            printBalance(client, account1, tokenId2);
//            printBalance(client, account2, tokenId2);
//            // buy token 1 at 10 and account2 wants to buy 10
//            Long amount1 = 100L;
//            Long amount2 = 100*3L;
//            response = atomicSwap(client, account1, account2, amount1, amount2, privateKey1, privateKey2, tokenId1, tokenId2);
//            receipt = response.getReceipt(getClient());
//            System.out.println(receipt);
//            // after transaction
//            printBalance(client, account1, tokenId1);
//            printBalance(client, account2, tokenId1);
//            printBalance(client, account1, tokenId2);
//            printBalance(client, account2, tokenId2);
//            // AccountBalance{hbars=1000 tℏ, tokens={0.0.1171=10000}}
//            // AccountBalance{hbars=1000 tℏ, tokens={0.0.1171=0, 0.0.1172=10000}}
////            long txnAmt = 200;
////            TransferTransaction txn = new TransferTransaction()
////                    .addTokenTransfer(tokenId, account, -txnAmt)
////                    .addTokenTransfer(tokenId, account2, txnAmt);
////            response = txn.freezeWith(client)
////                .sign(privateKey)
////                    .execute(client);
////            receipt = response.getReceipt(client);
////
////            System.out.println("The transfer of = " + tokenId + "  from " + account + " to " + account2 +" , status="+ receipt.status);
////
//            client.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }

//    public static void DiffOperator(){
//
//        Client client = getClient();
//
//        PrivateKey privateKey1  = PrivateKey.generateED25519();
//        PrivateKey privateKey2  = PrivateKey.generateED25519();
//
//        AccountId account1 = createAccount(client, privateKey1);
//        AccountId account2 = createAccount(client, privateKey2);
//
//        Client client1 = getDynamicClient(account1, privateKey1);
//        Client client2 = getDynamicClient(account2, privateKey2);
//
//        TransactionResponse response = null;
//        TransactionReceipt receipt = null;
//        try {
//            TokenId tokenId1 = createToken(client1, privateKey1, "NTP", account1);
//            TokenId tokenId2 = createToken(client2, privateKey2, "NFP", account2);
//            System.out.println(String.format("The account: %s has a token Id of %s.", account1.toString(), tokenId1.toString()));
//            System.out.println(String.format("The account: %s has a token Id of %s.", account2.toString(), tokenId2.toString()));
//
//            receipt = tokenAssociation(client1, account2, tokenId1, privateKey1, privateKey2);
//            System.out.println("The token =" + tokenId1 + " is associated with " + receipt.accountId + " , status="+ receipt.status);
//            System.out.println(receipt);
//            receipt = tokenAssociation(client2, account1, tokenId2, privateKey2, privateKey1);
//            System.out.println("The token =" + tokenId1 + " is associated with " + receipt.accountId + " , status="+ receipt.status);
//            System.out.println(receipt);
//
//            receipt = kycGrant(client1, account2, tokenId1, privateKey1);
//            System.out.println("The kyc grant token =" + tokenId1 + "  with " + receipt.accountId + " , status="+ receipt.status);
//            receipt = kycGrant(client2, account1, tokenId2, privateKey2);
//            System.out.println("The kyc grant token =" + tokenId2 + "  with " + receipt.accountId + " , status="+ receipt.status);
//
//            // before Transaction
//            printBalance(client1, account1, tokenId1);
//            printBalance(client1, account2, tokenId1);
//            printBalance(client2, account1, tokenId2);
//            printBalance(client2, account2, tokenId2);
//            // buy token 1 at 10 and account2 wants to buy 10
//            Long amount1 = 100L;
//            Long amount2 = 100*3L;
//            response = atomicSwap(client, account1, account2, amount1, amount2, privateKey1, privateKey2, tokenId1, tokenId2);
//            receipt = response.getReceipt(client);
//            System.out.println(receipt);
//            // after transaction
//            printBalance(client1, account1, tokenId1);
//            printBalance(client1, account2, tokenId1);
//            printBalance(client2, account1, tokenId2);
//            printBalance(client2, account2, tokenId2);
//            // AccountBalance{hbars=1000 tℏ, tokens={0.0.1171=10000}}
//            // AccountBalance{hbars=1000 tℏ, tokens={0.0.1171=0, 0.0.1172=10000}}
//            long txnAmt = 10;
//            TransferTransaction txn = new TransferTransaction()
//                    .addTokenTransfer(tokenId1, account1, -txnAmt)
//                    .addTokenTransfer(tokenId1, account2, txnAmt);
//            response = txn.freezeWith(client)
//                .sign(privateKey1)
//                    .execute(client);
//            receipt = response.getReceipt(client);
//
//            System.out.println("The transfer of = " + tokenId1 + "  from " + account1 + " to " + account2 +" , status="+ receipt.status);
////
//            // after transaction
//            printBalance(client1, account1, tokenId1);
//            printBalance(client1, account2, tokenId1);
//            printBalance(client2, account1, tokenId2);
//            printBalance(client2, account2, tokenId2);
//            client.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }








    // need two accounts then
    // same client from the looks of it
    // 2 new tokens

    // create token function





}
