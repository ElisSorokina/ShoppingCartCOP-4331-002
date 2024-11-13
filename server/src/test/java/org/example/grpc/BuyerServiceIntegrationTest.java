package org.example.grpc;

import io.grpc.StatusRuntimeException;
import org.example.data.model.CartEntry;
import org.example.data.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuyerServiceIntegrationTest extends IntegrationTestBase{
    private BuyerServiceGrpc.BuyerServiceBlockingStub buyerServiceBlockingStub;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PlatformTransactionManager transactionManager;
        

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        buyerServiceBlockingStub = BuyerServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void testGetEmptyItemList() {
        signUpBuyer();
        buyerLogin();
        GetItemListResponse itemList = buyerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(buyerSessionId).build());
        assertEquals(0, itemList.getItemCount());
    }

    @Test
    void testGetItemListNoLogin() {
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> buyerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().build()));
        assertEquals("Invalid session id", statusRuntimeException.getStatus().getDescription());
    }

    @Test
    void testGetItemListIfSeller() {
        signUpSeller();
        sellerLogin();
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> buyerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sellerSessionId).build()));
        assertEquals("For customers only", statusRuntimeException.getStatus().getDescription());
    }

    /**
     * For this test we add items from the seller side, and check if these items are available for the buyer
     */
    @Test
    void testGetItemList(){
        signUpBuyer();
        buyerLogin();

        signUpSeller();
        sellerLogin();
        List<Item> addedFromDB = addItemsToDB();


//

        List<Item> itemsInCart = buyerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(buyerSessionId).build()).getItemList();
        assertEquals(addedFromDB, itemsInCart);
//        assertEquals(newItem1.getQuantity(), item1FromMethod.getQuantity());
//        assertEquals(newItem1.getInvoicePriceCents(), item1FromMethod.getInvoicePriceCents());
//        assertEquals(newItem1.getSellPriceCents(), item1FromMethod.getSellPriceCents());
//        assertEquals(item1FromDB.getId().toString(), item1FromMethod.getId());
//        assertEquals(newItem2.getName(), item2FromMethod.getName());
//        assertEquals(newItem2.getQuantity(), item2FromMethod.getQuantity());
//        assertEquals(newItem2.getInvoicePriceCents(), item2FromMethod.getInvoicePriceCents());
//        assertEquals(newItem2.getSellPriceCents(), item2FromMethod.getSellPriceCents());
//        assertEquals(item2FromDB.getId().toString(), item2FromMethod.getId());

    }

    @Test
    void addItemsToCart(){
        signUpBuyer();
        buyerLogin();

        signUpSeller();
        sellerLogin();
        List<Item> addedFromDB = addItemsToDB();
        String itemId1 = addedFromDB.get(0).getId();
        String itemId2 = addedFromDB.get(1).getId();
        AddItemRequest request1 = AddItemRequest.newBuilder().setSessionId(buyerSessionId).setItemId(itemId1).build();
        AddItemRequest request2 = AddItemRequest.newBuilder().setSessionId(buyerSessionId).setItemId(itemId2).build();
        buyerServiceBlockingStub.addItemToCart(request1);
        buyerServiceBlockingStub.addItemToCart(request1);
        buyerServiceBlockingStub.addItemToCart(request2);
        Map<String, CartEntry> cartEntryByItemId = getCartEntryById();
        var itemIds = cartEntryByItemId.keySet();
        List<Item> updatedItemList = getItemsList();
        assertEquals(2, cartEntryByItemId.get(itemId1).getItemCount());
        assertEquals(1, cartEntryByItemId.get(itemId2).getItemCount());
        // assert amounts left in the store. initial amount is 10 and 5
        assertEquals(8, updatedItemList.get(0).getQuantity());
        assertEquals(4, updatedItemList.get(1).getQuantity());

        UpdateCartRequest updateRequest = UpdateCartRequest.newBuilder().setSessionId(buyerSessionId).setItemId(itemId1).setItemCount(9).build();
        buyerServiceBlockingStub.updateCart(updateRequest);
        updatedItemList  = getItemsList();
        assertEquals(1,updatedItemList.get(0).getQuantity());

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.newBuilder().setSessionId(buyerSessionId).setItemId(itemId2).build();
        buyerServiceBlockingStub.deleteItemFromCart(deleteItemRequest);
        var cartItemList = buyerServiceBlockingStub.getCartItemList(GetCartItemListRequest.newBuilder().setSessionId(buyerSessionId).build()).getCart().getCartEntriesList();
        assertEquals(1, cartItemList.size());
        assertEquals(itemId1, cartItemList.get(0).getItemId());


    }

    private List<Item> getItemsList() {
        var updatedItemList =buyerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(buyerSessionId).build()).getItemList();
        return updatedItemList;
    }


    public Map<String, CartEntry> getCartEntryById() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        return transactionTemplate.execute((TransactionCallback<Map<String, CartEntry>>) status -> {
            var cart = cartRepository.findByBuyer(sessionStore.getUser(UUID.fromString(buyerSessionId)));
            var cartEntries = cart.getCartEntries();
            var cartEntryByItemId = new HashMap<String, CartEntry>();
            for (CartEntry cartEntry : cartEntries) {
                cartEntryByItemId.put(cartEntry.getItemId().toString(), cartEntry);
            }
            return cartEntryByItemId;
        });
    }

}
