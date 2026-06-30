package com.banking.ui;

import com.banking.exception.AccountNotFoundException;
import com.banking.service.BankService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Panel for checking the balance of an account.
 * Displays customer name, account type, and formatted balance.
 */
public class BalanceEnquiryPanel extends JPanel {
    private BankService bankService;
    private JTextField accountField;
    private JTextArea resultArea;

    public BalanceEnquiryPanel(BankService bankService) {
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
        JLabel title = new JLabel("Balance Enquiry");
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

        // Button
        JButton checkBtn = new JButton("Check Balance");
        ThemeUtils.stylePrimaryButton(checkBtn);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(checkBtn, gbc);

        // Result display area
        resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createTitledBorder("Account Details"));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(resultArea), gbc);

        checkBtn.addActionListener(e -> handleCheckBalance());
    }

    private void handleCheckBalance() {
        try {
            String accountNumber = accountField.getText().trim();
            if (accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter an account number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Object> info = bankService.getBalance(accountNumber);
            String name    = (String) info.get("customerName");
            String type    = (String) info.get("accountType");
            double balance = (Double) info.get("balance");

            resultArea.setText(String.format(
                "  Customer Name : %s\n"
              + "  Account Type  : %s\n"
              + "  Balance       : %.2f",
                name, type, balance));

        } catch (AccountNotFoundException ex) {
            resultArea.setText("");
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Account Not Found", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            resultArea.setText("");
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
