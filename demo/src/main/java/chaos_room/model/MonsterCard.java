package chaos_room.model;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a monster card in the game.
 * Extends the concept of Card with monster-specific properties.
 * Demonstrates OOP principles: inheritance (conceptual) and encapsulation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonsterCard {
    private String id;
    private String type;
    private String name;
    private int level;
    private int treasure;
    private int levelsGained;
    private String description;
    private String nastyEffect;
    private Object levelsLost; 
    private Map<String, Object> tags;
    private String inGame;

    /**
     * Default constructor for Jackson deserialization.
     */
    public MonsterCard() {
    }

    public MonsterCard(String name, int level, String description, String nastyEffect) {
        this.name = name;
        this.level = level;
        this.description = description;
        this.nastyEffect = nastyEffect;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getTreasure() {
        return treasure;
    }

    public int getLevelsGained() {
        return levelsGained;
    }

    public String getDescription() {
        return description;
    }

    public String getNastyEffect() {
        return nastyEffect;
    }

    public Object getLevelsLost() {
        return levelsLost;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public String getInGame() {
        return inGame;
    }

    /**
     * Check if monster has a specific tag.
     * @param tagName Tag to check
     * @return true if monster has the tag
     */
    public boolean hasTag(String tagName) {
        return tags != null && tags.containsKey(tagName);
    }

    /**
     * Get tag value.
     * @param tagName Tag name
     * @return Tag value or null
     */
    public Object getTagValue(String tagName) {
        return tags != null ? tags.get(tagName) : null;
    }

    /**
     * Check if this is an undead monster.
     * @return true if undead
     */
    public boolean isUndead() {
        return hasTag("undead");
    }

    /**
     * Check if monster prevents help from other players.
     * @return true if no help allowed
     */
    public boolean preventsHelp() {
        return hasTag("noHelp");
    }

    /**
     * Check if monster prevents escape.
     * @return true if no escape allowed
     */
    public boolean preventsEscape() {
        return hasTag("noEscape");
    }

    @Override
    public String toString() {
        return String.format("%s (Poziom %d) - %s", name, level, description);
    }
}
