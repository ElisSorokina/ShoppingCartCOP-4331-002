package org.example.utils;

import org.example.data.model.CartEntry;
import org.example.data.model.Item;
import org.example.data.model.OrderItem;
import org.example.grpc.Cart;
import org.example.grpc.Order;
import org.example.service.SessionStore;

import java.util.UUID;

/**
 * Utility class for converting between domain objects and Protocol Buffer objects.
 */
public final class ProtoUtils {

    /**
     * Converts an Item domain object to its corresponding Protocol Buffer representation.
     *
     * @param item the Item object to be converted.
     * @return the Protocol Buffer representation of the Item.
     */
    public static org.example.grpc.Item toProto(Item item) {
        return org.example.grpc.Item.newBuilder()
                .setId(item.getId().toString())
                .setName(item.getName())
                .setQuantity(item.getQuantity())
                .setInvoicePriceCents(item.getInvoicePriceCents())
                .setSellPriceCents(item.getSellPriceCents())
                .build();
    }

    /**
     * Converts an ItemProto Protocol Buffer object to its corresponding domain object.
     *
     * @param proto the ItemProto object to be converted.
     * @param seller the seller associated with the item.
     * @return the domain representation of the Item.
     */
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

    /**
     * Converts a Cart domain object to its corresponding Protocol Buffer representation.
     *
     * @param cart the Cart object to be converted.
     * @return the Protocol Buffer representation of the Cart.
     */
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

    /**
     * Converts an OrderItem domain object to its corresponding Protocol Buffer representation.
     *
     * @param item the OrderItem object to be converted.
     * @return the Protocol Buffer representation of the OrderItem.
     */
    public static org.example.grpc.PurchasedItem toProto(OrderItem item) {
        return org.example.grpc.PurchasedItem.newBuilder()
                .setItemCount(item.getItemQuantity())
                .setItemName(item.getItem().getName())
                .setPricePerUnit(item.getItem().getSellPriceCents())
                .build();

    }

    /**
     * Converts an Order domain object to its corresponding Protocol Buffer representation.
     *
     * @param order the Order object to be converted.
     * @return the Protocol Buffer representation of the Order.
     */
    public static Order toProto(org.example.data.model.Order order){
        var orderBuilder = Order.newBuilder();
        for(OrderItem item: order.getOrderItems()){
            orderBuilder.addItem(toProto(item));
        }
        return orderBuilder.build();
    }

    private ProtoUtils(){
        // empty
    }
}
