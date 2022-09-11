package net.lihui.app.plugin.thoughtworkscodereviewtools.service;

import com.google.common.collect.Lists;
import com.ibm.icu.text.SimpleDateFormat;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.Card;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabel;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabelState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.FeedbackContext;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.LabelMapper.LABEL_MAPPER;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
        Date dueDate = DateUtils.addHours(new Date(), trelloConfiguration.getDueIntervalHours());
        if (isBlank(feedBackContext.getFeedback()) && !isNull(feedBackContext.getLabel())) {
            feedBackContext.setFeedback(feedBackContext.getLabel().getName());
        }
        Card card = Card.builder()
                .idList(todayCodeReviewListId)
                .name(feedBackContext.getFeedback())
                .desc(cardDesc)
                .due(dueDate)
                .build();
        if (nonNull(feedBackContext.getMember())) {
            card.setIdMembers(Collections.singletonList(feedBackContext.getMember().getId()));
        }
        if (nonNull(feedBackContext.getLabel())) {
            card.setIdLabels(Collections.singletonList(feedBackContext.getLabel().getId()));
        }

        return trelloClient.createCard(card);
    }

    public List<TrelloBoardMember> getTrelloBoardMembers() {
        List<Member> boardMembers = trelloClient.getBoardMembers();
        if (StringUtils.isNotBlank(trelloConfiguration.getTrelloDefaultMemberId())) {
            return MEMBER_MAPPER.toStateList(topDefaultMember(boardMembers));
        }
        return MEMBER_MAPPER.toStateList(boardMembers);
    }

    @NotNull
    private List<Member> topDefaultMember(List<Member> boardMembers) {
        Predicate<Member> isDefaultMember = member -> member.getId().equals(trelloConfiguration.getTrelloDefaultMemberId());
        Optional<Member> defaultMember = boardMembers.stream().filter(isDefaultMember).findFirst();
        if (defaultMember.isPresent()) {
            List<Member> sortedMembers = Lists.newArrayList();
            sortedMembers.add(defaultMember.get());
            boardMembers.stream().filter(member -> !isDefaultMember.test(member)).forEach(sortedMembers::add);
            return sortedMembers;
        }
        return boardMembers;
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
        TrelloBoardMemberState boardMemberState = TrelloBoardMemberState.getInstance();
        boardMemberState.updateTrelloBoardMemberList(getTrelloBoardMembers());

        List<TrelloBoardLabel> trelloBoardLabels = getTrelloBoardLabels();
        TrelloBoardLabelState boardLabelState = TrelloBoardLabelState.getInstance();
        boardLabelState.updateTrelloBoardLabelList(trelloBoardLabels);
    }
}
