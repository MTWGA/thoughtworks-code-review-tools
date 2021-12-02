package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.julienvey.trello.domain.Member;
import lombok.AllArgsConstructor;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerCheckboxDTO;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

@AllArgsConstructor
public class OwnerCheckboxTableModel extends AbstractTableModel {

    private final int COLUMN_COUNT = 3;
    private List<OwnerCheckboxDTO> ownerCheckboxDTOList;

    @Override
    public int getRowCount() {
        return ownerCheckboxDTOList.size() % COLUMN_COUNT == 0 ?
                ownerCheckboxDTOList.size() / COLUMN_COUNT : ownerCheckboxDTOList.size() / COLUMN_COUNT + 1;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (checkCellOutIndex(rowIndex, columnIndex)) {
            return null;
        }
        return ownerCheckboxDTOList.get(rowIndex * COLUMN_COUNT + columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public String getColumnName(int column) {
        return "";
    }

    private boolean checkCellOutIndex(int rowIndex, int columnIndex) {
        if (rowIndex * COLUMN_COUNT + columnIndex >= ownerCheckboxDTOList.size()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !checkCellOutIndex(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ownerCheckboxDTOList.set(rowIndex * COLUMN_COUNT + columnIndex, (OwnerCheckboxDTO) aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public List<Member> getSelectedMembers() {
        List<OwnerCheckboxDTO> selectedOwnerCheckboxDTOS = ownerCheckboxDTOList.stream()
                .filter(OwnerCheckboxDTO::isSelected)
                .collect(Collectors.toList());

        return MEMBER_MAPPER.toMemberList(selectedOwnerCheckboxDTOS);
    }
}
