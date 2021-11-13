package net.lihui.app.plugin.thoughtworkscodereviewtools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.julienvey.trello.domain.Card;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.config.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.notification.Notifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.vo.UserSelectedInfo;
import net.lihui.app.plugin.thoughtworkscodereviewtools.store.TwCodeReviewSettingsState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class CodeReviewFeedbackAction extends AnAction {
    private static final String CARD_DESCRIPTION_TEMPLATE = "### %s%n%s%n%n> %s";
    private static final String DIALOG_TITLE = "提交 Code Review 信息";
    private static final String SET_UP_NOTIFICATION = "您尚未配置 Trello 信息，请补全 Trello 配置信息 设置路径 Preferences -> Tw Code Review Tools 中设置";
    private final Logger log = LoggerFactory.getLogger(CodeReviewFeedbackAction.class);

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        Project project = actionEvent.getData(CommonDataKeys.PROJECT);
        log.info("project info : {}", project);

        TwCodeReviewSettingsState trelloSettings = TwCodeReviewSettingsState.getInstance();
        TrelloConfiguration trelloConfiguration = new TrelloConfiguration(trelloSettings.trelloApiKey, trelloSettings.trelloApiToken, trelloSettings.trelloBoardId);
        if (trelloConfiguration.isInvalid()) {
            Notifier.notifyError(project, SET_UP_NOTIFICATION);
            return;
        }

        UserSelectedInfo userSelectedInfo = new UserSelectedInfo(actionEvent);
        String userInput = Messages.showInputDialog(project, userSelectedInfo.getSelectedFilePath(), DIALOG_TITLE, null);
        if (isEmpty(userInput)) {
            return;
        }

        TrelloClient trelloClient = new TrelloClient(trelloConfiguration);
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloClient);

        String cardDesc = buildCardDesc(userSelectedInfo);
        String todayCodeReviewListId = codeReviewBoardService.getTodayCodeReviewListId();
        Card codeReviewCard = codeReviewBoardService.createCodeReviewCard(userInput, cardDesc, todayCodeReviewListId);

        if (codeReviewCard.getName().equals(userInput)) {
            Notifier.notifyInfo(project, "信息发送成功" + codeReviewCard.getName() + ":" + codeReviewCard.getDesc());
        }
    }

    private String buildCardDesc(UserSelectedInfo userSelectedInfo) {
        return String.format(CARD_DESCRIPTION_TEMPLATE, userSelectedInfo.getProjectName(),
                userSelectedInfo.getSelectedFilePath(), userSelectedInfo.getSelectedText());
    }
}