package org.example.service;

import jakarta.transaction.Transactional;
import org.example.data.model.*;
import org.example.data.repository.ItemRepository;
import org.example.data.repository.OrderItemRepository;
import org.example.grpc.GetProfitReportResponse;
import org.example.grpc.ItemProfit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SellerService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SessionStore sessionStore;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Item> getItemList(UUID sessionId) {
        return itemRepository.findBySeller(sessionStore.getUser(sessionId));
    }

    @Transactional
    public void updateItemList(List<Item> itemList, List<UUID> deletedIds) {
        itemRepository.deleteAllById(deletedIds);
        itemRepository.saveAll(itemList);
    }

    @Transactional
    public GetProfitReportResponse getProfitReport(UUID sessionId) {
        var seller = sessionStore.getUser(sessionId);
        if (seller == null) {
            throw new IllegalArgumentException("Invalid session or seller not found");
        }

        // Fetch sold items and calculate profits
        var soldItems = orderItemRepository.findOrderItemsBySeller(seller);
        int totalProfit = 0;
        var builder = GetProfitReportResponse.newBuilder();
        for (OrderItem orderItem : soldItems) {
            int profit = (orderItem.getItem().getSellPriceCents() - orderItem.getItem().getInvoicePriceCents()) * orderItem.getItemQuantity();
            totalProfit += profit;

            builder.addItemProfits(ItemProfit.newBuilder().setProfit(profit).setItemName(orderItem.getItem().getName()).build());
        }
        builder.setTotalProfit(totalProfit);

        return builder.build();
    }

}
