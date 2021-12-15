package net.lihui.app.plugin.thoughtworkscodereviewtools.service;

import com.ibm.icu.text.SimpleDateFormat;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.TList;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabel;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabelState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.FeedbackContext;
import org.apache.commons.lang.time.DateUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.LabelMapper.LABEL_MAPPER;
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

    public Card createCodeReviewCard(FeedbackContext feedBackContext, String cardDesc, String todayCodeReviewListId) {
        Card card = new Card();
        card.setName(feedBackContext.getFeedback());
        card.setDesc(cardDesc);
        Date dueDate = DateUtils.addHours(new Date(), trelloConfiguration.getDueIntervalHours());
        card.setDue(dueDate);
        if (!isNull(feedBackContext.getMember())) {
            card.setIdMembers(Collections.singletonList(feedBackContext.getMember().getId()));
        }

        return trelloClient.createCard(todayCodeReviewListId, card, feedBackContext.getLabel());
    }

    public List<TrelloBoardMember> getTrelloBoardMembers() {
        return MEMBER_MAPPER.toStateList(trelloClient.getBoardMembers());
    }

    public List<TrelloBoardLabel> getTrelloBoardLabels() {
        return LABEL_MAPPER.toLabelList(trelloClient.getLabels());
    }

    private String buildTodayCodeReviewListName() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public void updateBoardState() {
        List<TrelloBoardMember> trelloBoardMembers = getTrelloBoardMembers();
        TrelloBoardMemberState boardMemberState = TrelloBoardMemberState.getInstance();
        boardMemberState.updateTrelloBoardMemberList(trelloBoardMembers);

        List<TrelloBoardLabel> trelloBoardLabels = getTrelloBoardLabels();
        TrelloBoardLabelState boardLabelState = TrelloBoardLabelState.getInstance();
        boardLabelState.updateTrelloBoardLabelList(trelloBoardLabels);
    }
}
