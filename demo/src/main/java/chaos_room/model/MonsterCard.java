package chaos_room.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a Monster card in the game.
 * Monsters have levels, tags that affect combat, and nasty effects on player loss.
 * Demonstrates OOP Encapsulation with proper getters/setters.
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

    public MonsterCard() {}

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

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }

    public void setLevelsGained(int levelsGained) {
        this.levelsGained = levelsGained;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNastyEffect(String nastyEffect) {
        this.nastyEffect = nastyEffect;
    }

    public void setLevelsLost(Object levelsLost) {
        this.levelsLost = levelsLost;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public void setInGame(String inGame) {
        this.inGame = inGame;
    }

    // --- Tag-based combat methods ---

    /**
     * Checks if this monster is undead.
     * @return true if monster has the undead tag
     */
    public boolean isUndead() {
        return tags != null && tags.containsKey("undead") && Boolean.TRUE.equals(tags.get("undead"));
    }

    /**
     * Checks if this monster prevents escape.
     * @return true if no escape allowed
     */
    public boolean preventsEscape() {
        return tags != null && tags.containsKey("noEscape") && Boolean.TRUE.equals(tags.get("noEscape"));
    }

    /**
     * Checks if this monster prevents help from other players.
     * @return true if help is not allowed
     */
    public boolean preventsHelp() {
        return tags != null && tags.containsKey("noHelp") && Boolean.TRUE.equals(tags.get("noHelp"));
    }

    /**
     * Checks if this monster is magic resistant (only level counts).
     * @return true if magic resistant
     */
    public boolean isMagicResistant() {
        return tags != null && tags.containsKey("magicResist") && Boolean.TRUE.equals(tags.get("magicResist"));
    }

    /**
     * Gets the escape modifier from this monster's tags.
     * @return escape bonus/penalty (positive = easier, negative = harder)
     */
    public int getEscapeModifier() {
        if (tags != null && tags.containsKey("escapeBonus")) {
            return ((Number) tags.get("escapeBonus")).intValue();
        }
        return 0;
    }

    /**
     * Gets bonus for monster against a specific race.
     * @param raceName the race name
     * @return bonus for monster (penalty for player)
     */
    public int getBonusVsRace(String raceName) {
        if (tags == null || raceName == null) {
            return 0;
        }
        
        String tagKey = "vs" + capitalizeFirst(raceName);
        if (tags.containsKey(tagKey)) {
            return ((Number) tags.get(tagKey)).intValue();
        }
        
        // Also check for alternate spellings
        for (String key : tags.keySet()) {
            if (key.toLowerCase().startsWith("vs") && 
                key.toLowerCase().contains(raceName.toLowerCase())) {
                return ((Number) tags.get(key)).intValue();
            }
        }
        
        return 0;
    }

    /**
     * Gets bonus for monster against a specific class.
     * @param className the class name
     * @return bonus for monster (penalty for player)
     */
    public int getBonusVsClass(String className) {
        if (tags == null || className == null) {
            return 0;
        }
        
        String tagKey = "vs" + capitalizeFirst(className);
        if (tags.containsKey(tagKey)) {
            return ((Number) tags.get(tagKey)).intValue();
        }
        
        return 0;
    }

    /**
     * Calculates the levels lost when losing to this monster.
     * @param lowestPlayerLevel the lowest player level in the game (for dynamic calculation)
     * @param currentPlayerLevel the current player's level
     * @return number of levels lost
     */
    public int calculateLevelsLost(int lowestPlayerLevel, int currentPlayerLevel) {
        if (levelsLost == null) {
            return 0;
        }
        
        if (levelsLost instanceof Number) {
            return ((Number) levelsLost).intValue();
        }
        
        if ("dynamic".equals(levelsLost)) {
            // For monsters like Mademoazelle - drop to lowest player level
            if (isMagicResistant()) {
                return currentPlayerLevel - lowestPlayerLevel;
            }
            // For monsters like Ork 3872 - roll dice (returns max possible)
            return 6;
        }
        
        return 0;
    }

    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Loads all monsters from the JSON file.
     * @param jsonFilePath path to the JSON file
     * @return list of monster cards
     */
    public static List<MonsterCard> loadMonstersFromJson(String jsonFilePath) {
        try {
            FileReader reader = new FileReader(MonsterCard.class.getClassLoader().getResource(jsonFilePath).getPath());
            ObjectMapper json = new ObjectMapper();
            List<MonsterCard> allCards = json.readValue(reader, new TypeReference<List<MonsterCard>>() {});
            reader.close();
            
            // Filter only monster type cards
            List<MonsterCard> monsters = new ArrayList<>();
            for (MonsterCard card : allCards) {
                if ("monster".equals(card.getType())) {
                    monsters.add(card);
                }
            }
            return monsters;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Gets a random monster from the door cards.
     * @return a random monster card
     */
    public static MonsterCard getRandomMonster() {
        List<MonsterCard> monsters = loadMonstersFromJson("DoorCards.json");
        if (monsters == null || monsters.isEmpty()) {
            return null;
        }
        int idx = (int) (Math.random() * monsters.size());
        return monsters.get(idx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("【%s】 (Poziom %d)\n", name, level));
        sb.append(String.format("  %s\n", description));
        sb.append(String.format("  Zły efekt: %s\n", nastyEffect));
        sb.append(String.format("  Skarby: %d, Poziomy za zwycięstwo: %d", treasure, levelsGained));
        return sb.toString();
    }
}
