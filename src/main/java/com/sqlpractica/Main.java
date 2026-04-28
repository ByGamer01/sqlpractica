package com.sqlpractica;

import com.sqlpractica.db.Database;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.ui.AppFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación.
 * - Inicializa el esquema de la base de datos.
 * - Aplica un look-and-feel neutro.
 * - Lanza el frame principal en el hilo de EDT.
 */
public class Main {

    public static void main(String[] args) {
        try {
            Database.initSchema();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(null,
                    "No se ha podido inicializar la base de datos:\n" + e.getMessage(),
                    "Error fatal", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Runtime.getRuntime().addShutdownHook(new Thread(Database::close));

        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }
}
