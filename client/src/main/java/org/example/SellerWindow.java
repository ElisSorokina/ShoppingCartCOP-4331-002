/*
 * Created by JFormDesigner on Sat Sep 14 21:44:46 EDT 2024
 */

package org.example;

import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author MIRIN
 */
public class SellerWindow extends JFrame {
    private SellerController sellerController;

    public SellerWindow(SellerController sellerController, SellerItemTableModel model) {
        initComponents();
        this.sellerController = sellerController;
        itemList.setModel(model);
        // Set checkbox renderer and editor for the 'Deleted' column
        itemList.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        itemList.getColumnModel().getColumn(5).setCellRenderer(itemList.getDefaultRenderer(Boolean.class));

    }

    private void saveItemList(ActionEvent event) {
        sellerController.saveItemList();
    }

    private void addNewItem(ActionEvent e) {
        sellerController.addNewItem();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Liza Sorokina
        scrollPane1 = new JScrollPane();
        itemList = new JTable();
        addNewItemButton = new JButton();
        saveButton = new JButton();

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

        //---- addNewItemButton ----
        addNewItemButton.setText("Add New Item");
        addNewItemButton.addActionListener(e -> addNewItem(e));
        contentPane.add(addNewItemButton, CC.xywh(3, 3, 3, 1));

        //---- saveButton ----
        saveButton.setText("Save");
        saveButton.addActionListener(e -> saveItemList(e));
        contentPane.add(saveButton, CC.xy(7, 3));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Liza Sorokina
    private JScrollPane scrollPane1;
    private JTable itemList;
    private JButton addNewItemButton;
    private JButton saveButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
