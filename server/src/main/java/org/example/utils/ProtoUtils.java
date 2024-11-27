package org.example.utils;

import org.example.data.model.CartEntry;
import org.example.data.model.Item;
import org.example.data.model.OrderItem;
import org.example.grpc.Cart;
import org.example.grpc.Order;
import org.example.service.SessionStore;

import java.util.UUID;

public final class ProtoUtils {
    private ProtoUtils(){

    }

    public static org.example.grpc.Item toProto(Item item) {
        return org.example.grpc.Item.newBuilder()
                .setId(item.getId().toString())
                .setName(item.getName())
                .setQuantity(item.getQuantity())
                .setInvoicePriceCents(item.getInvoicePriceCents())
                .setSellPriceCents(item.getSellPriceCents())
                .build();
    }

    public static Item toDomain(org.example.grpc.Item protoItem, UUID sessionId, SessionStore sessionStore) {
        var item = new Item();
        if(protoItem.getId() == null || protoItem.getId().isEmpty()) {
            item.setId(UUID.randomUUID());
        } else {
            item.setId(UUID.fromString(protoItem.getId()));
        }

        item.setName(protoItem.getName());
        item.setQuantity(protoItem.getQuantity());
        item.setInvoicePriceCents(protoItem.getInvoicePriceCents());
        item.setSellPriceCents(protoItem.getSellPriceCents());
        item.setSeller(sessionStore.getUser(sessionId));
        return item;
    }

    public static Cart toProto(org.example.data.model.Cart domainCart){
        var cartBuilder = Cart.newBuilder();
        for (CartEntry cartEntry : domainCart.getCartEntries()) {
            cartBuilder.addCartEntries(org.example.grpc.CartEntry
                    .newBuilder()
                    .setItemId(cartEntry.getItemId().toString())
                    .setItemCount(cartEntry.getItemCount())
                    .setItemName(cartEntry.getItemName())
                    .setSellPrice(cartEntry.getSellPrice())
            );
        }
        return cartBuilder.build();
    }
    public static org.example.grpc.PurchasedItem toProto(OrderItem item) {
        return org.example.grpc.PurchasedItem.newBuilder()
                .setItemCount(item.getItemQuantity())
                .setItemName(item.getItem().getName())
                .setPricePerUnit(item.getItem().getSellPriceCents())
                .build();

    }

    public static Order toProto(org.example.data.model.Order order){
        var orderBuilder = Order.newBuilder();
        for(OrderItem item: order.getOrderItems()){
            orderBuilder.addItem(toProto(item));
        }
        return orderBuilder.build();
    }
}
