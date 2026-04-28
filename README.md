# SQL Practica

Aplicación de escritorio en **Java Swing** + **SQLite** para la gestión de empleados,
plazas, tipos de plaza, ocupaciones y nóminas.

La aplicación presenta una interfaz inspirada en una app móvil: marco estrecho,
cabecera, una barra de navegación horizontal en el medio con cinco pestañas y,
debajo, una tabla con el listado y un formulario de entrada con los botones de acción.

## Requisitos

- Java 17 o superior
- Maven 3.8 o superior
- Conexión a internet la primera vez (para descargar `sqlite-jdbc`)

## Compilación

```bash
mvn package
```

Genera `target/sqlpractica.jar` (uber-jar con todas las dependencias).

## Ejecución

```bash
java -jar target/sqlpractica.jar
```

La primera vez se crea automáticamente el fichero `sqlpractica.db` en la
carpeta desde donde se lance la aplicación, y se crean las cinco tablas.

## Estructura del código

```
src/main/java/com/sqlpractica/
├── Main.java                   ← punto de entrada
├── exception/
│   └── DAOException.java       ← excepción propia para errores de acceso a datos
├── model/                      ← POJOs (uno por tabla)
│   ├── Empleado.java
│   ├── TipoPlaza.java
│   ├── Plaza.java
│   ├── Ocupa.java
│   └── Nomina.java
├── db/
│   └── Database.java           ← singleton de conexión + creación del esquema
├── dao/                        ← un DAO por tabla con CRUD completo
│   ├── EmpleadoDAO.java
│   ├── TipoPlazaDAO.java
│   ├── PlazaDAO.java
│   ├── OcupaDAO.java
│   └── NominaDAO.java
└── ui/
    ├── Theme.java              ← colores y fuentes centralizados
    ├── AppFrame.java           ← frame principal con CardLayout
    ├── components/             ← componentes reutilizables
    │   ├── RoundedPanel.java
    │   ├── StyledButton.java
    │   ├── StyledTextField.java
    │   ├── Header.java
    │   └── NavBar.java
    └── panels/                 ← una pantalla por entidad
        ├── BaseCrudPanel.java  ← plantilla común (tabla + formulario + botones)
        ├── EmpleadoPanel.java
        ├── TipoPlazaPanel.java
        ├── PlazaPanel.java
        ├── OcupaPanel.java
        └── NominaPanel.java
```

## Decisiones de diseño

- **Patrón DAO**: la UI no toca SQL directamente. Cada operación pasa por un DAO.
- **PreparedStatement** en todas las consultas para protegerse contra SQL injection.
- **Excepción propia `DAOException`** que envuelve cualquier `SQLException`,
  así la UI captura errores de dominio y no excepciones JDBC.
- **`PRAGMA foreign_keys = ON`**: SQLite no aplica las claves foráneas por
  defecto; hay que activarlas explícitamente en la conexión.
- **`ON DELETE CASCADE`** en `ocupa` y `nomina` (al eliminar un empleado o una
  plaza, sus ocupaciones y nóminas se limpian automáticamente).
- **`ON DELETE SET NULL`** en la auto-relación de `plaza.codigo_plaza_supervisora`
  (si eliminas la plaza supervisora, las que dependían de ella quedan sin
  supervisora pero no se eliminan).
- **`ON DELETE RESTRICT`** en `tipo_plaza` (no se puede eliminar un tipo si hay
  plazas que lo referencian).
- **CardLayout** para cambiar entre las cinco pantallas sin recrearlas cada vez.
- **Validación en el formulario** antes de enviar nada al DAO (campos obligatorios,
  formatos numéricos, formato de fecha ISO).
