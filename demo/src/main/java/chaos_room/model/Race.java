package chaos_room.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a player's race in the game (e.g., Dwarf, Elf, Halfling).
 * Each race has unique abilities that affect gameplay.
 * Demonstrates OOP Inheritance - extends CharacterComponent.
 */
public class Race extends CharacterComponent {
    
    public Race() {
        super();
        this.type = "race";
    }
    
    public Race(String id, String name, String description) {
        super(id, name, "race", description);
    }
    
    /**
     * Loads all races from the JSON file.
     * @param jsonFilePath path to the JSON file
     * @return list of available races
     */
    public static List<Race> loadRacesFromJson(String jsonFilePath) {
        try {
            FileReader reader = new FileReader(Race.class.getClassLoader().getResource(jsonFilePath).getPath());
            ObjectMapper json = new ObjectMapper();
            List<Race> allCards = json.readValue(reader, new TypeReference<List<Race>>() {});
            reader.close();
            
            // Filter only race type cards
            allCards.removeIf(card -> !"race".equals(card.getType()));
            return allCards;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets a race by its id.
     * @param raceId the race id
     * @return the race or null if not found
     */
    public static Race getRaceById(String raceId) {
        List<Race> races = loadRacesFromJson("CharacterCards.json");
        if (races != null) {
            for (Race race : races) {
                if (raceId.equals(race.getId())) {
                    return race;
                }
            }
        }
        return null;
    }
    
    /**
     * Checks if this race gets bonus against a specific monster.
     * @param monster the monster to check
     * @return bonus modifier against this monster
     */
    public int getBonusAgainstMonster(MonsterCard monster) {
        if (monster == null || monster.getTags() == null) {
            return 0;
        }
        
        // Check if monster has a penalty against this race
        String raceTagKey = "vs" + capitalizeFirst(this.name);
        if (monster.getTags().containsKey(raceTagKey)) {
            // This is actually a bonus for the monster, so we return negative
            return 0;
        }
        
        return 0;
    }
    
    /**
     * Checks if this race has a penalty against a specific monster.
     * @param monster the monster to check
     * @return penalty amount (negative number)
     */
    public int getPenaltyFromMonster(MonsterCard monster) {
        if (monster == null || monster.getTags() == null) {
            return 0;
        }
        
        // Check for vsDwarf, vsElf, etc.
        String lowerName = this.name.toLowerCase();
        for (String key : monster.getTags().keySet()) {
            if (key.toLowerCase().startsWith("vs") && key.toLowerCase().contains(lowerName)) {
                Object value = monster.getTags().get(key);
                if (value instanceof Number) {
                    // Monster gets this bonus, so it's a penalty for the player
                    return ((Number) value).intValue();
                }
            }
        }
        
        return 0;
    }
    
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

