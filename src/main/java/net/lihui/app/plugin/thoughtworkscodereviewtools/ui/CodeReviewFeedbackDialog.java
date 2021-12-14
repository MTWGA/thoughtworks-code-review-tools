package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeReviewFeedbackDialog extends DialogWrapper {
    private static final String DIALOG_TITLE = "code review feedback";
    private static final String OK_BUTTON_TEXT = "OK";
    private final CodeReviewPanel codeReviewPanel;

    public CodeReviewFeedbackDialog(@Nullable Project project) {
        super(project);
        codeReviewPanel = new CodeReviewPanel();
        setTitle(DIALOG_TITLE);
        setOKButtonText(OK_BUTTON_TEXT);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return codeReviewPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return codeReviewPanel.getPreferredFocusedComponent();
    }

    public FeedbackContext getFeedbackContext() {
        return codeReviewPanel.getFeedbackContext();
    }
}
