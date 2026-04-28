package com.sqlpractica.ui.components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * JPanel con esquinas redondeadas. Hace el panel transparente
 * y pinta el fondo a mano para que se vea el radio.
 */
public class RoundedPanel extends JPanel {

    private final int radius;
    private Color background;

    public RoundedPanel(int radius, Color background) {
        this.radius = radius;
        this.background = background;
        setOpaque(false);
    }

    @Override
    public void setBackground(Color bg) {
        this.background = bg;
        super.setBackground(bg);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
