package chaos_room.model;

/**
 * Interface for entities that can participate in combat.
 * Demonstrates OOP principle: interface-based polymorphism.
 */
public interface Combatant {
    
    /**
     * Get the combat strength of this entity.
     * @return Combat strength value
     */
    int getCombatStrength();
    
    /**
     * Get the level of this entity.
     * @return Level value
     */
    int getLevel();
    
    /**
     * Check if this entity can escape from combat.
     * @return true if escape is possible
     */
    boolean canEscape();
    
    /**
     * Get bonus for escape attempts.
     * @return Escape bonus value
     */
    int getEscapeBonus();
}
