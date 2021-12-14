package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.julienvey.trello.NotAuthorizedException;
import com.julienvey.trello.TrelloBadRequestException;
import com.julienvey.trello.domain.Card;
import net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.exception.BaseException;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.notification.Notifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.CodeReviewFeedbackDialog;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.FeedbackContext;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class CodeReviewFeedbackAction extends AnAction {
    private static final String CARD_DESCRIPTION_TEMPLATE = "### %s%n%s%n%n> %s";
    private static final String SET_UP_NOTIFICATION = "Please fill in your trello configuration in: Preferences -> TW Code Review Tools";
    private static final String AUTHORIZED_FAIL_EXCEPTION = "Can not access your trello board, please check your trello configuration in: Preferences -> TW Code Review Tools";

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        Project project = actionEvent.getData(CommonDataKeys.PROJECT);
        try {
            doAction(actionEvent);
        } catch (BaseException exception) {
            Notifier.notifyError(project, exception.getMessage());
        }
    }

    private void doAction(AnActionEvent actionEvent) {
        Project project = actionEvent.getData(CommonDataKeys.PROJECT);

        TrelloConfiguration trelloConfiguration = getTrelloConfiguration();

        UserSelectedInfo userSelectedInfo = new UserSelectedInfo(actionEvent);

        FeedbackContext feedbackContext = showFeedbackDialog(project);
        if (isNull(feedbackContext)) return;

        String todayCodeReviewListId = getTodayCodeReviewListId(trelloConfiguration);

        String cardDesc = buildCardDesc(userSelectedInfo);
        createCodeReviewFeedbackCard(project, trelloConfiguration, feedbackContext, cardDesc, todayCodeReviewListId);
    }

    private TrelloConfiguration getTrelloConfiguration() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        try {
            if (trelloConfiguration.isAnyBlank()) {
                throw new BaseException(SET_UP_NOTIFICATION);
            }
        } catch (NullPointerException e) {
            throw new BaseException(SET_UP_NOTIFICATION);

        }
        return trelloConfiguration;
    }

    private void createCodeReviewFeedbackCard(Project project, TrelloConfiguration trelloConfiguration, FeedbackContext feedbackContext, String cardDesc, String todayCodeReviewListId) {
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);
        try {
            Card codeReviewCard = codeReviewBoardService.createCodeReviewCard(feedbackContext, cardDesc, todayCodeReviewListId);
            Notifier.notifyInfo(project, "信息发送成功" + codeReviewCard.getName() + ":" + codeReviewCard.getDesc());
        } catch (Exception e) {
            throw new BaseException("信息发送失败，原消息为：" + feedbackContext.getMember().getFullName() + ":" + cardDesc);
        }
    }

    private String getTodayCodeReviewListId(TrelloConfiguration trelloConfiguration) {
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);
        try {
            return codeReviewBoardService.getTodayCodeReviewListId();
        } catch (NotAuthorizedException notAuthorizedException) {
            throw new BaseException(AUTHORIZED_FAIL_EXCEPTION);
        } catch (TrelloBadRequestException trelloBadRequestException) {
            if (trelloBadRequestException.getMessage().equals("invalid id")) {
                throw new BaseException(TrelloRequestErrorConstant.INVALID_CONFIGURATION);
            } else {
                throw new BaseException("please check your network");
            }
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
