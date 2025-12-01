package chaos_room.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles combat between players and monsters.
 * Implements Munchkin-like battle mechanics with class/race abilities and monster tags.
 * Demonstrates OOP Encapsulation and Single Responsibility Principle.
 */
public class Combat {
    private static final Random random = new Random();
    private static final int ESCAPE_SUCCESS_THRESHOLD = 5; // Roll 5+ to escape
    
    private Player player;
    private MonsterCard monster;
    private Player helper;
    private CombatContext context;
    private List<Card> cardsUsedInCombat;
    private CombatResult result;
    
    /**
     * Combat result enumeration.
     */
    public enum CombatResult {
        VICTORY,
        DEFEAT,
        ESCAPED,
        FAILED_ESCAPE,
        IN_PROGRESS
    }
    
    public Combat(Player player, MonsterCard monster) {
        this.player = player;
        this.monster = monster;
        this.context = new CombatContext(player, monster);
        this.cardsUsedInCombat = new ArrayList<>();
        this.result = CombatResult.IN_PROGRESS;
    }
    
    /**
     * Gets the player's total combat strength.
     * @return combat strength including level, equipment, and bonuses
     */
    public int getPlayerCombatStrength() {
        int strength = player.getLevel();
        
        // Add bonuses from race abilities
        if (player.getRace() != null) {
            strength += player.getRace().getTotalCombatBonus(context);
        }
        
        // Add bonuses from class abilities
        if (player.getCharacterClass() != null) {
            strength += player.getCharacterClass().getTotalCombatBonus(context);
            strength += player.getCharacterClass().getBonusAgainstMonster(monster, context);
        }
        
        // Add helper's strength if present
        if (helper != null) {
            strength += helper.getLevel();
            if (helper.getRace() != null) {
                strength += helper.getRace().getTotalCombatBonus(context);
            }
            if (helper.getCharacterClass() != null) {
                strength += helper.getCharacterClass().getTotalCombatBonus(context);
            }
        }
        
        return strength;
    }
    
    /**
     * Gets the monster's effective combat level including bonuses.
     * @return monster's combat level
     */
    public int getMonsterCombatLevel() {
        int level = monster.getLevel();
        
        // Monster gets bonuses against certain races
        if (player.getRace() != null) {
            level += monster.getBonusVsRace(player.getRace().getName());
        }
        
        // Monster gets bonuses against certain classes
        if (player.getCharacterClass() != null) {
            level += monster.getBonusVsClass(player.getCharacterClass().getName());
            level += player.getCharacterClass().getPenaltyFromMonster(monster);
        }
        
        return level;
    }
    
    /**
     * Checks if the player wins the combat.
     * @return true if player wins
     */
    public boolean playerWins() {
        int playerStrength = getPlayerCombatStrength();
        int monsterLevel = getMonsterCombatLevel();
        
        // Magic resistant monsters ignore all bonuses - only level counts
        if (monster.isMagicResistant()) {
            playerStrength = player.getLevel();
            if (helper != null) {
                playerStrength += helper.getLevel();
            }
        }
        
        // Check for tie
        if (playerStrength == monsterLevel) {
            // Warrior wins on tie
            if (player.getCharacterClass() != null && player.getCharacterClass().hasWinOnTie()) {
                return true;
            }
            return false;
        }
        
        return playerStrength > monsterLevel;
    }
    
    /**
     * Sets the helper for this combat.
     * @param helper the helping player
     * @return true if help is allowed, false if monster prevents it
     */
    public boolean setHelper(Player helper) {
        if (monster.preventsHelp()) {
            return false;
        }
        this.helper = helper;
        return true;
    }
    
    /**
     * Uses a card to boost combat strength.
     * @param card the card to use
     */
    public void useCard(Card card) {
        cardsUsedInCombat.add(card);
        context.setCardsDiscarded(cardsUsedInCombat.size());
    }
    
    /**
     * Resolves the combat and determines the outcome.
     * @return the combat result
     */
    public CombatResult resolveCombat() {
        if (playerWins()) {
            result = CombatResult.VICTORY;
            applyVictory();
        } else {
            result = CombatResult.DEFEAT;
        }
        return result;
    }
    
    /**
     * Attempts to escape from combat.
     * @return true if escape successful
     */
    public boolean attemptEscape() {
        if (monster.preventsEscape()) {
            result = CombatResult.FAILED_ESCAPE;
            return false;
        }
        
        int roll = rollDice();
        int escapeBonus = getEscapeBonus();
        int totalRoll = roll + escapeBonus;
        
        if (totalRoll >= ESCAPE_SUCCESS_THRESHOLD) {
            result = CombatResult.ESCAPED;
            return true;
        }
        
        // Check for second escape attempt (Halfling ability)
        context.setFirstEscapeFailed(true);
        if (player.getRace() != null && player.getRace().hasSecondEscapeAttempt()) {
            // Player can try again after discarding a card
            return false;
        }
        
        result = CombatResult.FAILED_ESCAPE;
        return false;
    }
    
    /**
     * Second escape attempt for Halflings.
     * @return true if escape successful
     */
    public boolean attemptSecondEscape() {
        if (!context.isFirstEscapeFailed()) {
            return false;
        }
        
        int roll = rollDice();
        int escapeBonus = getEscapeBonus();
        int totalRoll = roll + escapeBonus;
        
        if (totalRoll >= ESCAPE_SUCCESS_THRESHOLD) {
            result = CombatResult.ESCAPED;
            return true;
        }
        
        result = CombatResult.FAILED_ESCAPE;
        return false;
    }
    
    /**
     * Gets total escape bonus from abilities and monster modifiers.
     * @return escape bonus
     */
    private int getEscapeBonus() {
        int bonus = 0;
        context.setEscapePhase(true);
        
        // Race escape bonus (e.g., Elf +1)
        if (player.getRace() != null) {
            bonus += player.getRace().getTotalEscapeBonus(context);
        }
        
        // Class escape bonus (e.g., Mage's Flight)
        if (player.getCharacterClass() != null) {
            bonus += player.getCharacterClass().getTotalEscapeBonus(context);
        }
        
        // Monster escape modifier (can be positive or negative)
        bonus += monster.getEscapeModifier();
        
        return bonus;
    }
    
    /**
     * Rolls a 6-sided dice.
     * @return result 1-6
     */
    public int rollDice() {
        return random.nextInt(6) + 1;
    }
    
    /**
     * Applies victory rewards to the player.
     */
    private void applyVictory() {
        // Add levels gained
        int levelsGained = monster.getLevelsGained();
        if (levelsGained > 0) {
            player.addLevel(levelsGained);
        }
        
        // Elf helper gets a level too
        if (helper != null && helper.getRace() != null) {
            for (BaseAbility ability : helper.getRace().getAbilities()) {
                if (ability.getEffects().containsKey("levelForHelping")) {
                    helper.addLevel(1);
                    break;
                }
            }
        }
    }
    
    /**
     * Applies defeat penalties to the player (nasty effect).
     * @param lowestPlayerLevel the lowest level among all players
     */
    public void applyDefeat(int lowestPlayerLevel) {
        int levelsLost = monster.calculateLevelsLost(lowestPlayerLevel, player.getLevel());
        if (levelsLost > 0) {
            player.loseLevel(levelsLost);
        }
    }
    
    /**
     * Gets a description of the current combat state.
     * @return combat state description
     */
    public String getCombatDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== WALKA ===\n");
        sb.append(String.format("Gracz: %s (Poziom %d", player.getName(), player.getLevel()));
        if (player.getRace() != null) {
            sb.append(String.format(", %s", player.getRace().getName()));
        }
        if (player.getCharacterClass() != null) {
            sb.append(String.format(", %s", player.getCharacterClass().getName()));
        }
        sb.append(")\n");
        
        if (helper != null) {
            sb.append(String.format("Pomocnik: %s (Poziom %d)\n", helper.getName(), helper.getLevel()));
        }
        
        sb.append(String.format("\nSiła gracza: %d\n", getPlayerCombatStrength()));
        sb.append(String.format("\nPotwór: %s (Poziom %d", monster.getName(), monster.getLevel()));
        
        int effectiveLevel = getMonsterCombatLevel();
        if (effectiveLevel != monster.getLevel()) {
            sb.append(String.format(" -> %d z bonusami", effectiveLevel));
        }
        sb.append(")\n");
        sb.append(String.format("  %s\n", monster.getDescription()));
        
        if (monster.preventsEscape()) {
            sb.append("  ⚠ Nie można uciec!\n");
        }
        if (monster.preventsHelp()) {
            sb.append("  ⚠ Nikt nie może pomóc!\n");
        }
        if (monster.isUndead()) {
            sb.append("  ☠ Nieumarły\n");
        }
        
        return sb.toString();
    }
    
    // Getters
    public Player getPlayer() {
        return player;
    }
    
    public MonsterCard getMonster() {
        return monster;
    }
    
    public Player getHelper() {
        return helper;
    }
    
    public CombatResult getResult() {
        return result;
    }
    
    public CombatContext getContext() {
        return context;
    }
    
    public List<Card> getCardsUsedInCombat() {
        return cardsUsedInCombat;
    }
}
