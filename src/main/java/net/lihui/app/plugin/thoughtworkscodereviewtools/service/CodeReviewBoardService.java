package net.lihui.app.plugin.thoughtworkscodereviewtools.service;

import com.ibm.icu.text.SimpleDateFormat;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.FeedBackAndMemberList;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CodeReviewBoardService {
    private final TrelloClient trelloClient;

    public CodeReviewBoardService(TrelloClient trelloClient) {
        this.trelloClient = trelloClient;
    }

    public String getTodayCodeReviewListId() {
        List<TList> boardListCollection = trelloClient.getBoardListCollection();
        String codeReviewListName = buildTodayCodeReviewListName();
        return boardListCollection.stream()
                .filter(list -> list.getName().equals(codeReviewListName))
                .findFirst()
                .map(TList::getId)
                .orElseGet(() -> trelloClient.createList(codeReviewListName));
    }

    public Card createCodeReviewCard(FeedBackAndMemberList feedBackAndMemberList, String cardDesc, String todayCodeReviewListId) {
        Card card = new Card();
        card.setName(feedBackAndMemberList.getFeedback());
        card.setDesc(cardDesc);
        List<String> submitMemberIdList = feedBackAndMemberList.getMemberList().stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        card.setIdMembers(submitMemberIdList);
        return trelloClient.createCard(todayCodeReviewListId, card);
    }

    public List<Member> getTrelloBoardMembers() {
        return trelloClient.getBoardMembers();
    }

    private String buildTodayCodeReviewListName() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }
}
