package com.banking.ui;

import com.banking.service.BankService;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window with CardLayout-based navigation.
 * Provides buttons to switch between the five MVP operation panels.
 */
public class MainFrame extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private BankService bankService;

    public MainFrame(BankService bankService) {
        this.bankService = bankService;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Online Banking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---- Navigation panel (left sidebar) ----
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(6, 1, 5, 5));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navPanel.setPreferredSize(new Dimension(200, 0));
        navPanel.setBackground(new Color(45, 52, 54));

        String[] labels = {
            "Create Account", "Deposit", "Withdraw",
            "Balance Enquiry", "Transaction History"
        };
        String[] cardNames = {
            "CREATE", "DEPOSIT", "WITHDRAW", "BALANCE", "HISTORY"
        };

        // Title label
        JLabel titleLabel = new JLabel("Banking System", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        navPanel.add(titleLabel);

        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            final String name = cardNames[i];
            btn.addActionListener(e -> cardLayout.show(cardPanel, name));
            navPanel.add(btn);
        }

        // ---- Content panel (CardLayout) ----
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new CreateAccountPanel(bankService),       "CREATE");
        cardPanel.add(new DepositPanel(bankService),             "DEPOSIT");
        cardPanel.add(new WithdrawPanel(bankService),            "WITHDRAW");
        cardPanel.add(new BalanceEnquiryPanel(bankService),      "BALANCE");
        cardPanel.add(new TransactionHistoryPanel(bankService),  "HISTORY");

        // ---- Assemble ----
        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }
}
