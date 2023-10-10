import com.hedera.hashgraph.sdk.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMatchingService {

    private List<Order> orders = new ArrayList<>();
    private PrivateKey orderMatchingPrivateKey = PrivateKey.generateED25519();
    private AccountId orderMatchingAccount = HederaSDK.createAccount(HederaSDK.getClient(), orderMatchingPrivateKey);
    private Client client = HederaSDK.getDynamicClient(orderMatchingAccount, orderMatchingPrivateKey);

    public void matchNewOrders(final Order newOrder){
        // get sell orders
        final List<Order> buyOrders = orders.stream()
                .filter(order -> "buy".equals(order.getOrderType()))
                .collect(Collectors.toList());
        // get buy orders
        final List<Order> sellOrders = orders.stream()
                .filter(order -> "sell".equals(order.getOrderType()))
                .collect(Collectors.toList());
        if (newOrder.getOrderType().equals("buy")) {
            // match with sells
            for (final Order existingOrder : sellOrders) {
                if (isCompatible(existingOrder, newOrder)) {
                    System.out.println("order is compatible " + existingOrder.getOrderType() + newOrder.getOrderType());
                    executeMatch(existingOrder, newOrder);
                    return;
                }
            }
        } else {
            // match with buys
            for (final Order existingOrder : buyOrders) {
                if (isCompatible(existingOrder, newOrder)) {
                    System.out.println("order is compatible " + existingOrder.getOrderType() + newOrder.getOrderType());
                    executeMatch(existingOrder, newOrder);
                    return;
                }
            }
        }
        System.out.println("no match found");
    }

    public void matchOldOrders(){

    }

    public boolean isCompatible(final Order existingOrder, final Order newOrder){
        boolean samePair = false;
//        boolean withinMinMax = false;
        boolean belowQuantity = false;
        boolean sameUser = false;
        if (newOrder.getSymbol().equals(existingOrder.getSymbol())) {
            samePair = true;
        }
        if (newOrder.getQuantity() <= existingOrder.getQuantity()) {
            belowQuantity = true;
        }
//        if (newOrder.getQuantity() <= existingOrder.getMaximumPrice() && newOrder.getQuantity() >= existingOrder.getMinimumPrice()) {
//            withinMinMax = true;
//        }
        if (newOrder.getAccountId().toString().equals(existingOrder.getAccountId().toString())) {
            sameUser = true;
        }

        return samePair && belowQuantity && !sameUser;
    }

    public void executeMatch(Order order1, Order order2){
        System.out.println("order match execution");
        // handle extra balance check even though balance is checked and locked at order creation
        // handle order not taking all the balance from the opposite order
    final TransferTransaction transferTransaction = HederaSDK.atomicSwap(this.client,
            order2.getAccountId(),
            order1.getAccountId(),
            order1.getQuantity(),
            order2.getPrice() * order1.getQuantity(),
            order1.getTokenId1(),
            order1.getTokenId2()
            );
System.out.println();
System.out.println(transferTransaction.getTransactionId().toString());

    final TransferTransaction sourceSign = HederaSDK.signSwap(transferTransaction, order2.getPrivateKey());
    final TransferTransaction destinationSign = HederaSDK.signSwap(sourceSign, order1.getPrivateKey());
    final TransactionResponse transactionResponse = HederaSDK.executeSwap(destinationSign, client);

    System.out.println(transactionResponse);

//    log.info(transactionResponse.getReceipt(hederaSDK.getEmtechMasterAccount()).toString());
        // remove order from redis
        // update/remove order
        if (order1.getQuantity() == order2.getQuantity()) {
            // exact match in quantity which cancels all orders
            orders.remove(order1);
//            redisService.deleteObject("order:" + existingOrder.getOrderId());
        } else {
            // either quantity is matched or partial is done
            // set new quantity because it was taken from it
            order1.setQuantity(order1.getQuantity() - order2.getQuantity());
        }
        // if order is matched, no matter what, it is exact quantity
//        redisService.deleteObject("order:" + newOrder.getOrderId());
        orders.remove(order2);
    }



    public void createOrder(AccountId accountId, String orderType, String symbol, long quantity, long price, PrivateKey privateKey, TokenId token1, TokenId token2){
        final Order order = new Order(1, accountId, orderType, symbol, price, quantity, token1, privateKey, token2, new Date().getTime());
        orders.add(order);
        matchNewOrders(order);
    }

}
