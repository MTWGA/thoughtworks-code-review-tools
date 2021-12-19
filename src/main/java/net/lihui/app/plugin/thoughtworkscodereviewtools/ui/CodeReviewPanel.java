package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import com.julienvey.trello.domain.Member;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.notification.Notifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabel;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabelState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.LabelDTO;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerDTO;
import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.LabelMapper.LABEL_MAPPER;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

public class CodeReviewPanel extends JPanel {
    private static final int DEFAULT_COMBO_BOX_DISPLAY_COUNT = 5;
    private ComboBox<OwnerDTO> ownerComboBox;
    private JTextField feedbackTextField;
    private JButton refreshButton;
    private ComboBox<LabelDTO> labelComboBox;

    public CodeReviewPanel() {
        initOwnerComboBox();
        initLabelComboBox();
        initFeedbackTextField();
        initRefreshButton();
    }

    private void initRefreshButton() {
        refreshButton = new JButton(AllIcons.Actions.Refresh);
        refreshButton.addActionListener(this::refreshAction);
        refreshButton.setPreferredSize(new Dimension(30, 30));
        this.add(refreshButton);
    }

    private void initFeedbackTextField() {
        feedbackTextField = new JTextField();
        feedbackTextField.setPreferredSize(new Dimension(200, 30));
        this.add(feedbackTextField);
    }

    private void refreshLabels() {
        List<TrelloBoardLabel> trelloBoardLabels = TrelloBoardLabelState.getInstance().getState().getTrelloBoardLabels();
        labelComboBox.setModel(new CollectionComboBoxModel(LABEL_MAPPER.toLabelDtoList(trelloBoardLabels)));
    }

    private void refreshOwners() {
        List<TrelloBoardMember> trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
        ownerComboBox.setModel(new CollectionComboBoxModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers)));
    }

    private void updateBoardState() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);
        codeReviewBoardService.updateBoardState();
    }

    private void initOwnerComboBox() {
        List<TrelloBoardMember> trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
        CollectionComboBoxModel comboBoxModel = new CollectionComboBoxModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers));
        ownerComboBox = new ComboBox(comboBoxModel);
        OwnerDtoToStringConverter stringConverter = new OwnerDtoToStringConverter();
        setupComboBox(ownerComboBox, stringConverter, new OwnerComboboxRenderer(), "owner");
    }

    private void initLabelComboBox() {
        List<TrelloBoardLabel> trelloBoardLabels = TrelloBoardLabelState.getInstance().getState().getTrelloBoardLabels();
        CollectionComboBoxModel comboBoxModel = new CollectionComboBoxModel(LABEL_MAPPER.toLabelDtoList(trelloBoardLabels));
        labelComboBox = new ComboBox(comboBoxModel);
        LabelDtoToStringConverter stringConverter = new LabelDtoToStringConverter();
        setupComboBox(labelComboBox, stringConverter, new LabelComboboxRenderer(), "Label");
    }

    private void setupComboBox(ComboBox comboBox, ObjectToStringConverter converter, ListCellRenderer renderer, String tipText) {
        comboBox.setRenderer(renderer);
        comboBox.setEditable(true);
        AutoCompleteComboBoxEditor editor = new AutoCompleteComboBoxEditor(comboBox.getEditor(), converter);
        comboBox.setEditor(editor);
        JTextComponent editorComponent = (JTextComponent) editor.getEditorComponent();
        AutoCompleteDocument autoCompleteDocument = new AutoCompleteDocument(new ComboBoxAdaptor(comboBox), false, converter);
        editorComponent.setDocument(autoCompleteDocument);
        editorComponent.addFocusListener(createFocusListener(comboBox));
        comboBox.setMaximumRowCount(DEFAULT_COMBO_BOX_DISPLAY_COUNT);
        comboBox.setToolTipText(tipText);
        this.add(comboBox);
    }

    private FocusListener createFocusListener(ComboBox comboBox) {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboBox.showPopup();
            }

            @Override
            public void focusLost(FocusEvent e) {
                comboBox.hidePopup();
            }
        };
    }

    public FeedbackContext getFeedbackContext() {
        return FeedbackContext.builder()
                .feedback(feedbackTextField.getText())
                .member(getSelectedMember())
                .label(getSelectedLabel())
                .build();
    }

    private com.julienvey.trello.domain.Label getSelectedLabel() {
        return labelComboBox.getItem() instanceof LabelDTO ? LABEL_MAPPER.toLabel(labelComboBox.getItem()) : null;
    }

    private Member getSelectedMember() {
        return ownerComboBox.getItem() instanceof OwnerDTO ? MEMBER_MAPPER.toMember(ownerComboBox.getItem()) : null;
    }

    public JComponent getPreferredFocusedComponent() {
        return ownerComboBox;
    }

    private void refreshAction(ActionEvent actionEvent) {
        try {
            updateBoardState();
            refreshOwners();
            refreshLabels();
            Notifier.notifyInfo(null, "refresh data success");
        } catch (Exception e) {
            Notifier.notifyError(null, "refresh failed please check network connection or check trello setting");
        }

    }
}
