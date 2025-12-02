package chaos_room.model;

/**
 * Enum representing different races in the game.
 * Each race has unique abilities and bonuses that affect gameplay.
 * Demonstrates OOP principles: encapsulation and abstraction.
 */
public enum Race {
    DWARF("Krasnolud", "Silny, uparty i bardzo pojemny", 6, true),
    HALFLING("Niziołek", "Mały, ale bardzo sprytny", 5, false),
    ELF("Elf", "Zwinny i szlachetny", 5, false),
    HUMAN("Człowiek", "Zwykły śmiertelnik bez specjalnych zdolności rasowych", 5, false);

    private final String name;
    private final String description;
    private final int handLimit;
    private final boolean unlimitedBigItems;

    Race(String name, String description, int handLimit, boolean unlimitedBigItems) {
        this.name = name;
        this.description = description;
        this.handLimit = handLimit;
        this.unlimitedBigItems = unlimitedBigItems;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getHandLimit() {
        return handLimit;
    }

    public boolean hasUnlimitedBigItems() {
        return unlimitedBigItems;
    }

    /**
     * Get escape bonus based on race.
     * @return Escape bonus value
     */
    public int getEscapeBonus() {
        switch (this) {
            case ELF:
                return 1; // +1 do ucieczki
            default:
                return 0;
        }
    }

    /**
     * Check if the race can get level for helping in combat.
     * @return true if race earns level for helping
     */
    public boolean getLevelForHelping() {
        return this == ELF;
    }

    /**
     * Check if race can double sell items.
     * @return true if race can double sell (once per turn)
     */
    public boolean canDoubleSell() {
        return this == HALFLING;
    }

    /**
     * Check if race can get a second escape attempt.
     * @return true if race can try escaping again
     */
    public boolean canRetryEscape() {
        return this == HALFLING;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
