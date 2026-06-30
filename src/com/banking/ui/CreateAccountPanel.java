package com.banking.ui;

import com.banking.exception.InvalidAmountException;
import com.banking.model.Customer;
import com.banking.service.BankService;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for creating a new customer account.
 * Collects customer details, account type, and initial deposit.
 */
public class CreateAccountPanel extends JPanel {
    private BankService bankService;
    private JTextField nameField, phoneField, emailField, addressField, amountField;
    private JComboBox<String> accountTypeBox;

    public CreateAccountPanel(BankService bankService) {
        this.bankService = bankService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Create New Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);
        gbc.gridwidth = 1;

        // Form fields
        nameField    = addField("Customer Name:", 1, gbc);
        phoneField   = addField("Phone:",         2, gbc);
        emailField   = addField("Email:",         3, gbc);
        addressField = addField("Address:",       4, gbc);
        amountField  = addField("Initial Deposit:", 5, gbc);

        // Account type dropdown
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Account Type:"), gbc);
        accountTypeBox = new JComboBox<>(new String[]{"SAVINGS", "CURRENT"});
        gbc.gridx = 1;
        add(accountTypeBox, gbc);

        // Submit button
        JButton createBtn = new JButton("Create Account");
        // Default native styling
        createBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        createBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        add(createBtn, gbc);

        createBtn.addActionListener(e -> handleCreateAccount());
    }

    private JTextField addField(String label, int row,
                                GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel(label), gbc);
        JTextField field = new JTextField(20);
        gbc.gridx = 1;
        add(field, gbc);
        return field;
    }

    /** Handles the Create Account button click */
    private void handleCreateAccount() {
        try {
            String name    = nameField.getText().trim();
            String phone   = phoneField.getText().trim();
            String email   = emailField.getText().trim();
            String address = addressField.getText().trim();
            String type    = (String) accountTypeBox.getSelectedItem();

            // Basic input validation
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Name and Phone are required fields.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double initialDeposit = Double.parseDouble(
                amountField.getText().trim());

            Customer customer = new Customer(0, name, phone, email, address);
            String accountNumber = bankService.createAccount(
                customer, type, initialDeposit);

            JOptionPane.showMessageDialog(this,
                "Account created successfully!\nAccount Number: "
                    + accountNumber,
                "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields after success
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
            addressField.setText("");
            amountField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid number for initial deposit.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidAmountException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Invalid Amount", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
