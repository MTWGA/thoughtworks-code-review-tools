package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeReviewFeedbackDialog extends DialogWrapper {
    private final CodeReviewFeedbackPanel codeReviewFeedbackPanel;

    public CodeReviewFeedbackDialog(@Nullable Project project) {
        super(project);
        codeReviewFeedbackPanel = new CodeReviewFeedbackPanel();
        setTitle("code review feedback");
        setOKButtonText("OK");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return codeReviewFeedbackPanel.getMainPanel();
    }

    public String getCodeReviewFeedback() {
        return codeReviewFeedbackPanel.getFeedback();
    }
}
