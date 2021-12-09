package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.julienvey.trello.NotAuthorizedException;
import com.julienvey.trello.TrelloBadRequestException;
import com.julienvey.trello.domain.Card;
import net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.notification.Notifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.CodeReviewFeedbackDialog;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.FeedBackContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class CodeReviewFeedbackAction extends AnAction {
    private static final String CARD_DESCRIPTION_TEMPLATE = "### %s%n%s%n%n> %s";
    private static final String SET_UP_NOTIFICATION = "您尚未配置 Trello 信息，请补全 Trello 配置信息 设置路径 Preferences -> Tw Code Review Tools 中设置";
    private static final String AUTHORIZED_FAIL_EXCEPTION = "您配置的 Trello 信息有误，无法获取 Trello 信息，请检查 Trello 配置，设置路径 Preferences -> Tw Code Review Tools";
    private final Logger log = LoggerFactory.getLogger(CodeReviewFeedbackAction.class);

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        Project project = actionEvent.getData(CommonDataKeys.PROJECT);
        log.info("project info : {}", project);

        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        if (trelloConfiguration.isInvalid()) {
            Notifier.notifyError(project, SET_UP_NOTIFICATION);
            return;
        }

        UserSelectedInfo userSelectedInfo = new UserSelectedInfo(actionEvent);
        CodeReviewFeedbackDialog codeReviewFeedbackDialog = new CodeReviewFeedbackDialog(project);
        boolean isCommitFeedback = codeReviewFeedbackDialog.showAndGet();
        if (!isCommitFeedback) {
            return;
        }
        FeedBackContext feedBackContext = codeReviewFeedbackDialog.getFeedbackContext();
        if (isEmpty(feedBackContext.getFeedback())) {
            return;
        }

        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);

        String cardDesc = buildCardDesc(userSelectedInfo);
        String todayCodeReviewListId;
        try {
            todayCodeReviewListId = codeReviewBoardService.getTodayCodeReviewListId();
        } catch (NotAuthorizedException notAuthorizedException) {
            Notifier.notifyError(project, AUTHORIZED_FAIL_EXCEPTION);
            return;
        } catch (TrelloBadRequestException trelloBadRequestException) {
            if (trelloBadRequestException.getMessage().equals("invalid id")) {
                Notifier.notifyError(project, TrelloRequestErrorConstant.INVALID_ID);
            }
            return;
        }
        Card codeReviewCard = new Card();

        try {
            codeReviewCard = codeReviewBoardService.createCodeReviewCard(feedBackContext, cardDesc, todayCodeReviewListId);
        } catch (Exception e) {
            Notifier.notifyInfo(project, "信息发送失败，愿消息为：" + feedBackContext.getMember().getFullName() + ":" + cardDesc);
        }

        if (codeReviewCard.getName().equals(feedBackContext.getFeedback())) {
            Notifier.notifyInfo(project, "信息发送成功" + codeReviewCard.getName() + ":" + codeReviewCard.getDesc());
        }
    }

    private String buildCardDesc(UserSelectedInfo userSelectedInfo) {
        return String.format(CARD_DESCRIPTION_TEMPLATE, userSelectedInfo.getProjectName(),
                userSelectedInfo.getSelectedFilePath(), userSelectedInfo.getSelectedText());
    }
}
