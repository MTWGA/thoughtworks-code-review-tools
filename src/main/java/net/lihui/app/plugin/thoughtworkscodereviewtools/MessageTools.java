package net.lihui.app.plugin.thoughtworkscodereviewtools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.julienvey.trello.domain.Card;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.config.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.notification.Notifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.store.TwCodeReviewSettingsState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class MessageTools extends AnAction {
    private static final String CARD_CONTENT_TEMPLATE = "### %s\\n%s\\n\\n> %s";
    private static final String DIALOG_TITLE = "提交 Code Review 信息";
    private static final String SET_UP_NOTIFICATION = "您尚未配置 Trello 信息，请补全 Trello 配置信息 设置路径 Preferences -> Tw Code Review Tools 中设置";
    private final Logger log = LoggerFactory.getLogger(MessageTools.class);

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        Project project = actionEvent.getData(CommonDataKeys.PROJECT);
        log.info("project info : {}", project);
        PsiFile psiFile = actionEvent.getData(CommonDataKeys.PSI_FILE);
        log.info("PSIFile : {}", psiFile);
        if (psiFile == null || project == null) {
            return;
        }

        TwCodeReviewSettingsState trelloSettings = TwCodeReviewSettingsState.getInstance();
        TrelloConfiguration trelloConfiguration = new TrelloConfiguration(trelloSettings.trelloApiKey, trelloSettings.trelloApiToken, trelloSettings.trelloBoardId);
        if (trelloConfiguration.isInvalid()) {
            Notifier.notifyError(project, SET_UP_NOTIFICATION);
            return;
        }

        String projectName = project.getName();
        String filePath = getFilePath(actionEvent, projectName);
        String userInput = Messages.showInputDialog(project, filePath, DIALOG_TITLE, null);
        if (isEmpty(userInput)) {
            return;
        }

        TrelloClient trelloClient = new TrelloClient(trelloConfiguration);
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloClient);

        String cardDesc = buildCardDesc(actionEvent, projectName);
        String todayCodeReviewListId = codeReviewBoardService.getTodayCodeReviewListId();
        Card codeReviewCard = codeReviewBoardService.createCodeReviewCard(userInput, cardDesc, todayCodeReviewListId);

        if (codeReviewCard.getName().equals(userInput)) {
            Notifier.notifyInfo(actionEvent.getProject(), "信息发送成功" + codeReviewCard.getName() + ":" + codeReviewCard.getDesc());
        }
    }

    private String buildCardDesc(AnActionEvent actionEvent, String projectName) {
        Editor editor = actionEvent.getData(CommonDataKeys.EDITOR);
        String selectedText = editor != null ? editor.getSelectionModel().getSelectedText() : "";

        String filePath = getFilePath(actionEvent, projectName);

        return String.format(CARD_CONTENT_TEMPLATE, projectName, defaultString(filePath), defaultString(selectedText));
    }

    private String getFilePath(AnActionEvent actionEvent, String projectName) {
        VirtualFile file = actionEvent.getData(VIRTUAL_FILE);
        String canonicalPath = null;
        if (file != null) {
            canonicalPath = file.getCanonicalPath();
        }
        if (canonicalPath == null) {
            log.error("canonicalPath should not be null");
            return null;
        }
        int index = canonicalPath.indexOf(projectName);
        return canonicalPath.substring(index + projectName.length() + 1);
    }
}
