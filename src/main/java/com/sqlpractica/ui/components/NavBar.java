package com.sqlpractica.ui.components;

import com.sqlpractica.ui.Theme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Barra de navegación horizontal con 5 botones que ocupan la franja
 * central de la app. El botón activo queda pintado con el color de acento
 * y el resto quedan como texto gris.
 */
public class NavBar extends JPanel {

    private final Map<String, NavItem> items = new LinkedHashMap<>();
    private String currentKey;
    private final Consumer<String> onSelect;

    public NavBar(Consumer<String> onSelect) {
        this.onSelect = onSelect;
        setBackground(Theme.BG_HEADER);
        setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 1, 0, Theme.BORDER_LIGHT),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        setLayout(new GridLayout(1, 5, 4, 0));
    }

    public void addTab(String key, String label) {
        NavItem item = new NavItem(key, label);
        items.put(key, item);
        add(item);
    }

    public void select(String key) {
        if (!items.containsKey(key)) return;
        currentKey = key;
        for (Map.Entry<String, NavItem> e : items.entrySet()) {
            e.getValue().setSelected(e.getKey().equals(key));
        }
        if (onSelect != null) onSelect.accept(key);
    }

    public String getCurrentKey() { return currentKey; }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    /** Botón individual, dibujado como una "pill". */
    private class NavItem extends JPanel {
        private final JLabel label;
        private boolean selected;

        NavItem(String key, String text) {
            setOpaque(true);
            setBackground(Theme.BG_HEADER);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setLayout(new GridLayout(1, 1));

            label = new JLabel(text, SwingConstants.CENTER);
            label.setFont(Theme.FONT_NAV);
            label.setForeground(Theme.TEXT_SECONDARY);
            label.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
            add(label);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { NavBar.this.select(key); }
            });
        }

        void setSelected(boolean s) {
            this.selected = s;
            label.setForeground(s ? Theme.TEXT_ON_ACCENT : Theme.TEXT_SECONDARY);
            setBackground(s ? Theme.ACCENT : Theme.BG_HEADER);
            repaint();
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            if (selected) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                try {
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } finally {
                    g2.dispose();
                }
            } else {
                super.paintComponent(g);
            }
        }
    }
}
