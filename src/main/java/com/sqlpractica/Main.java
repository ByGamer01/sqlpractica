package com.sqlpractica;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación.
 *
 * LÓGICA (en orden de ejecución):
 *   1. Java arranca llamando a main()
 *   2. Pedimos a Database que cree las tablas si no existen. Si falla,
 *      mostramos un diálogo de error y cerramos el programa con código 1
 *   3. Cambiamos el "look-and-feel" para que la ventana tenga el aspecto
 *      del sistema operativo (Windows aquí). Si falla, no pasa nada,
 *      seguimos con el aspecto por defecto de Java
 *   4. Registramos un "shutdown hook": código que se ejecutará cuando
 *      el programa termine, para cerrar la conexión a la base de datos
 *   5. Pedimos a Swing que cree la ventana principal y la haga visible.
 *      Esto se hace dentro de SwingUtilities.invokeLater para que toda
 *      la UI se construya en el "Event Dispatch Thread" (regla de Swing)
 *
 * Doc Swing y EDT:
 *   https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
 */
public class Main {

    public static void main(String[] args) {
        // 1. Inicializar la base de datos (CREATE TABLE IF NOT EXISTS).
        // Si no podemos crear las tablas, no tiene sentido seguir.
        if (!Database.inicializarEsquema()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se ha podido inicializar la base de datos:\n" + Database.getMensajeError(),
                    "Error fatal",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        //  Aplicar el aspecto del programa
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla el look-and-feel, seguimos con el de Java por defecto.
        }

        // 3. Cerrar la conexión a SQLite cuando se cierre el programa.
        Runtime.getRuntime().addShutdownHook(new Thread(Database::cerrar));

        // 4. Construir y mostrar la ventana en el hilo de eventos de Swing.
        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }
}
