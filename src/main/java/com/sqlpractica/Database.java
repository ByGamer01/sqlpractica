package com.sqlpractica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestor central de la conexión a SQLite.
 *
 * LÓGICA / RESPONSABILIDADES:
 * - Mantiene UNA SOLA conexión para toda la aplicación (patrón singleton),
 * guardada en el atributo estático "conexion".
 * - obtenerConexion(): si no hay conexión (o está cerrada), la crea.
 * Además ejecuta "PRAGMA foreign_keys = ON" porque SQLite, por defecto,
 * NO obliga a cumplir las claves foráneas.
 * - inicializarEsquema(): ejecuta los CREATE TABLE IF NOT EXISTS de las
 * 5 tablas (empleado, tipo_plaza, plaza, ocupa, nomina) con sus claves
 * primarias y foráneas tal y como aparecen en el diagrama E/R.
 * - cerrar(): cierra la conexión al terminar la aplicación.
 *
 * El constructor está privado y la clase es "final" porque nadie debería
 * crear instancias: todo se usa de forma estática.
 *
 * Doc JDBC + SQLite:
 * https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
 */
public final class Database {

    // URL de conexión: SQLite usa un fichero local llamado sqlpractica.db
    // que se crea en el directorio desde donde se ejecute el programa.
    private static final String DB_URL = "jdbc:sqlite:sqlpractica.db";

    // Conexión compartida (singleton). Estática porque pertenece a la clase,
    // no a una instancia.
    private static Connection conexion;

    // Constructor privado: nadie puede hacer "new Database()".
    private Database() {
    }

    /**
     * Devuelve la conexión compartida. Si no existe la abre y activa las FK.
     * synchronized para que dos hilos no creen dos conexiones a la vez.
     */
    public static synchronized Connection obtenerConexion() throws DAOException {
        try {
            // Si nunca se ha conectado o la conexión se cerró por error,
            // la (re)abrimos.
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(DB_URL);

                // SQLite ignora las claves foráneas si no se activan a mano.
                try (Statement st = conexion.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON;");
                }
            }
            return conexion;
        } catch (SQLException e) {
            // Convertimos cualquier error JDBC en nuestra DAOException
            // para que la UI no tenga que conocer SQLException.
            throw new DAOException("No se ha podido conectar a la base de datos.", e);
        }
    }

    /**
     * Crea las tablas si todavía no existen. Se llama una vez al arrancar.
     *
     * Cada string del array es una sentencia SQL DDL (Data Definition
     * Language). El IF NOT EXISTS evita errores si ya están creadas.
     */
    public static void inicializarEsquema() throws DAOException {
        String[] ddl = new String[] {
                // Tabla empleado: PK = nss.
                "CREATE TABLE IF NOT EXISTS empleado (" +
                        "  nss TEXT PRIMARY KEY," +
                        "  nombre TEXT NOT NULL," +
                        "  apellidos TEXT NOT NULL," +
                        "  email TEXT," +
                        "  iban TEXT" +
                        ");",

                // Tabla tipo_plaza: PK = nombre. Es una tabla "catálogo".
                "CREATE TABLE IF NOT EXISTS tipo_plaza (" +
                        "  nombre TEXT PRIMARY KEY," +
                        "  funcion TEXT" +
                        ");",

                // Tabla plaza: PK = codigo.
                // FK codigo_plaza_supervisora -> plaza(codigo) (auto-relación,
                // se pone NULL si se borra la supervisora)
                // FK nombre_tipo_plaza -> tipo_plaza(nombre)
                // (RESTRICT = no se puede borrar un tipo si hay plazas que lo usan)
                "CREATE TABLE IF NOT EXISTS plaza (" +
                        "  codigo TEXT PRIMARY KEY," +
                        "  nombre TEXT NOT NULL," +
                        "  salario REAL NOT NULL," +
                        "  codigo_plaza_supervisora TEXT," +
                        "  informe_supervision TEXT," +
                        "  nombre_tipo_plaza TEXT NOT NULL," +
                        "  FOREIGN KEY (codigo_plaza_supervisora) REFERENCES plaza(codigo) ON DELETE SET NULL," +
                        "  FOREIGN KEY (nombre_tipo_plaza) REFERENCES tipo_plaza(nombre) ON DELETE RESTRICT" +
                        ");",

                // Tabla ocupa: relación N:M entre empleado y plaza
                // PK compuesta = (nss_empleado, codigo_plaza)
                // Si se borra el empleado o la plaza, se borran sus ocupaciones (CASCADE)
                "CREATE TABLE IF NOT EXISTS ocupa (" +
                        "  nss_empleado TEXT NOT NULL," +
                        "  codigo_plaza TEXT NOT NULL," +
                        "  fecha_inicio TEXT NOT NULL," +
                        "  fecha_fin TEXT," +
                        "  PRIMARY KEY (nss_empleado, codigo_plaza)," +
                        "  FOREIGN KEY (nss_empleado) REFERENCES empleado(nss) ON DELETE CASCADE," +
                        "  FOREIGN KEY (codigo_plaza) REFERENCES plaza(codigo) ON DELETE CASCADE" +
                        ");",

                // Tabla nomina: PK = id autoincremental.
                // Las FK referencian al empleado y a la plaza.
                "CREATE TABLE IF NOT EXISTS nomina (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  iban_pago TEXT NOT NULL," +
                        "  importe_pago REAL NOT NULL," +
                        "  nss_empleado TEXT NOT NULL," +
                        "  codigo_plaza TEXT NOT NULL," +
                        "  FOREIGN KEY (nss_empleado) REFERENCES empleado(nss) ON DELETE CASCADE," +
                        "  FOREIGN KEY (codigo_plaza) REFERENCES plaza(codigo) ON DELETE CASCADE" +
                        ");"
        };

        // Statement permite ejecutar SQL directo. try-with-resources cierra
        // el Statement automáticamente al terminar.
        try (Statement st = obtenerConexion().createStatement()) {
            for (String sql : ddl) {
                st.execute(sql);
            }
        } catch (SQLException e) {
            throw new DAOException("Error inicializando el esquema de la base de datos.", e);
        }
    }

    /**
     * Cierra la conexión. Se llama desde el shutdown hook de Main.
     * Si no la ponemos no nos funcionará el cierre automático de la conexión y el
     * programa se quedará colgado al salir. synchronized para que no se cierre
     * mientras se está usando
     */
    public static synchronized void cerrar() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ignorada) {
                // Si falla al cerrar no hacemos nada, el programa va a terminar.
            }
            conexion = null;
        }
    }
}
