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
import net.lihui.app.plugin.thoughtworkscodereviewtools.entity.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.entity.TrelloList;
import net.lihui.app.plugin.thoughtworkscodereviewtools.notification.MyNotifier;
import net.lihui.app.plugin.thoughtworkscodereviewtools.store.TwCodeReviewSettingsState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

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

        TwCodeReviewSettingsState trelloSettings = TwCodeReviewSettingsState.getInstance();
        TrelloConfiguration trelloConfiguration = new TrelloConfiguration(trelloSettings.trelloApiKey, trelloSettings.trelloApiToken, trelloSettings.trelloBoardId);

        if (trelloConfiguration.isInvalid()) {
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

        Trello trelloApi = new TrelloImpl(trelloConfiguration.getTrelloApiKey(), trelloConfiguration.getTrelloApiToken(), new JDKTrelloHttpClient());
        List<TList> boardListCollection = getBoardListCollection(trelloConfiguration.getTrelloBoardId(), trelloApi::getBoard);
        String codeReviewListName = buildTodayCodeReviewListName();
        String todayCodeReviewListId = getTodayCodeReviewListId(trelloConfiguration, boardListCollection, codeReviewListName);

        String cardDesc = buildCardDesc(actionEvent, projectName);
        if (cardDesc == null) {
            return;
        }

        Card codeReviewCard = createCodeReviewCard(trelloApi, todayCodeReviewListId, trelloConfiguration.getTrelloBoardId(), cardDesc, input);
        if (codeReviewCard.getName().equals(input)) {
            MyNotifier.notifyInfo(actionEvent.getProject(), "信息发送成功" + codeReviewCard.getName() + ":" + codeReviewCard.getDesc());
        }
    }

    private String getTodayCodeReviewListId(TrelloConfiguration trelloConfiguration, List<TList> boardListCollection, String codeReviewListName) {
        return boardListCollection.stream()
                .filter(list -> list.getName().equals(codeReviewListName))
                .findFirst()
                .map(TList::getId)
                .orElseGet(() -> createTodayCodeReviewList(trelloConfiguration, codeReviewListName));
    }

    private Card createCodeReviewCard(Trello trelloApi, String todayCodeReviewListId, String boardId, String cardDesc, String input) {
        String currentMemberName = input.split(" ")[0];
        Card card = new Card();
        card.setName(input);
        card.setDesc(cardDesc);
        log.info("card info: {}", card);
        List<Member> memberList = trelloApi.getBoardMembers(boardId);
        memberList.stream()
                .filter(member -> member.getFullName().equals(currentMemberName))
                .findFirst()
                .ifPresent(member -> card.setIdMembers(Collections.singletonList(member.getId())));

        return trelloApi.createCard(todayCodeReviewListId, card);
    }

    private List<TList> getBoardListCollection(String boardId, Function<String, Board> getBoardFunction) {
        Board board = getBoardFunction.apply(boardId);
        List<TList> trelloListCollection = board.fetchLists();
        log.info("tlist: {}", JSON.toJSONString(trelloListCollection));
        return trelloListCollection;
    }

    private String buildTodayCodeReviewListName() {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记
        Date date = new Date();
        String trelloListName = sdf.format(date);
        log.info(trelloListName);
        return trelloListName;
    }

    private String createTodayCodeReviewList(TrelloConfiguration trelloConfiguration, String trelloListName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.trello.com/1/boards/" + trelloConfiguration.getTrelloBoardId() + "/lists";
        URI fullUri = UriComponentsBuilder.fromUriString(url)
                .queryParam("name", trelloListName)
                .queryParam("key", trelloConfiguration.getTrelloApiKey())
                .queryParam("token", trelloConfiguration.getTrelloApiToken())
                .buildAndExpand().toUri();
        TrelloList trelloList = restTemplate.postForObject(fullUri, null, TrelloList.class);
        return trelloList.getId();
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
