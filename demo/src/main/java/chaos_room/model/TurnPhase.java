package chaos_room.model;

/**
 * Enum representing the different phases of a player's turn.
 * Demonstrates OOP principle: state pattern using enums.
 */
public enum TurnPhase {
    KICK_DOOR("Wyważ drzwi", "Ciągnij kartę z talii drzwi"),
    LOOK_FOR_TROUBLE("Szukaj kłopotów", "Walcz z potworem lub ciągnij kolejną kartę"),
    LOOT_ROOM("Grabież pokoju", "Ciągnij kartę zakrytą jeśli nie walczyłeś"),
    CHARITY("Dobroczynność", "Oddaj nadmiarowe karty innemu graczowi");

    private final String name;
    private final String description;

    TurnPhase(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the next phase in the turn sequence.
     * @return Next turn phase
     */
    public TurnPhase nextPhase() {
        TurnPhase[] phases = values();
        int nextIndex = (this.ordinal() + 1) % phases.length;
        if (nextIndex == 0) {
            return null; // Turn is complete
        }
        return phases[nextIndex];
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}
