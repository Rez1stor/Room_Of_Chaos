package chaos_room.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a player's class in the game (e.g., Warrior, Mage, Thief, Cleric).
 * Each class has unique abilities that affect combat and gameplay.
 * Demonstrates OOP Inheritance - extends CharacterComponent.
 */
public class CharacterClass extends CharacterComponent {
    
    public CharacterClass() {
        super();
        this.type = "class";
    }
    
    public CharacterClass(String id, String name, String description) {
        super(id, name, "class", description);
    }
    
    /**
     * Loads all classes from the JSON file.
     * @param jsonFilePath path to the JSON file
     * @return list of available classes
     */
    public static List<CharacterClass> loadClassesFromJson(String jsonFilePath) {
        try {
            FileReader reader = new FileReader(CharacterClass.class.getClassLoader().getResource(jsonFilePath).getPath());
            ObjectMapper json = new ObjectMapper();
            List<CharacterClass> allCards = json.readValue(reader, new TypeReference<List<CharacterClass>>() {});
            reader.close();
            
            // Filter only class type cards
            allCards.removeIf(card -> !"class".equals(card.getType()));
            return allCards;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets a class by its id.
     * @param classId the class id
     * @return the class or null if not found
     */
    public static CharacterClass getClassById(String classId) {
        List<CharacterClass> classes = loadClassesFromJson("CharacterCards.json");
        if (classes != null) {
            for (CharacterClass charClass : classes) {
                if (classId.equals(charClass.getId())) {
                    return charClass;
                }
            }
        }
        return null;
    }
    
    /**
     * Checks if this is the Warrior class.
     * @return true if Warrior
     */
    public boolean isWarrior() {
        return "class-warrior".equals(id) || "Wojownik".equalsIgnoreCase(name);
    }
    
    /**
     * Checks if this is the Mage class.
     * @return true if Mage
     */
    public boolean isMage() {
        return "class-mage".equals(id) || "Mag".equalsIgnoreCase(name);
    }
    
    /**
     * Checks if this is the Thief class.
     * @return true if Thief
     */
    public boolean isThief() {
        return "class-thief".equals(id) || "Złodziej".equalsIgnoreCase(name);
    }
    
    /**
     * Checks if this is the Cleric class.
     * @return true if Cleric
     */
    public boolean isCleric() {
        return "class-cleric".equals(id) || "Kleryk".equalsIgnoreCase(name);
    }
    
    /**
     * Gets class bonus against a monster (e.g., Cleric vs undead).
     * @param monster the monster to check
     * @param context the combat context
     * @return bonus amount
     */
    public int getBonusAgainstMonster(MonsterCard monster, CombatContext context) {
        if (monster == null || monster.getTags() == null) {
            return 0;
        }
        
        int bonus = 0;
        
        // Cleric gets bonus against undead
        if (isCleric() && monster.getTags().containsKey("undead")) {
            for (BaseAbility ability : abilities) {
                if (ability.getEffects().containsKey("bonusPerDiscardVsUndead")) {
                    int perDiscard = ((Number) ability.getEffects().get("bonusPerDiscardVsUndead")).intValue();
                    int maxDiscards = 3;
                    if (ability.getConditions().containsKey("maxDiscards")) {
                        maxDiscards = ((Number) ability.getConditions().get("maxDiscards")).intValue();
                    }
                    int discards = Math.min(context.getCardsDiscarded(), maxDiscards);
                    bonus += perDiscard * discards;
                }
            }
        }
        
        return bonus;
    }
    
    /**
     * Gets penalty from monster based on class.
     * @param monster the monster
     * @return penalty amount (to be added to monster's level)
     */
    public int getPenaltyFromMonster(MonsterCard monster) {
        if (monster == null || monster.getTags() == null) {
            return 0;
        }
        
        // Check for vsCleric tag
        if (isCleric() && monster.getTags().containsKey("vsCleric")) {
            return ((Number) monster.getTags().get("vsCleric")).intValue();
        }
        
        return 0;
    }
}
