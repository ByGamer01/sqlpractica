package com.sqlpractica.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/** Botón grande para la pantalla inicial. */
public class MenuButton extends JButton {

  private final Color color;

  public MenuButton(String text, Color color) {
    super(text);
    this.color = color;
    setForeground(Color.WHITE);
    setFont(getFont().deriveFont(Font.BOLD, 20f));
    setFocusPainted(false);
    setContentAreaFilled(false);
    setBorder(new EmptyBorder(18, 24, 18, 24));
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    setPreferredSize(new Dimension(320, 66));
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    Graphics2D g2 = (Graphics2D)graphics.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(color);
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
    g2.dispose();
    super.paintComponent(graphics);
  }
}
