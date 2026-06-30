package com.banking.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Utility class to apply a consistent, modern, and interactive 
 * styling theme across all Swing components.
 */
public class ThemeUtils {
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    // Modern Flat UI Colors
    public static final Color PRIMARY_COLOR = new Color(9, 132, 227); // Blue
    public static final Color PRIMARY_HOVER = new Color(116, 185, 255);
    
    public static final Color SUCCESS_COLOR = new Color(0, 184, 148); // Green
    public static final Color SUCCESS_HOVER = new Color(85, 239, 196);
    
    public static final Color DANGER_COLOR = new Color(214, 48, 49);  // Red
    public static final Color DANGER_HOVER = new Color(255, 118, 117);

    public static final Color NAV_BG = new Color(45, 52, 54);         // Dark Gray
    public static final Color NAV_BTN_HOVER = new Color(99, 110, 114);

    public static void stylePrimaryButton(JButton btn) {
        styleButton(btn, PRIMARY_COLOR, PRIMARY_HOVER, Color.WHITE);
    }
    
    public static void styleSuccessButton(JButton btn) {
        styleButton(btn, SUCCESS_COLOR, SUCCESS_HOVER, Color.WHITE);
    }

    public static void styleDangerButton(JButton btn) {
        styleButton(btn, DANGER_COLOR, DANGER_HOVER, Color.WHITE);
    }

    public static void styleNavButton(JButton btn) {
        styleButton(btn, NAV_BG, NAV_BTN_HOVER, Color.WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

    private static void styleButton(JButton btn, Color bg, Color hover, Color fg) {
        btn.setFont(MAIN_FONT);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        // Ensure background is painted even on Windows L&F
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Interactive hover effects
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
    }

    public static void styleTextField(JTextField field) {
        field.setFont(MAIN_FONT);
        // Add pleasant padding inside the text box
        Border line = BorderFactory.createLineBorder(new Color(178, 190, 195), 1);
        Border empty = BorderFactory.createEmptyBorder(8, 10, 8, 10);
        field.setBorder(BorderFactory.createCompoundBorder(line, empty));
    }

    public static void styleLabel(JLabel label) {
        label.setFont(LABEL_FONT);
    }
    
    public static void styleTitle(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(new Color(45, 52, 54));
    }
}
