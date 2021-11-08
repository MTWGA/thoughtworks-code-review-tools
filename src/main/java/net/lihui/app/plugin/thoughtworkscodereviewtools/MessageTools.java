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
import net.lihui.app.plugin.thoughtworkscodereviewtools.entity.TrelloList;
import net.lihui.app.plugin.thoughtworkscodereviewtools.notification.MyNotifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.store.TwCodeReviewSettingsState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
        String projectName = project.getName();
        String title = "提交 Code Review 信息";
        String filePath = getFilePath(actionEvent, projectName);

        String input = Messages.showInputDialog(project, filePath, title, null);
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

        String todayListId;
        if (todayCard.isEmpty()) {
            todayListId = createTodayList(trelloKey, trelloAccessToken, boardId, formatData);
        } else {
            todayListId = todayCard.get().getId();
        }

        Editor editor = actionEvent.getData(CommonDataKeys.EDITOR);
        String selectedText = editor != null ? editor.getSelectionModel().getSelectedText() : "";
        // deal with the input data  get member and get data title

        String memberName = input.split(" ")[0];

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
        card = trelloApi.createCard(todayListId, card);
        if (card.getName().equals(input)) {
            MyNotifier.notifyInfo(actionEvent.getProject(), "信息发送成功" + card.getName() + ":" + card.getDesc());
        }
    }

    private String createTodayList(String trelloKey, String trelloAccessToken, String boardId, String formatData) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.trello.com/1/boards/" + boardId + "/lists";
        URI fullUri = UriComponentsBuilder.fromUriString(url)
                .queryParam("name", formatData)
                .queryParam("key", trelloKey)
                .queryParam("token", trelloAccessToken)
                .buildAndExpand().toUri();
        TrelloList trelloList = restTemplate.postForObject(fullUri, null, TrelloList.class);
        System.out.println("create new list id: " + trelloList.getId());
        return trelloList.getId();
    }

    @Nullable
    private String getCardDesc(AnActionEvent actionEvent, String selectedText, String projectName) {
        final String filePath = getFilePath(actionEvent, projectName);
        if (filePath == null) return null;

        if (selectedText == null) {
            selectedText = "";
        }
        return "### " + projectName + '\n' + filePath + "\n" + "\n> " + selectedText;
    }

    @Nullable
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
