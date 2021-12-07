package net.lihui.app.plugin.thoughtworkscodereviewtools.client;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.TrelloUrl;
import com.julienvey.trello.impl.http.JDKTrelloHttpClient;
import lombok.extern.slf4j.Slf4j;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.response.TrelloBoardListResponse;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
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

    public String createBoardList(String trelloListName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = TrelloUrl.API_URL + TrelloUrl.GET_BOARD_LISTS;
        URI fullUri = UriComponentsBuilder.fromUriString(url)
                .queryParam("name", trelloListName)
                .queryParam("key", trelloConfiguration.getTrelloApiKey())
                .queryParam("token", trelloConfiguration.getTrelloApiToken())
                .buildAndExpand(trelloConfiguration.getTrelloBoardId()).toUri();

        TrelloBoardListResponse trelloBoardListResponse = restTemplate.postForObject(fullUri, null, TrelloBoardListResponse.class);
        return trelloBoardListResponse.getId();
    }

    public List<Member> getBoardMembers() {
        return trelloApi.getBoardMembers(trelloConfiguration.getTrelloBoardId());
    }

    public Card createCard(String boardListId, Card card) {
        return trelloApi.createCard(boardListId, card);
    }

    public List<Label> getLabels() {
        return trelloApi.getBoardLabels(trelloConfiguration.getTrelloBoardId());
    }
}
