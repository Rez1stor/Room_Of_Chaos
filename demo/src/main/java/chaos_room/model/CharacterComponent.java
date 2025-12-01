package chaos_room.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static chaos_room.model.GameConstants.*;

/**
 * Abstract base class for character components (Race and Class).
 * Demonstrates OOP Abstraction and Inheritance principles.
 */
public abstract class CharacterComponent {
    protected String id;
    protected String name;
    protected String type;
    protected String description;
    protected List<BaseAbility> abilities;
    
    public CharacterComponent() {
        this.abilities = new ArrayList<>();
    }
    
    public CharacterComponent(String id, String name, String type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.abilities = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<BaseAbility> getAbilities() {
        return abilities;
    }
    
    public void setAbilities(List<BaseAbility> abilities) {
        this.abilities = abilities;
    }
    
    public void addAbility(BaseAbility ability) {
        this.abilities.add(ability);
    }
    
    /**
     * Calculates total combat bonus from all abilities.
     * @param context the combat context
     * @return total combat bonus
     */
    public int getTotalCombatBonus(CombatContext context) {
        int total = 0;
        for (BaseAbility ability : abilities) {
            if (ability.isUsableInCombat()) {
                total += ability.getCombatBonus(context);
            }
        }
        return total;
    }
    
    /**
     * Calculates total escape bonus from all abilities.
     * @param context the combat context
     * @return total escape bonus
     */
    public int getTotalEscapeBonus(CombatContext context) {
        int total = 0;
        for (BaseAbility ability : abilities) {
            total += ability.getEscapeBonus(context);
        }
        return total;
    }
    
    /**
     * Checks if any ability grants victory on tie.
     * @return true if any ability wins on tie
     */
    public boolean hasWinOnTie() {
        for (BaseAbility ability : abilities) {
            if (ability.winsOnTie()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if any ability allows a second escape attempt.
     * @return true if second escape is allowed
     */
    public boolean hasSecondEscapeAttempt() {
        for (BaseAbility ability : abilities) {
            if (ability.allowsSecondEscape()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the hand limit modifier from abilities.
     * @return hand limit (0 means use default)
     */
    public int getHandLimitModifier() {
        for (BaseAbility ability : abilities) {
            Map<String, Object> effects = ability.getEffects();
            if (effects.containsKey(EFFECT_HAND_LIMIT)) {
                return ((Number) effects.get(EFFECT_HAND_LIMIT)).intValue();
            }
        }
        return 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%s): %s\n", name, type, description));
        sb.append("Zdolności:\n");
        for (BaseAbility ability : abilities) {
            sb.append("  - ").append(ability.toString()).append("\n");
        }
        return sb.toString();
    }
}
