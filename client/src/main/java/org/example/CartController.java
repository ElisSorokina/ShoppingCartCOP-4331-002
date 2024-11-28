package org.example;

import io.grpc.ManagedChannel;
import org.example.grpc.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static org.example.CartTableModel.ID_COL;
import static org.example.CartTableModel.QUANTITY_COL;

public class CartController {
    private BuyerServiceGrpc.BuyerServiceBlockingStub buyerServiceBlockingStub;
    private final ManagedChannel channel;
    private CartWindow cartWindow;
    private String sessionId;
    private CartTableModel model;


    public CartController(ManagedChannel channel, String sessionId) {
        this.channel = channel;
        this.sessionId = sessionId;
        buyerServiceBlockingStub = BuyerServiceGrpc.newBlockingStub(channel);
    }

    public void openCart(){
        model = new CartTableModel(getCartItemList());
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getColumn() == QUANTITY_COL) {
                    buyerServiceBlockingStub.updateCart(UpdateCartRequest.newBuilder()
                                    .setItemCount((Integer)model.getValueAt(e.getFirstRow(), QUANTITY_COL))
                                    .setSessionId(sessionId)
                                    .setItemId(model.getValueAt(e.getFirstRow(), ID_COL).toString())
                            .build());
                }
            }
        });
        cartWindow = new CartWindow(this, model);
        cartWindow.setVisible(true);
    }

    private List<CartEntry> getCartItemList() {
        GetCartItemListResponse cartItemList = buyerServiceBlockingStub.getCartItemList(GetCartItemListRequest.newBuilder().setSessionId(sessionId).build());
        return cartItemList.getCart().getCartEntriesList();
    }

    public void delete(){

        var deletedList = model.getDeleted();
        for (CartEntry cartEntry: deletedList) {
            buyerServiceBlockingStub.deleteItemFromCart(DeleteItemRequest.newBuilder().setSessionId(sessionId).setItemId(cartEntry.getItemId()).build());
        }
        model.setCartEntries(getCartItemList());
        model.fireTableDataChanged();
    }

    public void initiateCheckout() {
        CheckoutController checkoutController = new CheckoutController(channel, sessionId);
        checkoutController.openCheckoutWindow(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                model.setCartEntries(getCartItemList());
                model.fireTableDataChanged();
            }
        });
    }

}
