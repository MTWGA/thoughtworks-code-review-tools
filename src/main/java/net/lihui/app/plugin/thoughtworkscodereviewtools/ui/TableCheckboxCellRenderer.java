package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableCheckboxCellRenderer extends JCheckBox implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof JCheckBox) {
            JCheckBox checkboxCell = (JCheckBox) value;
            setText(checkboxCell.getText());
            setSelected(checkboxCell.isSelected());
        }
        return this;
    }
}
