package org.example.utils;

import org.example.data.model.Item;
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
}
