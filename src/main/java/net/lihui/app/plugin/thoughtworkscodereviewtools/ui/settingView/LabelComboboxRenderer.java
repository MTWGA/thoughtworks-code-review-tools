package net.lihui.app.plugin.thoughtworkscodereviewtools.ui.settingView;


import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.LabelDTO;

import javax.swing.*;
import java.awt.*;

public class LabelComboboxRenderer implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        LabelDTO labelDTO = (LabelDTO) value;
        return new JLabel(labelDTO.getName());
    }
}
