package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.julienvey.trello.NotAuthorizedException;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.exception.BaseException;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.notification.Notifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.CodeReviewFeedbackDialog;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.FeedbackContext;

import static java.util.Objects.isNull;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant.AUTHORIZED_FAILED_FILL_SETTING_NOTIFICATION;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant.BOARD_ID_INVALID_ERROR_MESSAGE;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant.INVALID_BOARD_ID_NOTIFICATION;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant.SET_UP_NOTIFICATION;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class CodeReviewFeedbackAction extends AnAction {
    private static final String CARD_DESCRIPTION_TEMPLATE = "### %s%n%s%n%n> %s";
    private static final String CARD_CREATED_SUCCESSFULLY_MESSAGE_TEMPLATE = "Message sent successfully, %s:%s";
    private static final String CARD_CREATED_FAILED_MESSAGE_TEMPLATE = "Failed to send message, %s:%s";
    private CodeReviewBoardService codeReviewBoardService;

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        // should get project instance before pop up any dialog
        Project project = actionEvent.getProject();

        try {
            doAction(actionEvent);
        } catch (Exception exception) {
            if (exception.getMessage().equals(BOARD_ID_INVALID_ERROR_MESSAGE)) {
                Notifier.notifyError(project, INVALID_BOARD_ID_NOTIFICATION);
            } else if (exception instanceof NotAuthorizedException) {
                Notifier.notifyError(project, AUTHORIZED_FAILED_FILL_SETTING_NOTIFICATION);
            } else {
                Notifier.notifyError(project, exception.getMessage());
            }
        }
    }

    private void doAction(AnActionEvent actionEvent) {
        initCodeReviewBoardService();

        UserSelectedInfo userSelectedInfo = new UserSelectedInfo(actionEvent);
        String cardDesc = buildCardDesc(userSelectedInfo);

        FeedbackContext feedbackContext = showFeedbackDialog(userSelectedInfo.getProject());
        if (isNull(feedbackContext)) return;

        createCodeReviewFeedbackCard(feedbackContext, cardDesc);
        Notifier.notifyInfo(userSelectedInfo.getProject(), String.format(CARD_CREATED_SUCCESSFULLY_MESSAGE_TEMPLATE, feedbackContext.getFeedback(), cardDesc));
    }

    private void initCodeReviewBoardService() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        if (trelloConfiguration.isAnyBlank()) {
            throw new BaseException(SET_UP_NOTIFICATION);
        }
        codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);
    }

    private void createCodeReviewFeedbackCard(FeedbackContext feedbackContext, String cardDesc) {
        String todayCodeReviewListId = codeReviewBoardService.getTodayCodeReviewListId();
        try {
            codeReviewBoardService.createCodeReviewCard(feedbackContext, cardDesc, todayCodeReviewListId);
        } catch (Exception e) {
            throw new BaseException(String.format(CARD_CREATED_FAILED_MESSAGE_TEMPLATE, feedbackContext.getFeedback(), cardDesc));
        }
    }

    private FeedbackContext showFeedbackDialog(Project project) {
        CodeReviewFeedbackDialog codeReviewFeedbackDialog = new CodeReviewFeedbackDialog(project);
        boolean isCommitFeedback = codeReviewFeedbackDialog.showAndGet();
        if (!isCommitFeedback) {
            return null;
        }

        FeedbackContext feedbackContext = codeReviewFeedbackDialog.getFeedbackContext();
        if (isBlank(feedbackContext.getFeedback())) {
            throw new BaseException("feedback can not be blank, please input your feedback!");
        }
        return feedbackContext;
    }

    private String buildCardDesc(UserSelectedInfo userSelectedInfo) {
        return String.format(CARD_DESCRIPTION_TEMPLATE, userSelectedInfo.getProjectName(),
                userSelectedInfo.getSelectedFilePath(), userSelectedInfo.getSelectedText());
    }
}
