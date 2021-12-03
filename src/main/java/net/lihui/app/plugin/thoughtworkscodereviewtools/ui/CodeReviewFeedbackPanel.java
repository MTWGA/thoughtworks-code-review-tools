package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerCheckboxDTO;

import javax.swing.*;
import java.util.List;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

public class CodeReviewFeedbackPanel {
    private JPanel mainPanel;
    private JLabel ownerSelectionLabel;
    private JPanel ownerSelectionPanel;
    private JScrollPane ownerCheckboxScrollPanel;
    private JTable ownerListTable;
    private JPanel refreshOwnerListButtonPanel;
    private JButton refreshOwnerListButton;
    private JLabel feedbackLabel;
    private JTextField feedbackTextField;

    public CodeReviewFeedbackPanel() {
        List<TrelloBoardMember> trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
        ownerListTable.setModel(new OwnerCheckboxTableModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers)));
        ownerListTable.setDefaultRenderer(OwnerCheckboxDTO.class, new TableCheckboxCellRenderer());
        ownerListTable.setDefaultEditor(OwnerCheckboxDTO.class, new TableCheckboxCellEditor());
        ownerListTable.setShowGrid(true);

        refreshOwnerListButton.addActionListener(actionEvent -> refreshBoardMemberList());
    }

    private void refreshBoardMemberList() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);

        List<TrelloBoardMember> refreshedMemberList = codeReviewBoardService.getTrelloBoardMembers();
        TrelloBoardMemberState boardMemberState = TrelloBoardMemberState.getInstance();
        boardMemberState.updateTrelloBoardMemberList(refreshedMemberList);

        ownerListTable.setModel(new OwnerCheckboxTableModel(MEMBER_MAPPER.toDtoList(refreshedMemberList)));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public FeedBackContext getFeedbackContext() {
        OwnerCheckboxTableModel tableModel = (OwnerCheckboxTableModel)ownerListTable.getModel();
        return FeedBackContext.builder()
                .feedback(feedbackTextField.getText())
                .memberList(tableModel.getSelectedMembers())
                .build();
    }
}
