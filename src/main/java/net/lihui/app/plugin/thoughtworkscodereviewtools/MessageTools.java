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
    public void actionPerformed(AnActionEvent e) {
        String trelloKey = "5539a8fe5e55167267f18ea549372f0c";
        String trelloAccessToken = "c7c2dfc44d8fcea5884d1d7abf1f608eaee1313e6586687f7c252b1e27be7a1e";
        Project project = e.getData(CommonDataKeys.PROJECT);
        log.info("project info : {}", project);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        log.info("PSIFile : {}", psiFile);
        if (psiFile == null || project == null) {
            return;
        }
        String classPath = psiFile.getVirtualFile().getPath();

        String title = "Hello World!";
        String input = Messages.showInputDialog(project, classPath, title, Messages.getInformationIcon());
        log.info(input);

        if (input == null || input.isEmpty()) {
            return;
        }

//        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow("MyPlugin");
//        ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(e.getProject()).getConsole();
//        Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "MyPlugin Output", false);
//        toolWindow.getContentManager().addContent(content);
//        consoleView.print("Hello from MyPlugin!", ConsoleViewContentType.NORMAL_OUTPUT);

        Trello trelloApi = new TrelloImpl(trelloKey, trelloAccessToken, new JDKTrelloHttpClient());
        String boardId = "OpTkznTN";
        Board board = trelloApi.getBoard(boardId);
        List<TList> tLists = board.fetchLists();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记
        Date date = new Date();
        String formatData = sdf.format(date);
        log.info(formatData);
        log.info("tlist: {}", JSON.toJSONString(tLists));

        Optional<TList> todayCard = tLists.stream().filter(tList -> tList.getName().equals(formatData)).findAny();

        if (todayCard.isEmpty()) {
            // create the t list and get the t list data
            // This code sample uses the  'Unirest' library:
            // http://unirest.io/java.html
            HttpResponse<JsonNode> response = null;
            try {
                response = Unirest.post("https://api.trello.com/1/boards/" + boardId + "/lists")
                        .header("Accept", "application/json")
                        .queryString("name", formatData)
                        .queryString("key", trelloKey)
                        .queryString("token", trelloAccessToken)
                        .asJson();
            } catch (UnirestException ex) {
                ex.printStackTrace();
            }
            log.info("response data: {}", response.getBody());
        }

        tLists = board.fetchLists();
        todayCard = tLists.stream().filter(tList -> tList.getName().equals(formatData)).findAny();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null) {
            selectedText = "";
        }
        // deal with the input data  get member and get data title

        String memberName = input.split(" ")[0];

        String projectName = project.getName();
        final VirtualFile file = e.getData(VIRTUAL_FILE);
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null) {
            log.error("canonicalPath should not be null");
            return;
        }
        final int index = canonicalPath.indexOf(projectName);
        final String filePath = canonicalPath.substring(
                index + projectName.length() + 1
        );
        String cardDesc = "### " + projectName + '\n' + filePath + "\n" + "\n> " + selectedText;

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
                MyNotifier.notifyInfo(e.getProject(), "信息发送成功" + card.getName() + ":" + card.getDesc());
            }
        }
    }
}
