package com.sqlpractica.ui.components;

import com.sqlpractica.ui.Theme;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Botón plano, redondeado, con hover y desactivado. No depende del look-and-feel
 * del sistema, así se ve igual en Windows / Mac / Linux.
 */
public class StyledButton extends JButton {

    private final Color baseColor;
    private final Color hoverColor;
    private final Color textColor;
    private boolean hovering = false;

    public StyledButton(String text, Color baseColor, Color hoverColor, Color textColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.textColor = textColor;

        setFont(Theme.FONT_BUTTON);
        setForeground(textColor);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovering = true;  repaint(); }
            @Override public void mouseExited (MouseEvent e) { hovering = false; repaint(); }
        });
    }
}
