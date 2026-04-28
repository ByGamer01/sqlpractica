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
        table.setFont(Theme.FONT_TABLE);
        table.setRowHeight(26);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(Theme.BG_SELECTED);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(Theme.FONT_TABLE_HEAD);
        header.setBackground(Theme.BG_TABLE_HEADER);
        header.setForeground(Theme.TEXT_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                ((javax.swing.JLabel) c).setBorder(new EmptyBorder(2, 8, 2, 8));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Theme.BG_CARD : Theme.BG_TABLE_ALT);
                    c.setForeground(Theme.TEXT_PRIMARY);
                }
                return c;
            }
        };
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER_LIGHT, 1, true));
        sp.getViewport().setBackground(Theme.BG_CARD);
        add(sp, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                onRowSelected(table.getSelectedRow());
            }
        });

        formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
        add(formPanel, BorderLayout.SOUTH);
    }

    protected abstract void buildForm();
    public abstract void reloadTable();
    protected abstract void onRowSelected(int rowIndex);
    protected abstract void onCreate();
    protected abstract void onUpdate();
    protected abstract void onDelete();
    protected abstract void onClear();

    protected void addActionButtons() {
        JPanel buttons = new JPanel(new GridLayout(1, 4, 6, 0));
        buttons.setOpaque(false);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.setBorder(new EmptyBorder(10, 0, 0, 0));

        StyledButton bCreate = StyledButton.primary("Crear");
        StyledButton bUpdate = StyledButton.info("Editar");
        StyledButton bDelete = StyledButton.danger("Eliminar");
        StyledButton bClear  = StyledButton.secondary("Limpiar");

        bCreate.addActionListener(e -> safe(this::onCreate));
        bUpdate.addActionListener(e -> safe(this::onUpdate));
        bDelete.addActionListener(e -> safe(this::onDelete));
        bClear.addActionListener (e -> onClear());

        buttons.add(bCreate);
        buttons.add(bUpdate);
        buttons.add(bDelete);
        buttons.add(bClear);

        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(buttons);
    }

    /**
     * Envuelve una acción en try/catch para que cualquier DAOException
     * o error inesperado acabe como mensaje al usuario, no como stack trace.
     */
    protected void safe(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
            showError(cause.getMessage());
        }
    }

    protected void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    protected boolean confirm(String msg) {
        int r = JOptionPane.showConfirmDialog(this, msg, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return r == JOptionPane.YES_OPTION;
    }

    /** Helper: convierte DAOException en RuntimeException para usar dentro de safe(). */
    protected static void wrap(DaoAction action) {
        try {
            action.run();
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    protected interface DaoAction {
        void run() throws DAOException;
    }
}
