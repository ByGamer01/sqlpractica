package com.sqlpractica.ui.panels;

import com.sqlpractica.dao.NominaDAO;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Nomina;
import com.sqlpractica.ui.components.StyledTextField;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

public class NominaPanel extends BaseCrudPanel {

    private final NominaDAO dao = new NominaDAO();

    private final StyledTextField tfId      = new StyledTextField();
    private final StyledTextField tfIban    = new StyledTextField();
    private final StyledTextField tfImporte = new StyledTextField();
    private final StyledTextField tfNss     = new StyledTextField();
    private final StyledTextField tfPlaza   = new StyledTextField();

    public NominaPanel() {
        super(new String[]{"ID", "IBAN pago", "Importe", "NSS empleado", "Código plaza"});
        buildForm();
        tfId.setEditable(false);
        tfId.setBackground(new java.awt.Color(0xeef2ff));
        reloadTable();
    }

    @Override
    protected void buildForm() {
        formPanel.add(StyledTextField.makeLabel("ID (auto-generado)"));
        tfId.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfId);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(StyledTextField.makeLabel("IBAN pago"));
        tfIban.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfIban);
        formPanel.add(Box.createVerticalStrut(8));

        JPanel row = new JPanel(new GridLayout(1, 3, 8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(col("Importe (€)", tfImporte));
        row.add(col("NSS empleado", tfNss));
        row.add(col("Código plaza", tfPlaza));
        formPanel.add(row);

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
            List<Nomina> lista = dao.findAll();
            for (Nomina n : lista) {
                tableModel.addRow(new Object[]{
                        n.getId(),
                        n.getIbanPago(),
                        String.format("%.2f", n.getImportePago()),
                        n.getNssEmpleado(),
                        n.getCodigoPlaza()
                });
            }
        } catch (DAOException ex) {
            showError(ex.getMessage());
        }
    }

    @Override
    protected void onRowSelected(int rowIndex) {
        tfId.setText(str(tableModel.getValueAt(rowIndex, 0)));
        tfIban.setText(str(tableModel.getValueAt(rowIndex, 1)));
        tfImporte.setText(str(tableModel.getValueAt(rowIndex, 2)).replace(",", "."));
        tfNss.setText(str(tableModel.getValueAt(rowIndex, 3)));
        tfPlaza.setText(str(tableModel.getValueAt(rowIndex, 4)));
    }

    private static String str(Object o) { return o == null ? "" : o.toString(); }
}
