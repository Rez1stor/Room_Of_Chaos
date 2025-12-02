package chaos_room.model;
import chaos_room.view.Menu;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;

/**
 * Represents a player in the game.
 * Demonstrates OOP principles: encapsulation (private fields with getters/setters),
 * composition (has-a relationships with cards, class, race),
 * and interface implementation (Combatant).
 */
public class Player implements Combatant {
    private String name;
    private int level;
    private int strength;
    private List<Card> inventory;
    private List<Card> equippedItems;
    private String gender;
    private CharacterClass characterClass;
    private Race race;
    private boolean usedDoubleSell;
    private boolean usedSecondEscape;
    private static Random random = new Random();

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.level = 1;
        this.strength = 0;
        this.characterClass = CharacterClass.NONE;
        this.race = Race.HUMAN;
        this.inventory = new ArrayList<>();
        this.equippedItems = new ArrayList<>();
        this.usedDoubleSell = false;
        this.usedSecondEscape = false;
        
        StarterPack();
    }

    /**
     * Constructor with class and race selection.
     */
    public Player(String name, String gender, CharacterClass characterClass, Race race) {
        this(name, gender);
        this.characterClass = characterClass;
        this.race = race;
    }

    /**
     * Draw a card from the door deck.
     */
    public void drawCard() {
        if (inventory.size() >= getMaxCardsInHand()) {
            System.out.println("Nie możesz wziąć więcej kart, Twój ekwipunek jest pełny.");
            return;
        }
        inventory.add(Card.getRandomCardFromJson("DoorCards.json"));
    }

    /**
     * Discard a card from inventory (put face down).
     */
    public Card discardCard(int index) {
        if (index >= 0 && index < inventory.size()) {
            return inventory.remove(index);
        }
        return null;
    }

    private void StarterPack() {
        for (int i = 0; i < 4; i++) drawCard();
        showInventory();
    }

    public void showInventory() {
        System.out.println("Ekwipunek gracza " + name + ":");
        for (Card card : inventory) {
            System.out.printf("- %s (Typ: %s, Opis: %s)%n", card.getName(), card.getType(), card.getDescription());
        }
    }

    public void selectCard() {
        int selected = Menu.displayMenu(inventory.toArray());
        Card chosenCard = inventory.get(selected);
        System.out.printf("Wybrałeś kartę: %s%n \n%s", chosenCard.getName(), chosenCard.getDescription());
        int options = Menu.displayMenu(new String[]{"Użyj karty", "Odrzuć kartę", "Anuluj"});
        switch (options) {
            case 0:
                useCard(chosenCard);
                break;
            case 1:
                System.out.println("Odrzucasz kartę: " + chosenCard.getName());
                inventory.remove(chosenCard);
                break;
            case 2:
                System.out.println("Anulowano wybór karty.");
                break;
            default:
                System.out.println("Nieprawidłowa opcja.");
                break;
        }
    }

    public void useCard(Card card) {
        System.out.println("Używasz karty: " + card.getName());
        inventory.remove(card);
        equippedItems.add(card);
    }

    /**
     * Calculate total combat strength.
     * @return Combined level + equipment bonuses + class bonuses
     */
    @Override
    public int getCombatStrength() {
        int total = level;
        for (Card item : equippedItems) {
            // Add item bonuses here when implemented
        }
        total += characterClass.getCombatBonus();
        return total;
    }

    /**
     * Get escape bonus for running away.
     * @return Total escape bonus from race and class
     */
    @Override
    public int getEscapeBonus() {
        return race.getEscapeBonus() + characterClass.getEscapeBonus();
    }

    /**
     * Check if player can escape from combat.
     * @return Always true for players (unless blocked by monster)
     */
    @Override
    public boolean canEscape() {
        return true; // Players can always attempt to escape
    }

    /**
     * Help another player in combat.
     * @param targetPlayer The player being helped
     * @param helpStrength The strength contribution
     */
    public void helpPlayer(Player targetPlayer, int helpStrength) {
        System.out.println(name + " pomaga graczowi " + targetPlayer.getName() + " dodając " + helpStrength + " do siły!");
        // Elf gets level for helping
        if (race.getLevelForHelping()) {
            System.out.println(name + " (Elf) otrzymuje poziom za pomoc w walce!");
        }
    }

    /**
     * Level up the player.
     */
    public void levelUp() {
        level++;
        System.out.println(name + " awansuje na poziom " + level + "!");
    }

    /**
     * Level down the player.
     */
    public void levelDown(int levels) {
        level = Math.max(1, level - levels);
        System.out.println(name + " traci poziomy i jest teraz na poziomie " + level + ".");
    }

    /**
     * Reset turn-based abilities.
     */
    public void resetTurnAbilities() {
        usedDoubleSell = false;
        usedSecondEscape = false;
    }

    /**
     * Try to use double sell ability (Halfling).
     * @return true if ability was used
     */
    public boolean tryDoubleSell() {
        if (race.canDoubleSell() && !usedDoubleSell) {
            usedDoubleSell = true;
            return true;
        }
        return false;
    }

    /**
     * Try to use second escape attempt (Halfling).
     * @return true if second attempt is available
     */
    public boolean trySecondEscape() {
        if (race.canRetryEscape() && !usedSecondEscape) {
            usedSecondEscape = true;
            return true;
        }
        return false;
    }

    /**
     * Get max cards allowed in hand based on race.
     * @return Max hand size
     */
    public int getMaxCardsInHand() {
        return race.getHandLimit();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }

    public String getGender() {
        return gender;
    }

    public List<Card> getInventory() {
        return inventory;
    }

    public List<Card> getEquippedItems() {
        return equippedItems;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public String toString() {
        return String.format("%s (Poziom %d, %s, %s)", name, level, characterClass.getName(), race.getName());
    }
}