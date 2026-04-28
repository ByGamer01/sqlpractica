package com.sqlpractica.ui.components;

import com.sqlpractica.ui.Theme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;

/**
 * JTextField con fondo blanco, borde gris y altura cómoda.
 */
public class StyledTextField extends JTextField {

    public StyledTextField() {
        setFont(Theme.FONT_INPUT);
        setForeground(Theme.TEXT_PRIMARY);
        setBackground(Theme.BG_CARD);
        setOpaque(true);
        setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER_INPUT, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, 32);
    }
}
