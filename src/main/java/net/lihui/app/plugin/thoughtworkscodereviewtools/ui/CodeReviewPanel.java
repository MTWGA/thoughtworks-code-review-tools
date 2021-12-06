package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import lombok.extern.slf4j.Slf4j;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerCheckboxDTO;
import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Collections;
import java.util.List;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

@Slf4j
public class CodeReviewPanel {
    private JPanel panel = new JPanel();
    private ComboBox<OwnerCheckboxDTO> comboBox;
    private JTextField feedBackText;

    public CodeReviewPanel() {
        List<TrelloBoardMember> trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
        comboBox = new ComboBox(new CollectionComboBoxModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers)));
        comboBox.setRenderer(new OwnerComboBoxRenderer());
        comboBox.setEditable(true);
        comboBox.setMaximumRowCount(3);
        comboBox.setSelectedItem(null); // remember last selected user
        AutoCompleteComboBoxEditor editor = new AutoCompleteComboBoxEditor(comboBox.getEditor(), ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        JTextComponent editorComponent = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editorComponent.setDocument(new AutoCompleteDocument(new ComboBoxAdaptor(comboBox), true));
        comboBox.setEditor(editor);
        comboBox.getItem();
        log.info("comboBox.getItem is: {}", comboBox.getItem());
        feedBackText = new JTextField();
        panel.add(comboBox);
        panel.add(feedBackText);
    }

    public JPanel getPanel() {
        return panel;
    }

    public ComboBox getOwnerComboBox() {
        log.info("comboBox.getItem is: {}", comboBox.getItem());
        return comboBox;
    }

    public FeedBackContext getFeedbackContext() {
        return FeedBackContext.builder()
                .feedback(feedBackText.getText())
                .memberList(Collections.singletonList(MEMBER_MAPPER.toMember(comboBox.getItem())))
                .build();
    }
}
