package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import lombok.extern.slf4j.Slf4j;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabel;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabelState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.LabelDTO;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerDTO;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.settingView.LabelComboboxRenderer;
import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Collections;
import java.util.List;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

@Slf4j
public class CodeReviewPanel {
    private JPanel panel = new JPanel();
    private ComboBox<OwnerDTO> ownerComboBox;
    private JTextField feedBackText;
    private JButton refreshButton;
    private ComboBox<LabelDTO> labelComboBox;

    public CodeReviewPanel() {
        initOwnerComboBox();
        initLabelComboBox();
        feedBackText = new JTextField();
        feedBackText.setPreferredSize(new Dimension(200, 30));
        panel.add(feedBackText);
        refreshButton = new JButton("refresh");
        refreshButton.addActionListener(actionEvent -> {
            // refresh the member and the label data
        });
        panel.add(refreshButton);

    }

    private void initOwnerComboBox() {
        List<TrelloBoardMember> trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
        CollectionComboBoxModel comboBoxModel = new CollectionComboBoxModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers));
        ownerComboBox = new ComboBox(comboBoxModel);
        ownerComboBox.setRenderer(new OwnerComboboxRenderer());
        ownerComboBox.setEditable(true);
        OwnerDtoToStringConverter stringConverter = new OwnerDtoToStringConverter();
        AutoCompleteComboBoxEditor editor = new AutoCompleteComboBoxEditor(ownerComboBox.getEditor(), stringConverter);
        JTextComponent editorComponent = (JTextComponent) ownerComboBox.getEditor().getEditorComponent();
        AutoCompleteDocument autoCompleteDocument = new AutoCompleteDocument(new ComboBoxAdaptor(ownerComboBox), false, stringConverter);
        editorComponent.setDocument(autoCompleteDocument);
        ownerComboBox.setEditor(editor);
        ownerComboBox.setMaximumRowCount(3);
        ownerComboBox.setSelectedItem(null); // remember last selected user
        ownerComboBox.setToolTipText("owner");
        panel.add(ownerComboBox);
    }

    private void initLabelComboBox() {
        List<TrelloBoardLabel> trelloBoardLabels = TrelloBoardLabelState.getInstance().getState().getTrelloBoardLabels();
        CollectionComboBoxModel comboBoxModel = new CollectionComboBoxModel(MEMBER_MAPPER.toLabelDtoList(trelloBoardLabels));
        labelComboBox = new ComboBox(comboBoxModel);
        labelComboBox.setRenderer(new LabelComboboxRenderer());
        labelComboBox.setEditable(true);
        AutoCompleteComboBoxEditor editor = new AutoCompleteComboBoxEditor(labelComboBox.getEditor(), ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        JTextComponent editorComponent = (JTextComponent) labelComboBox.getEditor().getEditorComponent();
        AutoCompleteDocument autoCompleteDocument = new AutoCompleteDocument(new ComboBoxAdaptor(labelComboBox), false, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        editorComponent.setDocument(autoCompleteDocument);
        labelComboBox.setEditor(editor);
        labelComboBox.setMaximumRowCount(3);
        labelComboBox.setSelectedItem(null);
        labelComboBox.setToolTipText("label");
        panel.add(labelComboBox);
    }

    public JPanel getPanel() {
        return panel;
    }

    public ComboBox getOwnerComboBox() {
        return ownerComboBox;
    }

    public FeedBackContext getFeedbackContext() {
        return FeedBackContext.builder()
                .feedback(feedBackText.getText())
                .memberList(Collections.singletonList(MEMBER_MAPPER.toMember(ownerComboBox.getItem())))
                .label(MEMBER_MAPPER.toLabel(labelComboBox.getItem()))
                .build();
    }
}
