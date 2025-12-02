package chaos_room.model;

/**
 * Enum representing different character classes in the game.
 * Each class has unique abilities and bonuses that affect gameplay.
 * Demonstrates OOP principles: encapsulation and abstraction.
 */
public enum CharacterClass {
    WARRIOR("Wojownik", "Silny, brutalny i zawsze gotów do walki", 1, 0, true),
    MAGE("Mag", "Mistrz zaklęć i magicznych sztuczek", 0, 1, false),
    THIEF("Złodziej", "Sprytny i podstępny", 0, 0, false),
    CLERIC("Kleryk", "Kapłan światła i obrońca dusz", 0, 0, false),
    NONE("Brak klasy", "Zwykły śmiertelnik bez specjalnych umiejętności", 0, 0, false);

    private final String name;
    private final String description;
    private final int combatBonus;
    private final int escapeBonus;
    private final boolean winOnTie;

    CharacterClass(String name, String description, int combatBonus, int escapeBonus, boolean winOnTie) {
        this.name = name;
        this.description = description;
        this.combatBonus = combatBonus;
        this.escapeBonus = escapeBonus;
        this.winOnTie = winOnTie;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCombatBonus() {
        return combatBonus;
    }

    public int getEscapeBonus() {
        return escapeBonus;
    }

    public boolean canWinOnTie() {
        return winOnTie;
    }

    /**
     * Apply class-specific ability during combat.
     * @param player The player using the ability
     * @param discardedCards Number of cards discarded to activate ability
     * @return Combat bonus from ability
     */
    public int applyAbility(Player player, int discardedCards) {
        switch (this) {
            case WARRIOR:
                // Bujstwo: +1 per discarded card (max 3)
                return Math.min(discardedCards, 3);
            case MAGE:
                // Mage abilities work differently
                return 0;
            case THIEF:
                // Thief abilities target other players
                return 0;
            case CLERIC:
                // +3 per discard vs undead (handled in combat)
                return 0;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
