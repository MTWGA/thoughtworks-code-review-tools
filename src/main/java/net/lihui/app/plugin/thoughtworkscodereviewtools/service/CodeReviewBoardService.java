package net.lihui.app.plugin.thoughtworkscodereviewtools.service;

import com.ibm.icu.text.SimpleDateFormat;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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
                .orElseGet(() -> trelloClient.createListIfNotExist(codeReviewListName));
    }

    public Card createCodeReviewCard(String userInput, String cardDesc, String todayCodeReviewListId) {
        String currentMemberName = userInput.split(" ")[0];
        Card card = new Card();
        card.setName(userInput);
        card.setDesc(cardDesc);
        if (currentMemberName.equals("me")) {
            Member authorMember = trelloClient.getAuthorMember();
            card.setIdMembers(Collections.singletonList(authorMember.getId()));
        } else {
            List<Member> memberList = trelloClient.getBoardMembers();
            memberList.stream()
                    .filter(member -> member.getFullName().equals(currentMemberName))
                    .findFirst()
                    .ifPresent(member -> card.setIdMembers(Collections.singletonList(member.getId())));
        }
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
