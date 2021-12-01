package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerCheckboxDTO;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class TableCheckboxCellEditor extends AbstractCellEditor implements TableCellEditor {
    private String id;
    private String userName;
    private String fullName;
    private boolean selected;
    private JCheckBox checkBox;

    public TableCheckboxCellEditor() {
        checkBox = new JCheckBox();
        checkBox.addActionListener(e -> {
            fireEditingStopped(); // Without this call, the editor would remain active, even though the modal dialog is no longer visible. The call to fireEditingStopped lets the table know that it can deactivate the editor, letting the cell be handled by the renderer again.
        });
    }

    @Override
    public Object getCellEditorValue() {
        return new OwnerCheckboxDTO(id, userName, fullName, selected);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        OwnerCheckboxDTO ownerCheckboxDTO = (OwnerCheckboxDTO) value;
        id = ownerCheckboxDTO.getId();
        fullName = ownerCheckboxDTO.getFullName();
        userName = ownerCheckboxDTO.getUsername();
        selected = !ownerCheckboxDTO.isSelected();

        checkBox.setText(fullName);
        checkBox.setSelected(selected);
        return checkBox; // return editor, not the edited data or result data
    }
}
