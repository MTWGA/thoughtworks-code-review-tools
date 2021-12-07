package net.lihui.app.plugin.thoughtworkscodereviewtools.service;

import com.ibm.icu.text.SimpleDateFormat;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabel;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.FeedBackContext;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

public class CodeReviewBoardService {
    private final TrelloClient trelloClient;

    public CodeReviewBoardService(TrelloConfiguration trelloConfiguration) {
        this.trelloClient = new TrelloClient(trelloConfiguration);
    }

    public String getTodayCodeReviewListId() {
        List<TList> boardListCollection = trelloClient.getBoardListCollection();
        String codeReviewListName = buildTodayCodeReviewListName();
        return boardListCollection.stream()
                .filter(list -> list.getName().equals(codeReviewListName))
                .findFirst()
                .map(TList::getId)
                .orElseGet(() -> trelloClient.createBoardList(codeReviewListName));
    }

    public Card createCodeReviewCard(FeedBackContext feedBackContext, String cardDesc, String todayCodeReviewListId) {
        Card card = new Card();
        card.setName(feedBackContext.getFeedback());
        card.setDesc(cardDesc);
        List<String> submitMemberIdList = feedBackContext.getMemberList().stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        card.setIdMembers(submitMemberIdList);

        return trelloClient.createCard(todayCodeReviewListId, card, feedBackContext.getLabel().getId());
    }

    public List<TrelloBoardMember> getTrelloBoardMembers() {
        List<Member> boardMembers = trelloClient.getBoardMembers();
        return MEMBER_MAPPER.toStateList(boardMembers);
    }

    public List<TrelloBoardLabel> getTrelloBoardLabels() {
        return MEMBER_MAPPER.toLabelList(trelloClient.getLabels());
    }

    private String buildTodayCodeReviewListName() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }
}
