package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeReviewFeedbackDialog extends DialogWrapper {
    public static final String DIALOG_TITLE = "code review feedback";
    private final CodeReviewFeedbackPanel codeReviewFeedbackPanel;

    public CodeReviewFeedbackDialog(@Nullable Project project) {
        super(project);
        codeReviewFeedbackPanel = new CodeReviewFeedbackPanel();
        setTitle(DIALOG_TITLE);
        setOKButtonText("OK");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return codeReviewFeedbackPanel.getMainPanel();
    }

    public FeedBackContext getFeedbackContext() {
        return codeReviewFeedbackPanel.getFeedbackContext();
    }
}
