package com.sqlpractica;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.sqlpractica.ui.EmpleadoPanel;
import com.sqlpractica.ui.NominaPanel;
import com.sqlpractica.ui.OcupaPanel;
import com.sqlpractica.ui.PlazaPanel;
import com.sqlpractica.ui.TipoPlazaPanel;

/**
 * Ventana principal de la aplicación.
 *
 * LÓGICA:
 *   - Hereda de JFrame, que es la clase estándar de Swing para ventanas.
 *   - Crea un JTabbedPane (componente con pestañas).
 *   - Por cada entidad de la base de datos (empleado, plaza, etc.) añade
 *     una pestaña con su panel CRUD correspondiente.
 *   - Cada panel se construye solo: pide los datos a su DAO y dibuja la
 *     tabla y el formulario.
 *
 * Doc JTabbedPane:
 *   https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/javax/swing/JTabbedPane.html
 */
public class AppFrame extends JFrame {

    public AppFrame() {
        // Título de la ventana.
        super("Programación con bases de datos");

        // Cuando el usuario cierra la X, la JVM termina (y se ejecuta
        // el shutdown hook de Main que cierra la conexión).
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Tamaño inicial y centrado en la pantalla.
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Contenedor de pestañas. Cada addTab(nombre, componente) crea
        // una pestaña con ese título y mete dentro el panel indicado.
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Empleados", new EmpleadoPanel());
        tabs.addTab("Plazas", new PlazaPanel());
        tabs.addTab("Tipos de plaza", new TipoPlazaPanel());
        tabs.addTab("Ocupaciones", new OcupaPanel());
        tabs.addTab("Nominas", new NominaPanel());

        // El JTabbedPane ocupa todo el contenido del frame.
        add(tabs);
    }
}
