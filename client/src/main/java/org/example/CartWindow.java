/*
 * Created by JFormDesigner on Sat Sep 14 21:44:46 EDT 2024
 */

package org.example;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.example.CartTableModel.DELETED_COL;

/**
 * @author MIRIN
 */
public class CartWindow extends JFrame {
    private CartController cartController;

    public CartWindow(CartController cartController, CartTableModel model) {
        initComponents();
        this.cartController = cartController;
        itemList.setModel(model);
        // Set checkbox renderer and editor for the 'Deleted' column
        itemList.getColumnModel().getColumn(DELETED_COL).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        itemList.getColumnModel().getColumn(DELETED_COL).setCellRenderer(itemList.getDefaultRenderer(Boolean.class));

    }

    private void addNewItem(ActionEvent e) {
        // TODO add your code here
    }

    private void saveItemList(ActionEvent e) {
        // TODO add your code here
    }

    private void delete(ActionEvent e) {
        cartController.delete();

    }

    private void checkout(ActionEvent e) {
       cartController.initiateCheckout();
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - elizaveta sorokina
        scrollPane1 = new JScrollPane();
        itemList = new JTable();
        deleteButton = new JButton();
        checkoutButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "9dlu, $lcgap, default, $lcgap, 64dlu, $lcgap, 108dlu, $lcgap, 11dlu",
            "2*(default, $lgap), default"));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(itemList);
        }
        contentPane.add(scrollPane1, CC.xywh(3, 1, 5, 1));

        //---- deleteButton ----
        deleteButton.setText("Delete Item(s)");
        deleteButton.addActionListener(e -> {
			addNewItem(e);
			delete(e);
		});
        contentPane.add(deleteButton, CC.xywh(3, 3, 3, 1));

        //---- checkoutButton ----
        checkoutButton.setText("Checkout");
        checkoutButton.addActionListener(e -> {
			saveItemList(e);
			checkout(e);
		});
        contentPane.add(checkoutButton, CC.xy(7, 3));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - elizaveta sorokina
    private JScrollPane scrollPane1;
    private JTable itemList;
    private JButton deleteButton;
    private JButton checkoutButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
