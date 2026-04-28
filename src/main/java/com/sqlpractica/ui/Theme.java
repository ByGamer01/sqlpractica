package com.sqlpractica.ui;

import java.awt.Color;
import java.awt.Font;

/**
 * Constantes visuales de la aplicación. Centralizar colores y fuentes
 * permite cambiar el "look" sin tener que tocar cada panel.
 */
public final class Theme {

    private Theme() {}

    public static final int WIDTH  = 460;
    public static final int HEIGHT = 820;
    public static final int CORNER_RADIUS = 28;

    public static final Color BG_DARK     = new Color(0x1a1a2e);
    public static final Color BG_APP      = new Color(0xf8fafc);
    public static final Color BG_CARD     = new Color(0xffffff);
    public static final Color BG_HEADER   = new Color(0xffffff);
    public static final Color BG_TABLE_HEADER = new Color(0xf1f5f9);
    public static final Color BG_TABLE_ALT    = new Color(0xfafbfc);
    public static final Color BG_SELECTED     = new Color(0xeef2ff);

    public static final Color TEXT_PRIMARY   = new Color(0x0f172a);
    public static final Color TEXT_SECONDARY = new Color(0x64748b);
    public static final Color TEXT_MUTED     = new Color(0x94a3b8);
    public static final Color TEXT_ON_ACCENT = Color.WHITE;

    public static final Color BORDER_LIGHT = new Color(0xe2e8f0);
    public static final Color BORDER_INPUT = new Color(0xcbd5e1);

    public static final Color ACCENT       = new Color(0x6366f1);
    public static final Color ACCENT_HOVER = new Color(0x4f46e5);
    public static final Color INFO         = new Color(0x0ea5e9);
    public static final Color INFO_HOVER   = new Color(0x0284c7);
    public static final Color DANGER       = new Color(0xef4444);
    public static final Color DANGER_HOVER = new Color(0xdc2626);

    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.PLAIN, 20);
    public static final Font FONT_SUBTITLE   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_INPUT      = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON     = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_NAV        = new Font("Segoe UI", Font.BOLD, 10);
    public static final Font FONT_TABLE      = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TABLE_HEAD = new Font("Segoe UI", Font.BOLD, 11);
}
