package com.sqlpractica;

/**
 * Excepción propia para los errores de la capa de datos (DAO).
 *
 * LÓGICA / POR QUÉ EXISTE:
 *   - Los DAOs trabajan con JDBC y pueden lanzar SQLException.
 *   - Si dejásemos pasar SQLException tal cual, la capa de UI tendría
 *     que importar java.sql y conocer detalles de la base de datos.
 *   - Por eso, cada DAO captura SQLException y lanza una DAOException
 *     con un mensaje. La UI solo tiene que mostrar getMessage().
 *
 * Hereda de Exception (es "checked"): el compilador obliga a tratarla
 * con try/catch o a propagarla con "throws".
 */
public class DAOException extends Exception {

    // Constructor con solo el mensaje (cuando no hay otra excepción detrás).
    public DAOException(String message) {
        super(message);
    }

    // Constructor con mensaje + causa original (la SQLException),
    // útil para no perder el rastro del error real.
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
