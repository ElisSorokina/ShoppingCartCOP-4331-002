package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.example.grpc.Role;
import org.example.grpc.SignUpRequest;
/*
 * Created by JFormDesigner on Fri Sep 06 20:46:06 EDT 2024
 */


/**
 * @author MIRIN
 */
public class RegistrationForm extends JDialog {
    private MainController controller;

    public RegistrationForm(MainController controller) {
        super((Window) null);
        this.controller = controller;
        initComponents();
        roleComboBox.addItem(Role.SELLER);
        roleComboBox.addItem(Role.CUSTOMER);

    }

    private void completeSignUp(ActionEvent e) {
        controller.completeSignUp(SignUpRequest.newBuilder()
                        .setLogin(loginField.getText())
                        .setPassword(new String(passwordField.getPassword()))
                        .setRole(Role.valueOf(roleComboBox.getSelectedItem().toString()))
                .build());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Liza Sorokina
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        roleLabel = new JLabel();
        roleComboBox = new JComboBox();
        loginLabel = new JLabel();
        loginField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
            dialogPane.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder
            ( 0, 0 ,0 , 0) ,  "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border
            .TitledBorder . BOTTOM, new java. awt .Font ( "Dialo\u0067", java .awt . Font. BOLD ,12 ) ,java . awt
            . Color .red ) ,dialogPane. getBorder () ) ); dialogPane. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void
            propertyChange (java . beans. PropertyChangeEvent e) { if( "borde\u0072" .equals ( e. getPropertyName () ) )throw new RuntimeException( )
            ;} } );
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "default, $lcgap, 86dlu, $lcgap, 23dlu, $lcgap, default",
                    "7*(default, $lgap), default"));

                //---- roleLabel ----
                roleLabel.setText("Your Role");
                contentPanel.add(roleLabel, CC.xy(3, 3));
                contentPanel.add(roleComboBox, CC.xywh(3, 5, 5, 1));

                //---- loginLabel ----
                loginLabel.setText("Login");
                contentPanel.add(loginLabel, CC.xy(3, 7));
                contentPanel.add(loginField, CC.xywh(3, 9, 5, 1));

                //---- passwordLabel ----
                passwordLabel.setText("Password");
                contentPanel.add(passwordLabel, CC.xy(3, 11));
                contentPanel.add(passwordField, CC.xywh(3, 13, 5, 1));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("5dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                    "$glue, $button, $rgap, $button",
                    "pref"));

                //---- okButton ----
                okButton.setText("Submit");
                okButton.addActionListener(e -> completeSignUp(e));
                buttonBar.add(okButton, CC.xy(4, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Liza Sorokina
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel roleLabel;
    private JComboBox roleComboBox;
    private JLabel loginLabel;
    private JTextField loginField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
