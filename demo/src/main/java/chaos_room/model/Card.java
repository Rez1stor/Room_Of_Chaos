package chaos_room.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private String id;
    private String name;
    private String type;
    private String description;

    public Card() {}

    public Card(String id, String name, String type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }
    

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("Card{id='%s', name='%s', type='%s', description='%s'}", id, name, type, description);
    }

    public static Card getRandomCardFromJson(String jsonFilePath) {
        try{
            FileReader reader = new FileReader(Card.class.getClassLoader().getResource(jsonFilePath).getPath());
            ObjectMapper json = new ObjectMapper();
            List<Card> cards = json.readValue(reader, new TypeReference<List<Card>>() {}); 
            if (cards == null || cards.size() == 0) {
                return null;
            }
            int idx = (int) (Math.random() * cards.size());
            System.out.println("Wylosowana karta: " + cards.get(idx).getName());
            reader.close();
            return cards.get(idx);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}