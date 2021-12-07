package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.LabelDTO;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

public class LabelDtoToStringConverter extends ObjectToStringConverter {
    @Override
    public String getPreferredStringForItem(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return o.toString();
        }
        return ((LabelDTO) o).getName();
    }
}
