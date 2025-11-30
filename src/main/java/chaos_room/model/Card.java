package chaos_room.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a card in the chaos room.
 */
public class Card {
    /**
     * Shared ObjectMapper instance for JSON serialization/deserialization.
     * ObjectMapper is thread-safe once configured and can be reused across threads.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("value")
    private int value;

    public Card() {
    }

    public Card(int id, String name, String description, int value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Convert this card to JSON string.
     */
    public String toJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    /**
     * Create a Card from JSON string.
     */
    public static Card fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Card.class);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}
