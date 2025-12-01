package chaos_room.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of the Ability interface.
 * Demonstrates OOP encapsulation and implementation of interface.
 */
public class BaseAbility implements Ability {
    private String id;
    private String name;
    private String description;
    private Map<String, Object> effects;
    private Map<String, Object> conditions;
    
    public BaseAbility() {
        this.effects = new HashMap<>();
        this.conditions = new HashMap<>();
    }
    
    public BaseAbility(String id, String name, String description, 
                       Map<String, Object> effects, Map<String, Object> conditions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.effects = effects != null ? effects : new HashMap<>();
        this.conditions = conditions != null ? conditions : new HashMap<>();
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public Map<String, Object> getEffects() {
        return effects;
    }
    
    public void setEffects(Map<String, Object> effects) {
        this.effects = effects;
    }
    
    @Override
    public Map<String, Object> getConditions() {
        return conditions;
    }
    
    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }
    
    @Override
    public boolean isUsableInCombat() {
        Object usableIn = conditions.get("usableIn");
        return usableIn == null || "combat".equals(usableIn);
    }
    
    @Override
    public boolean isUsableInEscape() {
        Object usableIn = conditions.get("usableIn");
        return "escape".equals(usableIn);
    }
    
    @Override
    public int getCombatBonus(CombatContext context) {
        int bonus = 0;
        
        // Combat bonus per discard (e.g., Warrior's Bujstwo)
        if (effects.containsKey("combatBonusPerDiscard")) {
            int bonusPerDiscard = ((Number) effects.get("combatBonusPerDiscard")).intValue();
            int maxDiscards = getMaxDiscards();
            int discards = Math.min(context.getCardsDiscarded(), maxDiscards);
            bonus += bonusPerDiscard * discards;
        }
        
        // Bonus vs undead (e.g., Cleric's Banish)
        if (effects.containsKey("bonusPerDiscardVsUndead") && context.monsterHasTag("undead")) {
            int bonusPerDiscard = ((Number) effects.get("bonusPerDiscardVsUndead")).intValue();
            int maxDiscards = getMaxDiscards();
            int discards = Math.min(context.getCardsDiscarded(), maxDiscards);
            bonus += bonusPerDiscard * discards;
        }
        
        return bonus;
    }
    
    @Override
    public int getEscapeBonus(CombatContext context) {
        int bonus = 0;
        
        // Escape bonus (e.g., Elf's +1)
        if (effects.containsKey("escapeBonus")) {
            bonus += ((Number) effects.get("escapeBonus")).intValue();
        }
        
        // Escape bonus per discard (e.g., Mage's Flight)
        if (effects.containsKey("escapeBonusPerDiscard")) {
            int bonusPerDiscard = ((Number) effects.get("escapeBonusPerDiscard")).intValue();
            int maxDiscards = getMaxDiscards();
            int discards = Math.min(context.getCardsDiscarded(), maxDiscards);
            bonus += bonusPerDiscard * discards;
        }
        
        return bonus;
    }
    
    /**
     * Gets the maximum number of discards allowed for this ability.
     * @return max discards, defaults to 0 if not specified
     */
    protected int getMaxDiscards() {
        if (conditions.containsKey("maxDiscards")) {
            return ((Number) conditions.get("maxDiscards")).intValue();
        }
        return 0;
    }
    
    /**
     * Checks if this ability grants victory on tie.
     * @return true if ability wins on tie
     */
    public boolean winsOnTie() {
        return effects.containsKey("winOnTie") && (Boolean) effects.get("winOnTie");
    }
    
    /**
     * Checks if this ability allows a second escape attempt.
     * @return true if second escape is allowed
     */
    public boolean allowsSecondEscape() {
        return effects.containsKey("secondEscapeAttempt") && (Boolean) effects.get("secondEscapeAttempt");
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s", name, description);
    }
}
