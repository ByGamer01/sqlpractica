package com.sqlpractica.ui;

import com.sqlpractica.ui.components.Header;
import com.sqlpractica.ui.components.NavBar;
import com.sqlpractica.ui.panels.BaseCrudPanel;
import com.sqlpractica.ui.panels.EmpleadoPanel;
import com.sqlpractica.ui.panels.NominaPanel;
import com.sqlpractica.ui.panels.OcupaPanel;
import com.sqlpractica.ui.panels.PlazaPanel;
import com.sqlpractica.ui.panels.TipoPlazaPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Frame principal de la aplicación. Tiene proporciones tipo móvil y contiene:
 *  - Cabecera (Header) arriba con el título de la sección.
 *  - Una franja de navegación (NavBar) justo en el medio con 5 pestañas.
 *  - Un panel central que cambia según la pestaña seleccionada
 *    mediante CardLayout.
 */
public class AppFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel cardContainer = new JPanel(cards);
    private final Header header = new Header();

    private final Map<String, BaseCrudPanel> panels = new LinkedHashMap<>();
    private final Map<String, String> titles = new LinkedHashMap<>();

    public AppFrame() {
        super("SQL Practica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Theme.WIDTH, Theme.HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_APP);
        setLayout(new BorderLayout());
    }
}
