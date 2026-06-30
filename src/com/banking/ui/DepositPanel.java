package com.banking.ui;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InvalidAmountException;
import com.banking.service.BankService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Panel for depositing money into an existing account.
 */
public class DepositPanel extends JPanel {
    private BankService bankService;
    private JTextField accountField, amountField;

    public DepositPanel(BankService bankService) {
        this.bankService = bankService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Deposit Money");
        ThemeUtils.styleTitle(title);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);
        gbc.gridwidth = 1;

        // Account number
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel accLbl = new JLabel("Account Number:");
        ThemeUtils.styleLabel(accLbl);
        add(accLbl, gbc);
        accountField = new JTextField(20);
        ThemeUtils.styleTextField(accountField);
        gbc.gridx = 1;
        add(accountField, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel amtLbl = new JLabel("Amount:");
        ThemeUtils.styleLabel(amtLbl);
        add(amtLbl, gbc);
        amountField = new JTextField(20);
        ThemeUtils.styleTextField(amountField);
        gbc.gridx = 1;
        add(amountField, gbc);

        // Button
        JButton depositBtn = new JButton("Deposit");
        ThemeUtils.styleSuccessButton(depositBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(depositBtn, gbc);

        depositBtn.addActionListener(e -> handleDeposit());
    }

    private void handleDeposit() {
        try {
            String accountNumber = accountField.getText().trim();
            if (accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter an account number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountField.getText().trim());
            bankService.deposit(accountNumber, amount);

            // Fetch updated balance for confirmation
            Map<String, Object> info = bankService.getBalance(accountNumber);
            double newBalance = (Double) info.get("balance");

            JOptionPane.showMessageDialog(this,
                String.format("Deposit successful!\nNew Balance: %.2f",
                              newBalance),
                "Success", JOptionPane.INFORMATION_MESSAGE);

            amountField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid number.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidAmountException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Invalid Amount", JOptionPane.ERROR_MESSAGE);
        } catch (AccountNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Account Not Found", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
