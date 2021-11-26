package thoughtworkscodereviewtools;

import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloUrl;
import net.lihui.app.plugin.thoughtworkscodereviewtools.entity.TrelloList;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Test
    public void testUriBuilder() {
        String url = TrelloUrl.API_URL + TrelloUrl.GET_BOARD_LISTS;
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("name", "name1")
                .queryParam("key", "trelloKey")
                .queryParam("token", "trelloAccessToken")
                .buildAndExpand("12312").toUri();

        assertEquals("https://api.trello.com/1/boards/12312/lists?name=name1&key=trelloKey&token=trelloAccessToken", uri.toString());
    }

    @Test
    public void should_return_new_id_when_or_else_given_list_is_null() {
        Optional<TList> todayCard = Optional.empty();

        String todayListId = todayCard.map(TList::getId).orElse("new Id");

        assertEquals("new Id", todayListId);
    }

    @Test
    public void should_return_exist_id_when_map_to_get_list_id_given_list_is_exist() {
        Optional<TrelloList> todayCard = Optional.of(new TrelloList("exist id"));

        String todayListId = todayCard.map(TrelloList::getId).orElse("new Id");

        assertEquals("exist id", todayListId);
    }
}
