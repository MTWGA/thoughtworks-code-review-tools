package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import javax.swing.*;

public class CodeReviewFeedbackPanel {
    private JPanel mainPanel;
    private JLabel ownerLabel;
    private JPanel ownerCheckListPanel;
    private JScrollPane ownerCheckboxAreaPanel;
    private JTable ownerListTable;
    private JPanel refreshOwnerListButtonPanel;
    private JButton refreshOwnerListButton;
    private JLabel feedbackLabel;
    private JTextField feedbackTextField;

    public CodeReviewFeedbackPanel() {
        ownerListTable.setDefaultRenderer(JCheckBox.class, new TableCheckboxCellRenderer());
        ownerListTable.setModel(new OwnerCheckboxTableModel());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getFeedback() {
        return feedbackTextField.getText();
    }
}
