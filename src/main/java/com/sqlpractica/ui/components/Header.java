package com.sqlpractica.ui.components;

import com.sqlpractica.ui.Theme;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Cabecera fija en la parte superior de la app: una "ceja" "SQL PRACTICA"
 * y el título de la sección activa justo debajo.
 */
public class Header extends JPanel {

    private final JLabel titleLabel;

    public Header() {
        setBackground(Theme.BG_HEADER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT),
                BorderFactory.createEmptyBorder(18, 20, 14, 20)
        ));

        JLabel eyebrow = new JLabel("SQL PRACTICA");
        eyebrow.setFont(Theme.FONT_SUBTITLE);
        eyebrow.setForeground(Theme.TEXT_MUTED);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(eyebrow);

        titleLabel = new JLabel("Empleados");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        add(titleLabel);
    }
}
