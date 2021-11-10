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
import net.lihui.app.plugin.thoughtworkscodereviewtools.entity.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.notification.MyNotifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.store.TwCodeReviewSettingsState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class MessageTools extends AnAction {
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
            MyNotifier.notifyError(project, "您尚未配置 Trello 信息，请补全 Trello 配置信息 设置路径 Preferences -> Tw Code Review Tools 中设置");
            return;
        }

        String projectName = project.getName();
        String title = "提交 Code Review 信息";
        String filePath = getFilePath(actionEvent, projectName);
        String userInput = Messages.showInputDialog(project, filePath, title, null);
        if (isEmpty(userInput)) {
            return;
        }
        String cardDesc = buildCardDesc(actionEvent, projectName);
        if (cardDesc == null) {
            return;
        }

        TrelloClient trelloClient = new TrelloClient(trelloConfiguration);
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloClient);
        String todayCodeReviewListId = codeReviewBoardService.getTodayCodeReviewListId();
        Card codeReviewCard = codeReviewBoardService.createCodeReviewCard(userInput, cardDesc, todayCodeReviewListId);

        if (codeReviewCard.getName().equals(userInput)) {
            MyNotifier.notifyInfo(actionEvent.getProject(), "信息发送成功" + codeReviewCard.getName() + ":" + codeReviewCard.getDesc());
        }
    }

    private String buildCardDesc(AnActionEvent actionEvent, String projectName) {
        Editor editor = actionEvent.getData(CommonDataKeys.EDITOR);
        String selectedText = editor != null ? editor.getSelectionModel().getSelectedText() : "";
        if (selectedText == null) {
            selectedText = "";
        }

        final String filePath = getFilePath(actionEvent, projectName);
        if (filePath == null) return null;

        return "### " + projectName + '\n' + filePath + "\n" + "\n> " + selectedText;
    }

    private String getFilePath(AnActionEvent actionEvent, String projectName) {
        final VirtualFile file = actionEvent.getData(VIRTUAL_FILE);
        String canonicalPath = null;
        if (file != null) {
            canonicalPath = file.getCanonicalPath();
        }
        if (canonicalPath == null) {
            log.error("canonicalPath should not be null");
            return null;
        }
        final int index = canonicalPath.indexOf(projectName);
        return canonicalPath.substring(
                index + projectName.length() + 1
        );
    }
}
