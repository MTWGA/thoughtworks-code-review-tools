package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class TableCheckboxCellEditor extends AbstractCellEditor implements TableCellEditor {
    private String text;
    private boolean isChecked;
    private JCheckBox checkBox;

    public TableCheckboxCellEditor() {
        checkBox = new JCheckBox();
        checkBox.addActionListener(e -> {
            fireEditingStopped(); // Without this call, the editor would remain active, even though the modal dialog is no longer visible. The call to fireEditingStopped lets the table know that it can deactivate the editor, letting the cell be handled by the renderer again.
        });
    }

    @Override
    public Object getCellEditorValue() {
        return new JCheckBox(text, isChecked);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JCheckBox checkboxCell = (JCheckBox) value;
        text = checkboxCell.getText();
        isChecked = !checkboxCell.isSelected();

        checkBox.setText(text);
        checkBox.setSelected(isChecked);
        return checkBox; // return editor, not the edited data or result data
    }
}
