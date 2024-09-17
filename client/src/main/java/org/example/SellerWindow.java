/*
 * Created by JFormDesigner on Sat Sep 14 21:44:46 EDT 2024
 */

package org.example;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author MIRIN
 */
public class SellerWindow extends JFrame {
    public SellerWindow() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Liza Sorokina
        scrollPane1 = new JScrollPane();
        itemList = new JTable();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "9dlu, $lcgap, default",
            "2*(default, $lgap), default"));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(itemList);
        }
        contentPane.add(scrollPane1, CC.xy(3, 1));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Liza Sorokina
    private JScrollPane scrollPane1;
    private JTable itemList;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
