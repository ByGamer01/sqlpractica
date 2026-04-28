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

    public static StyledButton primary(String text) {
        return new StyledButton(text, Theme.ACCENT, Theme.ACCENT_HOVER, Theme.TEXT_ON_ACCENT);
    }

    public static StyledButton info(String text) {
        return new StyledButton(text, Theme.INFO, Theme.INFO_HOVER, Theme.TEXT_ON_ACCENT);
    }

    public static StyledButton danger(String text) {
        return new StyledButton(text, Theme.DANGER, Theme.DANGER_HOVER, Theme.TEXT_ON_ACCENT);
    }

    public static StyledButton secondary(String text) {
        return new StyledButton(text, Theme.BG_CARD, Theme.BG_TABLE_HEADER, Theme.TEXT_SECONDARY);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, Math.max(34, d.height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            Color fill;
            if (!isEnabled()) {
                fill = new Color(baseColor.getRed(), baseColor.getGreen(),
                                 baseColor.getBlue(), 110);
            } else if (getModel().isPressed()) {
                fill = hoverColor.darker();
            } else if (hovering) {
                fill = hoverColor;
            } else {
                fill = baseColor;
            }

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            if (baseColor.equals(Theme.BG_CARD)) {
                g2.setColor(Theme.BORDER_INPUT);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
