package com.sqlpractica.ui.components;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** Cabecera común para las secciones internas. */
public class SectionHeader extends JPanel {

  public SectionHeader(String title, Runnable onBack) {
    super(new BorderLayout(12, 0));
    setBackground(UiStyles.BACKGROUND);
    setBorder(new EmptyBorder(20, 24, 12, 24));

    JLabel label = new JLabel(title);
    label.setForeground(UiStyles.TEXT);
    label.setFont(label.getFont().deriveFont(Font.BOLD, 24f));

    JButton back = UiStyles.actionButton("Volver", UiStyles.SECONDARY);
    back.addActionListener(e -> onBack.run());

    add(label, BorderLayout.WEST);
    add(back, BorderLayout.EAST);
  }
}
