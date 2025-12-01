package chaos_room.model;

import java.util.HashMap;
import java.util.Map;

import static chaos_room.model.GameConstants.*;

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
        Object usableIn = conditions.get(CONDITION_USABLE_IN);
        return usableIn == null || PHASE_COMBAT.equals(usableIn);
    }
    
    @Override
    public boolean isUsableInEscape() {
        Object usableIn = conditions.get(CONDITION_USABLE_IN);
        return PHASE_ESCAPE.equals(usableIn);
    }
    
    @Override
    public int getCombatBonus(CombatContext context) {
        int bonus = 0;
        
        // Combat bonus per discard (e.g., Warrior's Bujstwo)
        if (effects.containsKey(EFFECT_COMBAT_BONUS_PER_DISCARD)) {
            int bonusPerDiscard = ((Number) effects.get(EFFECT_COMBAT_BONUS_PER_DISCARD)).intValue();
            int maxDiscards = getMaxDiscards();
            int discards = Math.min(context.getCardsDiscarded(), maxDiscards);
            bonus += bonusPerDiscard * discards;
        }
        
        // Bonus vs undead (e.g., Cleric's Banish)
        if (effects.containsKey(EFFECT_BONUS_VS_UNDEAD) && context.monsterHasTag(TAG_UNDEAD)) {
            int bonusPerDiscard = ((Number) effects.get(EFFECT_BONUS_VS_UNDEAD)).intValue();
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
        if (effects.containsKey(EFFECT_ESCAPE_BONUS)) {
            bonus += ((Number) effects.get(EFFECT_ESCAPE_BONUS)).intValue();
        }
        
        // Escape bonus per discard (e.g., Mage's Flight)
        if (effects.containsKey(EFFECT_ESCAPE_BONUS_PER_DISCARD)) {
            int bonusPerDiscard = ((Number) effects.get(EFFECT_ESCAPE_BONUS_PER_DISCARD)).intValue();
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
        if (conditions.containsKey(CONDITION_MAX_DISCARDS)) {
            return ((Number) conditions.get(CONDITION_MAX_DISCARDS)).intValue();
        }
        return 0;
    }
    
    /**
     * Checks if this ability grants victory on tie.
     * @return true if ability wins on tie
     */
    public boolean winsOnTie() {
        return effects.containsKey(EFFECT_WIN_ON_TIE) && (Boolean) effects.get(EFFECT_WIN_ON_TIE);
    }
    
    /**
     * Checks if this ability allows a second escape attempt.
     * @return true if second escape is allowed
     */
    public boolean allowsSecondEscape() {
        return effects.containsKey(EFFECT_SECOND_ESCAPE_ATTEMPT) && (Boolean) effects.get(EFFECT_SECOND_ESCAPE_ATTEMPT);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s", name, description);
    }
}
