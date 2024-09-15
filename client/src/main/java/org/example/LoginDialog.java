/*
 * Created by JFormDesigner on Thu Sep 05 21:53:46 EDT 2024
 */

package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author MIRIN
 */
public class LoginDialog extends JDialog {
    private final MainController controller;

    public LoginDialog(MainController controller) {
        super(((Window) null));
        this.controller = controller;
        initComponents();
    }

    private void signUp(ActionEvent e) {
        controller.signUp();
    }

    private void submit(ActionEvent e) {
        String sessionId = null;
        try {
            sessionId = controller.login(loginField.getText(), new String(passwordField.getPassword()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid credentials, try again", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Liza Sorokina
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        newUserLabel = new JLabel();
        signUpButton = new JButton();
        loginLbl = new JLabel();
        loginField = new JTextField();
        passwordLbl = new JLabel();
        passwordField = new JPasswordField();
        submitButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
            dialogPane.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
            ( 0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder. CENTER, javax. swing. border
            . TitledBorder. BOTTOM, new java .awt .Font ("Dialo\u0067" ,java .awt .Font .BOLD ,12 ), java. awt
            . Color. red) ,dialogPane. getBorder( )) ); dialogPane. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
            propertyChange (java .beans .PropertyChangeEvent e) {if ("borde\u0072" .equals (e .getPropertyName () )) throw new RuntimeException( )
            ; }} );
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "6*(default, $lcgap), default",
                    "5*(default, $lgap), default"));

                //---- newUserLabel ----
                newUserLabel.setText("New user?");
                contentPanel.add(newUserLabel, CC.xy(1, 1));

                //---- signUpButton ----
                signUpButton.setText("Sign Up");
                signUpButton.addActionListener(e -> signUp(e));
                contentPanel.add(signUpButton, CC.xy(5, 1));

                //---- loginLbl ----
                loginLbl.setText("Login");
                contentPanel.add(loginLbl, CC.xy(1, 3));
                contentPanel.add(loginField, CC.xywh(1, 5, 5, 1));

                //---- passwordLbl ----
                passwordLbl.setText("Password");
                contentPanel.add(passwordLbl, CC.xy(1, 7));
                contentPanel.add(passwordField, CC.xywh(1, 9, 5, 1));

                //---- submitButton ----
                submitButton.setText("Submit");
                submitButton.addActionListener(e -> submit(e));
                contentPanel.add(submitButton, CC.xywh(1, 11, 5, 1));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
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
    private JLabel newUserLabel;
    private JButton signUpButton;
    private JLabel loginLbl;
    private JTextField loginField;
    private JLabel passwordLbl;
    private JPasswordField passwordField;
    private JButton submitButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
