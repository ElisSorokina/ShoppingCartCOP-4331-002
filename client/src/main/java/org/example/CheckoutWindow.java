package org.example;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import org.example.grpc.Card;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CheckoutWindow extends JFrame {
    private CheckoutController controller;

    public CheckoutWindow(CheckoutController controller) {
        this.controller = controller;
        initComponents();
    }

    private void confirmOrder(ActionEvent e) {
        String address = addressField.getText();
        String cardNumber = cardNumberField.getText();
        int expYear, expMonth, cvv;

        // Validate inputs
        try {
            expMonth = Integer.parseInt(expMonthField.getText());
            expYear = Integer.parseInt(expYearField.getText());
            cvv = Integer.parseInt(cvvField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input for expiration date or CVV.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ensure required fields are filled
        if (address.isEmpty() || cardNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate expMonth range
        if (expMonth < 1 || expMonth > 12) {
            JOptionPane.showMessageDialog(this, "Expiration month must be between 1 and 12.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controller.processCheckout(address, cardNumber, expYear, expMonth, cvv);
            JOptionPane.showMessageDialog(this, "Order confirmed! Thank you.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the window after successful checkout
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to process order. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void initComponents() {
        setTitle("Checkout");
        var contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
                "right:pref, 5px, fill:200px",
                "6*(pref, 5px), pref"));

        // Address Field
        contentPane.add(new JLabel("Address:"), CC.xy(1, 1));
        addressField = new JTextArea(3, 20);
        contentPane.add(new JScrollPane(addressField), CC.xy(3, 1));

        // Card Number Field
        contentPane.add(new JLabel("Card Number:"), CC.xy(1, 3));
        cardNumberField = new JTextField();
        contentPane.add(cardNumberField, CC.xy(3, 3));

        // Expiration Month
        contentPane.add(new JLabel("Exp Month (1-12):"), CC.xy(1, 5));
        expMonthField = new JTextField();
        contentPane.add(expMonthField, CC.xy(3, 5));

        // Expiration Year
        contentPane.add(new JLabel("Exp Year:"), CC.xy(1, 7));
        expYearField = new JTextField();
        contentPane.add(expYearField, CC.xy(3, 7));

        // CVV Field
        contentPane.add(new JLabel("CVV:"), CC.xy(1, 9));
        cvvField = new JTextField();
        contentPane.add(cvvField, CC.xy(3, 9));

        // Confirm Button
        confirmButton = new JButton("Confirm Order");
        confirmButton.addActionListener(this::confirmOrder);
        contentPane.add(confirmButton, CC.xy(3, 11));

        pack();
        setLocationRelativeTo(null);
    }

    // Fields
    private JTextArea addressField;
    private JTextField cardNumberField;
    private JTextField expMonthField;
    private JTextField expYearField;
    private JTextField cvvField;
    private JButton confirmButton;
}