package org.example.grpc;


import io.grpc.StatusRuntimeException;
import org.example.data.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SellerServiceIntegrationTest extends IntegrationTestBase {

    @Test
    void testGetItemList() {
        signUpSeller();
        sellerLogin();
        GetItemListResponse itemList = sellerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sellerSessionId).build());
        assertEquals(0, itemList.getItemCount());
    }

    @Test
    void testGetItemListNoLogin() {
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> sellerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().build()));
        assertEquals("Invalid session id", statusRuntimeException.getStatus().getDescription());
    }

    @Test
    void testGetItemListIfBuyer() {
        signUpBuyer();
        buyerLogin();
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> sellerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(buyerSessionId).build()));
        assertEquals("For sellers only", statusRuntimeException.getStatus().getDescription());
    }

    @Test
    void testUpdateItemListNoLogin() {
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> sellerServiceBlockingStub.updateItemList(UpdateItemListRequest.newBuilder().build()));
        assertEquals("Invalid session id", statusRuntimeException.getStatus().getDescription());
    }

    @Test
    void testUpdateItemListIfBuyer() {
        signUpBuyer();
        buyerLogin();
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> sellerServiceBlockingStub.updateItemList(UpdateItemListRequest.newBuilder().setSessionId(buyerSessionId).build()));
        assertEquals("For sellers only", statusRuntimeException.getStatus().getDescription());
    }
    @Test
    void testAddItems(){
        addItemsToDB();

        var itemsFromDB = itemRepository.findBySeller(sessionStore.getUser(UUID.fromString(sellerSessionId)));
        assertEquals(2, itemsFromDB.size());
        var item1FromDB = itemsFromDB.get(0);
        var item2FromDB = itemsFromDB.get(1);

        assertEquals(newItem1.getName(), item1FromDB.getName());
        assertEquals(newItem1.getQuantity(), item1FromDB.getQuantity());
        assertEquals(newItem1.getInvoicePriceCents(), item1FromDB.getInvoicePriceCents());
        assertEquals(newItem1.getSellPriceCents(), item1FromDB.getSellPriceCents());
        assertEquals(newItem2.getName(), item2FromDB.getName());
        assertEquals(newItem2.getQuantity(), item2FromDB.getQuantity());
        assertEquals(newItem2.getInvoicePriceCents(), item2FromDB.getInvoicePriceCents());
        assertEquals(newItem2.getSellPriceCents(), item2FromDB.getSellPriceCents());
        GetItemListResponse itemList = sellerServiceBlockingStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sellerSessionId).build());
        assertEquals(2, itemList.getItemCount());
        var item1FromMethod = itemList.getItem(0);
        var item2FromMethod = itemList.getItem(1);

        assertEquals(newItem1.getName(), item1FromMethod.getName());
        assertEquals(newItem1.getQuantity(), item1FromMethod.getQuantity());
        assertEquals(newItem1.getInvoicePriceCents(), item1FromMethod.getInvoicePriceCents());
        assertEquals(newItem1.getSellPriceCents(), item1FromMethod.getSellPriceCents());
        assertEquals(item1FromDB.getId().toString(), item1FromMethod.getId());
        assertEquals(newItem2.getName(), item2FromMethod.getName());
        assertEquals(newItem2.getQuantity(), item2FromMethod.getQuantity());
        assertEquals(newItem2.getInvoicePriceCents(), item2FromMethod.getInvoicePriceCents());
        assertEquals(newItem2.getSellPriceCents(), item2FromMethod.getSellPriceCents());
        assertEquals(item2FromDB.getId().toString(), item2FromMethod.getId());

        var updateItemRequestForDeletion = UpdateItemListRequest.newBuilder()
                .addItem(Item.newBuilder().setDeleted(true).setId(item1FromMethod.getId()))
                .addItem(Item.newBuilder().setDeleted(true).setId(item2FromMethod.getId()))
                .setSessionId(sellerSessionId)
                .build();

        sellerServiceBlockingStub.updateItemList(updateItemRequestForDeletion);
        assertTrue(itemRepository.findBySeller(sessionStore.getUser(UUID.fromString(sellerSessionId))).isEmpty());
    }


}
