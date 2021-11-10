package net.lihui.app.plugin.thoughtworkscodereviewtools.client;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.JDKTrelloHttpClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.config.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.entity.TrelloList;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class TrelloClient {
    private final TrelloConfiguration trelloConfiguration;
    private final Trello trelloApi;

    public TrelloClient(TrelloConfiguration trelloConfiguration) {
        this.trelloConfiguration = trelloConfiguration;
        this.trelloApi = new TrelloImpl(trelloConfiguration.getTrelloApiKey(), trelloConfiguration.getTrelloApiToken(), new JDKTrelloHttpClient());
    }

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

    public List<Member> getBoardMembers() {
        return trelloApi.getBoardMembers(trelloConfiguration.getTrelloBoardId());
    }

    public Card createCard(String todayCodeReviewListId, Card card) {
        return trelloApi.createCard(todayCodeReviewListId, card);
    }
}
