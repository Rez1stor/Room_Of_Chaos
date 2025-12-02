package chaos_room.model;

/**
 * Abstract base class for all game entities.
 * Demonstrates OOP principle: abstraction.
 * Provides common functionality for players and monsters.
 */
public abstract class GameEntity {
    protected String name;
    protected int level;
    
    /**
     * Constructor for game entities.
     * @param name Entity name
     * @param level Entity level
     */
    public GameEntity(String name, int level) {
        this.name = name;
        this.level = level;
    }
    
    /**
     * Get the entity's combat strength.
     * Must be implemented by subclasses.
     * @return Combat strength value
     */
    public abstract int getCombatStrength();
    
    /**
     * Get the entity's name.
     * @return Entity name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the entity's level.
     * @return Entity level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Set the entity's level.
     * @param level New level
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Display entity information.
     * @return String representation
     */
    @Override
    public abstract String toString();
}
