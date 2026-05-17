package com.sqlpractica.ui;

import com.sqlpractica.ui.components.MenuButton;
import com.sqlpractica.ui.components.UiStyles;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Pantalla inicial con navegación tipo móvil. */
public class HomePanel extends JPanel {

  public HomePanel(Runnable abrirEmpleados, Runnable abrirPlazas,
                   Runnable abrirTiposPlaza, Runnable abrirOcupaciones,
                   Runnable abrirNominas) {
    super(new BorderLayout());
    setBackground(UiStyles.BACKGROUND);
    setBorder(javax.swing.BorderFactory.createEmptyBorder(42, 32, 42, 32));

    JLabel titulo = new JLabel("SQL Práctica", SwingConstants.CENTER);
    titulo.setForeground(UiStyles.TEXT);
    titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 32f));

    JLabel subtitulo =
        new JLabel("Elige un módulo para gestionar la base de datos",
                   SwingConstants.CENTER);
    subtitulo.setForeground(UiStyles.MUTED);
    subtitulo.setFont(subtitulo.getFont().deriveFont(15f));

    JPanel header = new JPanel(new GridLayout(2, 1, 0, 8));
    header.setOpaque(false);
    header.add(titulo);
    header.add(subtitulo);

    JPanel menu = new JPanel(new GridLayout(5, 1, 0, 14));
    menu.setOpaque(false);
    menu.add(button("Empleados", new Color(37, 99, 235), abrirEmpleados));
    menu.add(button("Plazas", new Color(14, 165, 233), abrirPlazas));
    menu.add(
        button("Tipos de plaza", new Color(139, 92, 246), abrirTiposPlaza));
    menu.add(button("Ocupaciones", new Color(245, 158, 11), abrirOcupaciones));
    menu.add(button("Nóminas", new Color(16, 185, 129), abrirNominas));

    JPanel menuWrapper = new JPanel(new GridBagLayout());
    menuWrapper.setOpaque(false);
    menuWrapper.add(menu);

    JPanel wrapper = new JPanel(new BorderLayout(0, 28));
    wrapper.setOpaque(false);
    wrapper.add(header, BorderLayout.NORTH);
    wrapper.add(menuWrapper, BorderLayout.CENTER);

    add(wrapper, BorderLayout.CENTER);
  }

  private MenuButton button(String text, Color color, Runnable action) {
    MenuButton button = new MenuButton(text, color);
    button.addActionListener(e -> action.run());
    return button;
  }
}
