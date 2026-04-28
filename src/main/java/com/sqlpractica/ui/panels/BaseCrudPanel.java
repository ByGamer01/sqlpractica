package com.sqlpractica.ui.panels;

import com.sqlpractica.exception.DAOException;
import com.sqlpractica.ui.Theme;
import com.sqlpractica.ui.components.StyledButton;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * Plantilla común para todos los paneles CRUD: una tabla arriba y un
 * formulario abajo con 4 botones (Crear, Editar, Eliminar, Limpiar).
 * Cada subclase decide las columnas, los campos del formulario y
 * cómo se enlaza cada acción al DAO correspondiente.
 */
public abstract class BaseCrudPanel extends JPanel {

    protected final DefaultTableModel tableModel;
    protected final JTable table;
    protected final JPanel formPanel;

    protected BaseCrudPanel(String[] columns) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_APP);
        setBorder(new EmptyBorder(10, 12, 14, 12));

        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);

        formPanel = new JPanel();
    }
}
