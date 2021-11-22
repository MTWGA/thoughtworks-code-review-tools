package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import javax.swing.*;

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
        ownerListTable.setModel(new OwnerCheckboxTableModel());
        ownerListTable.setDefaultRenderer(JCheckBox.class, new TableCheckboxCellRenderer());
        ownerListTable.setDefaultEditor(JCheckBox.class, new TableCheckboxCellEditor());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getFeedback() {
        return feedbackTextField.getText();
    }
}
