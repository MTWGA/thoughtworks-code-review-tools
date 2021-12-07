package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeReviewFeedbackDialog extends DialogWrapper {
    public static final String DIALOG_TITLE = "code review feedback";
    private final CodeReviewPanel codeReviewPanel;

    public CodeReviewFeedbackDialog(@Nullable Project project) {
        super(project);
        codeReviewPanel = new CodeReviewPanel();
        setTitle(DIALOG_TITLE);
        setOKButtonText("OK");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return codeReviewPanel.getPanel();

    }

    public FeedBackContext getFeedbackContext() {
        codeReviewPanel.getOwnerComboBox();
        return codeReviewPanel.getFeedbackContext();
    }
}
