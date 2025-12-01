package chaos_room.model;

/**
 * Context object for combat that provides information needed for ability calculations.
 * Encapsulates all combat-related data needed by abilities.
 */
public class CombatContext {
    private Player player;
    private MonsterCard monster;
    private int cardsDiscarded;
    private boolean isEscapePhase;
    private boolean firstEscapeFailed;
    
    public CombatContext(Player player, MonsterCard monster) {
        this.player = player;
        this.monster = monster;
        this.cardsDiscarded = 0;
        this.isEscapePhase = false;
        this.firstEscapeFailed = false;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public MonsterCard getMonster() {
        return monster;
    }
    
    public int getCardsDiscarded() {
        return cardsDiscarded;
    }
    
    public void setCardsDiscarded(int cardsDiscarded) {
        this.cardsDiscarded = cardsDiscarded;
    }
    
    public boolean isEscapePhase() {
        return isEscapePhase;
    }
    
    public void setEscapePhase(boolean escapePhase) {
        isEscapePhase = escapePhase;
    }
    
    public boolean isFirstEscapeFailed() {
        return firstEscapeFailed;
    }
    
    public void setFirstEscapeFailed(boolean firstEscapeFailed) {
        this.firstEscapeFailed = firstEscapeFailed;
    }
    
    /**
     * Checks if the monster has a specific tag.
     * @param tagName the tag to check
     * @return true if monster has the tag
     */
    public boolean monsterHasTag(String tagName) {
        if (monster == null || monster.getTags() == null) {
            return false;
        }
        return monster.getTags().containsKey(tagName);
    }
    
    /**
     * Gets the value of a monster tag.
     * @param tagName the tag name
     * @return the tag value, or null if not present
     */
    public Object getMonsterTagValue(String tagName) {
        if (monster == null || monster.getTags() == null) {
            return null;
        }
        return monster.getTags().get(tagName);
    }
}
