package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.julienvey.trello.domain.Member;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloMemberProperties;
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
    List<TrelloBoardMember> trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
    OwnerCheckboxTableModel tableModel = new OwnerCheckboxTableModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers));

    public CodeReviewFeedbackPanel() {
        ownerListTable.setModel(tableModel);
        ownerListTable.setDefaultRenderer(OwnerCheckboxDTO.class, new TableCheckboxCellRenderer());
        ownerListTable.setDefaultEditor(OwnerCheckboxDTO.class, new TableCheckboxCellEditor());

        refreshOwnerListButton.addActionListener(actionEvent -> {
            fetchBoardMemberList();
            trelloBoardMembers = TrelloBoardMemberState.getInstance().getState().getTrelloBoardMembers();
            tableModel = new OwnerCheckboxTableModel(MEMBER_MAPPER.toDtoList(trelloBoardMembers));
            ownerListTable.setModel(tableModel);
        });
    }

    private void fetchBoardMemberList() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        TrelloBoardMemberState boardMemberState = TrelloBoardMemberState.getInstance();

        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);
        List<Member> trelloBoardMembers = codeReviewBoardService.getTrelloBoardMembers();
        boardMemberState.setTrelloMemberProperties(new TrelloMemberProperties(MEMBER_MAPPER.toStateList(trelloBoardMembers)));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public FeedBackContext getFeedbackContext() {
        return FeedBackContext.builder()
                .feedback(feedbackTextField.getText())
                .memberList(tableModel.getSelectedMembers())
                .build();
    }
}
