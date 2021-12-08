package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;


import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.LabelDTO;

import javax.swing.*;
import java.awt.*;

public class LabelComboboxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        LabelDTO labelDTO = (LabelDTO) value;
        return super.getListCellRendererComponent(list, labelDTO.getName(), index, isSelected, cellHasFocus);
    }
}
