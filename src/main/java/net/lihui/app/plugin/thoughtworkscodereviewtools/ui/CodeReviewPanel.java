package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import lombok.extern.slf4j.Slf4j;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerDTO;
import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

@Slf4j
public class CodeReviewPanel {
    private JPanel panel = new JPanel();
    private ComboBox<OwnerDTO> comboBox;
    private JTextField feedBackText;
    private JButton refreshButton;

    public CodeReviewPanel() {
        initOwnerComboBox();
        feedBackText = new JTextField();
        feedBackText.setPreferredSize(new Dimension(200, 30));
        panel.add(feedBackText);
        refreshButton = new JButton("refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // refresh the member and the label data
            }
        });
        panel.add(refreshButton);

    }

    private void initOwnerComboBox() {
        List<TrelloBoardMember> trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
        CollectionComboBoxModel comboBoxModel = new CollectionComboBoxModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers));
        comboBox = new ComboBox(comboBoxModel);
        comboBox.setEditable(true);
        comboBox.setMaximumRowCount(3);
        comboBox.setSelectedItem(null); // remember last selected user
        AutoCompleteComboBoxEditor editor = new AutoCompleteComboBoxEditor(comboBox.getEditor(), ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        JTextComponent editorComponent = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editorComponent.setDocument(new AutoCompleteDocument(new ComboBoxAdaptor(comboBox), true));
        comboBox.setEditor(editor);
        comboBox.setToolTipText("owner");
        panel.add(comboBox);
    }

    public JPanel getPanel() {
        return panel;
    }

    public ComboBox getOwnerComboBox() {
        return comboBox;
    }

    public FeedBackContext getFeedbackContext() {
        return FeedBackContext.builder()
                .feedback(feedBackText.getText())
                .memberList(Collections.singletonList(MEMBER_MAPPER.toMember(comboBox.getItem())))
                .build();
    }
}
