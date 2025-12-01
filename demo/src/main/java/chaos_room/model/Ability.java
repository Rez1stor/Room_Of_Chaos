package chaos_room.model;

import java.util.Map;

/**
 * Interface representing a character ability (from race or class).
 * Demonstrates OOP Interface principle for common ability behavior.
 */
public interface Ability {
    
    /**
     * Gets the unique identifier of this ability.
     * @return ability id
     */
    String getId();
    
    /**
     * Gets the display name of this ability.
     * @return ability name
     */
    String getName();
    
    /**
     * Gets the description explaining what this ability does.
     * @return ability description
     */
    String getDescription();
    
    /**
     * Gets the effects this ability provides.
     * @return map of effect names to their values
     */
    Map<String, Object> getEffects();
    
    /**
     * Gets the conditions required to use this ability.
     * @return map of condition names to their values
     */
    Map<String, Object> getConditions();
    
    /**
     * Checks if this ability can be used in combat.
     * @return true if usable in combat
     */
    boolean isUsableInCombat();
    
    /**
     * Checks if this ability can be used during escape.
     * @return true if usable during escape
     */
    boolean isUsableInEscape();
    
    /**
     * Calculates the combat bonus this ability provides.
     * @param context the combat context for calculating bonuses
     * @return combat bonus value
     */
    int getCombatBonus(CombatContext context);
    
    /**
     * Calculates the escape bonus this ability provides.
     * @param context the combat context for calculating bonuses
     * @return escape bonus value
     */
    int getEscapeBonus(CombatContext context);
}
