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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

public class CodeReviewBoardService {
    private final TrelloClient trelloClient;
    private final TrelloConfiguration trelloConfiguration;

    public CodeReviewBoardService(TrelloConfiguration trelloConfiguration) {
        this.trelloClient = new TrelloClient(trelloConfiguration);
        this.trelloConfiguration = trelloConfiguration;
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
        Date dueDate = new Date(new Date().getTime() + trelloConfiguration.getDueIntervalHours() * 60 * 60 * 1000);
        card.setDue(dueDate);
        if (!isNull(feedBackContext.getMember())) {
            card.setIdMembers(Collections.singletonList(feedBackContext.getMember().getId()));
        }

        return trelloClient.createCard(todayCodeReviewListId, card, feedBackContext.getLabel());
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
