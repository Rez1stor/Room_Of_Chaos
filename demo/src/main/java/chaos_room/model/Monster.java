package chaos_room.model;

/**
 * Represents a monster in combat.
 * Demonstrates OOP principles: implementation of Combatant interface.
 */
public class Monster implements Combatant {
    private MonsterCard card;
    
    public Monster(MonsterCard card) {
        this.card = card;
    }
    
    @Override
    public int getCombatStrength() {
        return card.getLevel();
    }
    
    @Override
    public int getLevel() {
        return card.getLevel();
    }
    
    @Override
    public boolean canEscape() {
        return !card.preventsEscape();
    }
    
    @Override
    public int getEscapeBonus() {
        // Monsters don't escape, but some affect player escape
        Object escapeTag = card.getTagValue("escapeBonus");
        if (escapeTag != null) {
            return ((Number) escapeTag).intValue();
        }
        return 0;
    }
    
    /**
     * Get the underlying monster card.
     * @return MonsterCard instance
     */
    public MonsterCard getCard() {
        return card;
    }
    
    /**
     * Get monster name.
     * @return Monster name
     */
    public String getName() {
        return card.getName();
    }
    
    /**
     * Check if this monster is undead.
     * @return true if undead
     */
    public boolean isUndead() {
        return card.isUndead();
    }
    
    /**
     * Check if help is blocked.
     * @return true if no help allowed
     */
    public boolean preventsHelp() {
        return card.preventsHelp();
    }
    
    @Override
    public String toString() {
        return card.toString();
    }
}
