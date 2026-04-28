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

        titleLabel = new JLabel("Empleados");
    }
}
