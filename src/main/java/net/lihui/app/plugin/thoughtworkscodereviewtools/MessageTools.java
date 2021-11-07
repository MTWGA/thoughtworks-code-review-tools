package net.lihui.app.plugin.thoughtworkscodereviewtools;

import com.alibaba.fastjson.JSON;
import com.ibm.icu.text.SimpleDateFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.JDKTrelloHttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.lihui.app.plugin.thoughtworkscodereviewtools.notification.MyNotifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.store.TwCodeReviewSettingsState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

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

        TwCodeReviewSettingsState settings = TwCodeReviewSettingsState.getInstance();
        String trelloKey = settings.trelloApiKey;
        String trelloAccessToken = settings.trelloApiToken;
        String boardId = settings.trelloBoardId;

        if (trelloKey.isBlank() || trelloAccessToken.isBlank() || boardId.isBlank()) {
            MyNotifier.notifyError(project, "您尚未配置 Trello 信息，请补全 Trello 配置信息 设置路径 Preferences -> Tw Code Review Tools 中设置");
        }
        String classPath = psiFile.getVirtualFile().getPath();
        String title = "提交 Code Review 信息";
        String input = Messages.showInputDialog(project, classPath, title, null);
        log.info(input);

        if (input == null || input.isEmpty()) {
            return;
        }

        Trello trelloApi = new TrelloImpl(trelloKey, trelloAccessToken, new JDKTrelloHttpClient());
        Board board = trelloApi.getBoard(boardId);
        List<TList> tLists = board.fetchLists();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记
        Date date = new Date();
        String formatData = sdf.format(date);
        log.info(formatData);
        log.info("tlist: {}", JSON.toJSONString(tLists));

        Optional<TList> todayCard = tLists.stream().filter(tList -> tList.getName().equals(formatData)).findAny();
        // TODO：this request should been solved by sdk
        if (todayCard.isEmpty()) {
            HttpResponse<JsonNode> response = null;
            try {
                response = Unirest.post("https://api.trello.com/1/boards/" + boardId + "/lists")
                        .header("Accept", "application/json")
                        .queryString("name", formatData)
                        .queryString("key", trelloKey)
                        .queryString("token", trelloAccessToken)
                        .asJson();
            } catch (UnirestException ex) {
                log.error("请求获取trello board list失败", ex);
            }
            if (response == null) {
                log.error("请求获取trello board list失败");
                return;
            }
            log.info("response data: {}", response.getBody());
        }

        tLists = board.fetchLists();
        todayCard = tLists.stream().filter(tList -> tList.getName().equals(formatData)).findAny();
        Editor editor = actionEvent.getData(CommonDataKeys.EDITOR);
        String selectedText = editor != null ? editor.getSelectionModel().getSelectedText() : "";
        // deal with the input data  get member and get data title

        String memberName = input.split(" ")[0];

        String projectName = project.getName();
        String cardDesc = getCardDesc(actionEvent, selectedText, projectName);
        if (cardDesc == null) return;

        Card card = new Card();
        card.setName(input);
        card.setDesc(cardDesc);
        log.info("card info: {}", card);
        List<Member> memberList = trelloApi.getBoardMembers(boardId);
        Optional<Member> member = memberList.stream().filter(member1 -> member1.getFullName().equals(memberName)).findAny();
        if (member.isPresent()) {
            card.setIdMembers(Collections.singletonList(member.get().getId()));
        }
        if (todayCard.isPresent()) {
            card = trelloApi.createCard(todayCard.get().getId(), card);
            if (card.getName().equals(input)) {
                MyNotifier.notifyInfo(actionEvent.getProject(), "信息发送成功" + card.getName() + ":" + card.getDesc());
            }
        }
    }

    @Nullable
    private String getCardDesc(AnActionEvent actionEvent, String selectedText, String projectName) {
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
        final String filePath = canonicalPath.substring(
                index + projectName.length() + 1
        );
        return "### " + projectName + '\n' + filePath + "\n" + "\n> " + selectedText;
    }
}
