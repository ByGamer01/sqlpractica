package com.sqlpractica.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** Estilos compartidos para mantener la UI consistente y sencilla. */
public final class UiStyles {

  public static final Color BACKGROUND = new Color(245, 247, 251);
  public static final Color CARD = Color.WHITE;
  public static final Color TEXT = new Color(31, 41, 55);
  public static final Color MUTED = new Color(107, 114, 128);
  public static final Color PRIMARY = new Color(37, 99, 235);
  public static final Color SUCCESS = new Color(22, 163, 74);
  public static final Color WARNING = new Color(217, 119, 6);
  public static final Color DANGER = new Color(220, 38, 38);
  public static final Color SECONDARY = new Color(75, 85, 99);
  public static final Color BORDER = new Color(229, 231, 235);

  private UiStyles() {}

  public static JPanel basePanel() {
    JPanel panel = new JPanel();
    panel.setBackground(BACKGROUND);
    panel.setBorder(new EmptyBorder(24, 28, 24, 28));
    return panel;
  }

  public static JPanel contentPanel() {
    JPanel panel = new JPanel(new java.awt.BorderLayout(14, 14));
    panel.setBackground(BACKGROUND);
    panel.setBorder(new EmptyBorder(0, 24, 24, 24));
    return panel;
  }

  public static JPanel formCard(int rows) {
    JPanel panel = new JPanel(new GridLayout(rows, 2, 10, 10));
    panel.setBackground(CARD);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER),
        new EmptyBorder(16, 16, 16, 16)));
    return panel;
  }

  public static JPanel buttonRow() {
    JPanel panel = new JPanel(new GridLayout(1, 4, 8, 8));
    panel.setOpaque(false);
    return panel;
  }

  public static JButton actionButton(String text, Color color) {
    JButton button = new JButton(text);
    button.setBackground(color);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorder(new EmptyBorder(8, 12, 8, 12));
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.setFont(button.getFont().deriveFont(Font.BOLD));
    return button;
  }

  public static void styleTable(JTable table) {
    table.setRowHeight(28);
    table.setShowGrid(false);
    table.setFillsViewportHeight(true);
    table.getTableHeader().setReorderingAllowed(false);
    table.getTableHeader().setBackground(new Color(239, 246, 255));
    table.getTableHeader().setForeground(TEXT);
    table.getTableHeader().setFont(
        table.getTableHeader().getFont().deriveFont(Font.BOLD));
  }

  public static JScrollPane tableScroll(JTable table) {
    JScrollPane scroll = new JScrollPane(table);
    scroll.setBorder(BorderFactory.createLineBorder(BORDER));
    scroll.getViewport().setBackground(CARD);
    return scroll;
  }

  public static void styleTextField(JTextField field) {
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER), new EmptyBorder(6, 8, 6, 8)));
  }
}
