/*
 * Created by JFormDesigner on Sat Sep 14 21:44:46 EDT 2024
 */

package org.example;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author MIRIN
 */
public class BuyerWindow extends JFrame {
    private BuyerController buyerController;

    public BuyerWindow(BuyerController buyerController, BuyerItemTableModel model) {
        initComponents();
        this.buyerController = buyerController;
        itemList.setModel(model);
        // Set checkbox renderer and editor for the 'Deleted' column
        itemList.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        itemList.getColumnModel().getColumn(3).setCellRenderer(itemList.getDefaultRenderer(Boolean.class));

    }





    private void addToCart(ActionEvent e) {
        buyerController.addToCart();

    }

    private void goToCart(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - elizaveta sorokina
        scrollPane1 = new JScrollPane();
        itemList = new JTable();
        addToCartButton = new JButton();
        goToCartButton = new JButton();

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

        //---- addToCartButton ----
        addToCartButton.setText("Add to Cart");
        addToCartButton.addActionListener(e -> addToCart(e));
        contentPane.add(addToCartButton, CC.xy(7, 3));

        //---- goToCartButton ----
        goToCartButton.setText("Go to Cart");
        goToCartButton.addActionListener(e -> goToCart(e));
        contentPane.add(goToCartButton, CC.xy(7, 5));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - elizaveta sorokina
    private JScrollPane scrollPane1;
    private JTable itemList;
    private JButton addToCartButton;
    private JButton goToCartButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
