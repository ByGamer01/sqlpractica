# SQL Practica

Aplicación de escritorio en **Java Swing** + **SQLite** para la gestión de
empleados, plazas, tipos de plaza, ocupaciones y nóminas.

La interfaz es un `JFrame` con un `JTabbedPane` que contiene una pestaña por
entidad. Cada pestaña tiene la misma estructura: una tabla con el listado
arriba y un formulario con cuatro botones (Crear, Editar, Eliminar, Limpiar)
debajo.

## Requisitos

- Java 17 o superior
- Maven 3.8 o superior
- Conexión a internet la primera vez (para descargar `sqlite-jdbc`)

## Compilación

```bash
mvn package
```

Genera `target/sqlpractica.jar` (uber-jar con todas las dependencias dentro).

## Ejecución

```bash
java -jar target/sqlpractica.jar
```

La primera vez se crea automáticamente el fichero `sqlpractica.db` en la
carpeta desde donde se lance la aplicación, junto con las cinco tablas.

## Estructura del código

```
src/main/java/com/sqlpractica/
├── Main.java              ← punto de entrada (init BD + UIManager + EDT)
├── AppFrame.java          ← ventana principal con JTabbedPane
├── Database.java          ← singleton de conexión + creación del esquema
├── DAOException.java      ← excepción propia para errores de acceso a datos
│
├── model/                 ← POJOs (uno por tabla)
│   ├── Empleado.java
│   ├── TipoPlaza.java
│   ├── Plaza.java
│   ├── Ocupa.java
│   └── Nomina.java
│
├── dao/                   ← un DAO por tabla con CRUD (insertar/actualizar/eliminar/obtenerTodos)
│   ├── EmpleadoDAO.java
│   ├── TipoPlazaDAO.java
│   ├── PlazaDAO.java
│   ├── OcupaDAO.java
│   └── NominaDAO.java
│
└── ui/                    ← un panel CRUD por entidad
    ├── EmpleadoPanel.java
    ├── TipoPlazaPanel.java
    ├── PlazaPanel.java
    ├── OcupaPanel.java
    └── NominaPanel.java
```

Tres carpetas (paquetes) además de la raíz: `model`, `dao`, `ui`.
Cada panel `JPanel` se basta solo: pide los datos a su DAO y dibuja la
tabla y el formulario.

## Flujo de la aplicación

```
Main.main()
  └─> Database.inicializarEsquema()        # CREATE TABLE IF NOT EXISTS x5
  └─> UIManager.setLookAndFeel(...)        # estilo del SO
  └─> SwingUtilities.invokeLater(new AppFrame())
            │
            ▼
     AppFrame (JFrame)
       └─ JTabbedPane con 5 pestañas
            ├─ EmpleadoPanel
            ├─ PlazaPanel
            ├─ TipoPlazaPanel
            ├─ OcupaPanel
            └─ NominaPanel
                  │
                  ▼
        cada panel llama al DAO correspondiente
                  │
                  ▼
          PreparedStatement (JDBC)
                  │
                  ▼
              SQLite (sqlpractica.db)
```

## Capas

1. **Modelo** (`model/`): POJOs (clases con atributos y métodos
   `obtenerXxx` / `asignarXxx`). No saben SQL; sólo guardan datos.
2. **DAO** (`dao/`): clases que hablan con la BD usando `PreparedStatement`.
   Cualquier `SQLException` se envuelve en `DAOException` para que la UI
   no dependa de JDBC.
3. **UI** (`ui/`): paneles Swing. Sólo conocen el modelo y el DAO.
4. **Database**: gestor singleton de la conexión y creación del esquema.

## Decisiones de diseño

- **Patrón DAO**: la UI no toca SQL directamente. Cada operación pasa por
  un DAO.
- **`PreparedStatement`** en todas las consultas para protegerse contra SQL
  injection.
  Doc: <https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html>
- **Excepción propia `DAOException`** que envuelve cualquier `SQLException`,
  así la UI captura errores de dominio y no excepciones JDBC.
- **`PRAGMA foreign_keys = ON`**: SQLite no aplica las claves foráneas por
  defecto; hay que activarlas explícitamente en la conexión.
- **`ON DELETE CASCADE`** en `ocupa` y `nomina`: al eliminar un empleado o
  una plaza, sus ocupaciones y nóminas se limpian automáticamente.
- **`ON DELETE SET NULL`** en la auto-relación de
  `plaza.codigo_plaza_supervisora`: si eliminas la plaza supervisora, las
  que dependían de ella quedan sin supervisora pero no se eliminan.
- **`ON DELETE RESTRICT`** en `tipo_plaza`: no se puede eliminar un tipo si
  hay plazas que lo referencian.
- **`JTabbedPane`** estándar para cambiar entre las cinco pantallas, sin
  componentes personalizados.
  Doc: <https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/javax/swing/JTabbedPane.html>
- **`JTable` + `DefaultTableModel`** para mostrar las listas; las celdas
  no son editables (la edición se hace por el formulario).
  Doc: <https://docs.oracle.com/javase/tutorial/uiswing/components/table.html>
- **`LocalDate.parse`** para validar las fechas en `OcupaPanel` (formato
  ISO `yyyy-MM-dd`).
  Doc: <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html#parse(java.lang.CharSequence)>
- **Validación en el formulario** antes de llamar al DAO: campos
  obligatorios, salario/importe numérico, formato de fecha ISO.

## Esquema de la base de datos

```
empleado(nss PK, nombre, apellidos, email, iban)

tipo_plaza(nombre PK, funcion)

plaza(codigo PK,
      nombre,
      salario,
      codigo_plaza_supervisora FK -> plaza(codigo)      [ON DELETE SET NULL],
      informe_supervision,
      nombre_tipo_plaza        FK -> tipo_plaza(nombre) [ON DELETE RESTRICT])

ocupa(nss_empleado FK -> empleado(nss) [ON DELETE CASCADE],
      codigo_plaza FK -> plaza(codigo) [ON DELETE CASCADE],
      fecha_inicio,
      fecha_fin,
      PRIMARY KEY (nss_empleado, codigo_plaza))

nomina(id PK AUTOINCREMENT,
       iban_pago,
       importe_pago,
       nss_empleado FK -> empleado(nss) [ON DELETE CASCADE],
       codigo_plaza FK -> plaza(codigo) [ON DELETE CASCADE])
```

## Sugerencia de estructura para el documento de la práctica

- **Portada** — nombre, título, fecha
- **Objectiu** — qué pide el enunciado, qué entidades hay
- **Esquema relacional** — diagrama del enunciado y cardinalidades
- **Decisions de disseny**:
  - Patrón DAO y separación en capas
  - `PreparedStatement` en lugar de `Statement` (seguridad)
  - Excepción `DAOException` propia
  - `PRAGMA foreign_keys = ON`
  - Estrategia `ON DELETE` para cada FK (CASCADE / SET NULL / RESTRICT)
  - `JTabbedPane` estándar de Swing
- **Estructura de paquetes** — captura del árbol del proyecto
- **Pantalles de l'aplicació** — una captura por pestaña con un párrafo
- **Gestió d'errors** — confirmación al eliminar + validación de formulario
- **Conclusions** — qué has aprendido, dificultades y posibles mejoras
  (p. ej. usar `JComboBox` para las FK en vez de campos de texto)
