package chaos_room;

import chaos_room.model.Card;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Card class.
 */
public class CardTest {

    @Test
    public void testCardCreation() {
        Card card = new Card(1, "Test Card", "A test card", 50);
        assertEquals(1, card.getId());
        assertEquals("Test Card", card.getName());
        assertEquals("A test card", card.getDescription());
        assertEquals(50, card.getValue());
    }

    @Test
    public void testCardSetters() {
        Card card = new Card();
        card.setId(2);
        card.setName("Updated Card");
        card.setDescription("An updated card");
        card.setValue(100);

        assertEquals(2, card.getId());
        assertEquals("Updated Card", card.getName());
        assertEquals("An updated card", card.getDescription());
        assertEquals(100, card.getValue());
    }

    @Test
    public void testCardToJson() throws JsonProcessingException {
        Card card = new Card(1, "JSON Card", "A JSON card", 75);
        String json = card.toJson();
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"JSON Card\""));
        assertTrue(json.contains("\"description\":\"A JSON card\""));
        assertTrue(json.contains("\"value\":75"));
    }

    @Test
    public void testCardFromJson() throws JsonProcessingException {
        String json = "{\"id\":3,\"name\":\"Parsed Card\",\"description\":\"A parsed card\",\"value\":200}";
        Card card = Card.fromJson(json);
        assertEquals(3, card.getId());
        assertEquals("Parsed Card", card.getName());
        assertEquals("A parsed card", card.getDescription());
        assertEquals(200, card.getValue());
    }

    @Test
    public void testCardRoundTrip() throws JsonProcessingException {
        Card original = new Card(5, "Round Trip", "Testing round trip", 999);
        String json = original.toJson();
        Card parsed = Card.fromJson(json);

        assertEquals(original.getId(), parsed.getId());
        assertEquals(original.getName(), parsed.getName());
        assertEquals(original.getDescription(), parsed.getDescription());
        assertEquals(original.getValue(), parsed.getValue());
    }

    @Test
    public void testCardToString() {
        Card card = new Card(1, "Test", "Test desc", 10);
        String str = card.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("name='Test'"));
        assertTrue(str.contains("description='Test desc'"));
        assertTrue(str.contains("value=10"));
    }
}
