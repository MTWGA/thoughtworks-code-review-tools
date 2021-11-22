package net.lihui.app.plugin.thoughtworkscodereviewtools.client;

import com.google.gson.Gson;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.JDKTrelloHttpClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.entity.TrelloList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class TrelloClient {
    private final TrelloConfiguration trelloConfiguration;
    private final Trello trelloApi;
    private static Logger log = LoggerFactory.getLogger(TrelloClient.class);
    Gson gson = new Gson();

    public TrelloClient(TrelloConfiguration trelloConfiguration) {
        this.trelloConfiguration = trelloConfiguration;
        this.trelloApi = new TrelloImpl(trelloConfiguration.getTrelloApiKey(), trelloConfiguration.getTrelloApiToken(), new JDKTrelloHttpClient());
    }

    // TODO add cache
    public List<TList> getBoardListCollection() {
        Board board = trelloApi.getBoard(trelloConfiguration.getTrelloBoardId());
        return board.fetchLists();
    }


    public String createListIfNotExist(String trelloListName) {
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

    // TODO add cache
    public List<Member> getBoardMembers() {
        return trelloApi.getBoardMembers(trelloConfiguration.getTrelloBoardId());
    }

    public Card createCard(String todayCodeReviewListId, Card card) {
        return trelloApi.createCard(todayCodeReviewListId, card);
    }

    public Member getAuthorMember() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.trello.com/1/tokens/" + trelloConfiguration.getTrelloApiToken() + "/member";
        URI fullUri = UriComponentsBuilder.fromUriString(url)
                .queryParam("key", trelloConfiguration.getTrelloApiKey())
                .queryParam("token", trelloConfiguration.getTrelloApiToken())
                .queryParam("fields", "id")
                .buildAndExpand().toUri();
        Member authorMember = restTemplate.getForObject(fullUri, Member.class);
        log.info("AuthorMember Data: {}", gson.toJson(authorMember));
        return authorMember;
    }
}
