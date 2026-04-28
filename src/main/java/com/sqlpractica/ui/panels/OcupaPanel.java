package com.sqlpractica.ui.panels;

import com.sqlpractica.dao.OcupaDAO;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Ocupa;
import com.sqlpractica.ui.components.StyledTextField;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

public class OcupaPanel extends BaseCrudPanel {

    private final OcupaDAO dao = new OcupaDAO();

    private final StyledTextField tfNss          = new StyledTextField();
    private final StyledTextField tfCodigoPlaza  = new StyledTextField();
    private final StyledTextField tfFechaInicio  = new StyledTextField();
    private final StyledTextField tfFechaFin     = new StyledTextField();

    public OcupaPanel() {
        super(new String[]{"NSS empleado", "Código plaza", "Fecha inicio", "Fecha fin"});
        buildForm();
        reloadTable();
    }

    @Override
    protected void buildForm() {
        JPanel row1 = new JPanel(new GridLayout(1, 2, 8, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.add(col("NSS empleado (PK)", tfNss));
        row1.add(col("Código plaza (PK)", tfCodigoPlaza));
        formPanel.add(row1);
        formPanel.add(Box.createVerticalStrut(8));

        JPanel row2 = new JPanel(new GridLayout(1, 2, 8, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.add(col("Fecha inicio (yyyy-mm-dd)", tfFechaInicio));
        row2.add(col("Fecha fin (opcional)", tfFechaFin));
        formPanel.add(row2);

        addActionButtons();
    }

    private JPanel col(String label, StyledTextField field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new javax.swing.BoxLayout(p, javax.swing.BoxLayout.Y_AXIS));
        p.add(StyledTextField.makeLabel(label));
        p.add(field);
        return p;
    }

    @Override
    public void reloadTable() {
        try {
            tableModel.setRowCount(0);
            List<Ocupa> lista = dao.findAll();
            for (Ocupa o : lista) {
                tableModel.addRow(new Object[]{
                        o.getNssEmpleado(),
                        o.getCodigoPlaza(),
                        o.getFechaInicio(),
                        o.getFechaFin() == null ? "" : o.getFechaFin()
                });
            }
        } catch (DAOException ex) {
            showError(ex.getMessage());
        }
    }

    @Override
    protected void onRowSelected(int rowIndex) {
        tfNss.setText(str(tableModel.getValueAt(rowIndex, 0)));
        tfCodigoPlaza.setText(str(tableModel.getValueAt(rowIndex, 1)));
        tfFechaInicio.setText(str(tableModel.getValueAt(rowIndex, 2)));
        tfFechaFin.setText(str(tableModel.getValueAt(rowIndex, 3)));
    }

    private static String str(Object o) { return o == null ? "" : o.toString(); }
}
