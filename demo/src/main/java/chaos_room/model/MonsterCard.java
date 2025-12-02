package chaos_room.model;
import java.util.Map;

public class MonsterCard extends Card implements Combatant { // OOP Inheritance
    private int level;
    private int treasure;
    private int levelsGained;
    private String nastyEffect;
    private Object levelsLost; 
    private Map<String, Object> tags;
    private String inGame;
    private int strength;

    public MonsterCard() {
        super();
    }

    public MonsterCard(String id, String name, CardType type, String description, int level, String nastyEffect,
                       int treasure, int levelsGained, Object levelsLost, Map<String, Object> tags,
                       String inGame, int strength) {
        super(id, name, type, description);
        this.level = level;
        this.nastyEffect = nastyEffect;
        this.treasure = treasure;
        this.levelsGained = levelsGained;
        this.levelsLost = levelsLost;
        this.tags = tags;
        this.inGame = inGame;
        this.strength = this.level; // Default strength is equal to level
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNastyEffect() {
        return nastyEffect;
    }

    public void setNastyEffect(String nastyEffect) {
        this.nastyEffect = nastyEffect;
    }

    public int getTreasure() {
        return treasure;
    }

    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }

    public int getLevelsGained() {
        return levelsGained;
    }

    public void setLevelsGained(int levelsGained) {
        this.levelsGained = levelsGained;
    }

    public Object getLevelsLost() {
        return levelsLost;
    }

    public void setLevelsLost(Object levelsLost) {
        this.levelsLost = levelsLost;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public String getInGame() {
        return inGame;
    }

    public void setInGame(String inGame) {
        this.inGame = inGame;
    }

    public int getStrength() {
        if (strength == 0) {
            return level;
        }
        return strength;
    }

    public int setStrength(int strength) {
        return this.strength = strength;
    }

    @Override
    public boolean use(Player player, chaos_room.view.GameView view) {
        view.displayMessage("You start a fight with a monster!");
        player.getInventory().remove(this);
        // The controller needs to know to start a fight.
        // Returning true signals "something happened that might end turn or change state"
        return true; 
    }
}
