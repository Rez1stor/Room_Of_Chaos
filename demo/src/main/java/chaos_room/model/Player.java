package chaos_room.model;
import chaos_room.view.Menu;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;

import static chaos_room.model.GameConstants.*;

/**
 * Represents a player in the game.
 * Contains player's race, class, level, inventory, and abilities.
 * Demonstrates OOP Encapsulation and Composition (has-a relationship with Race and CharacterClass).
 */
public class Player {
    private String name;
    private int level;
    private int strength;
    private List<Card> inventory;
    private List<Card> playerDeck;
    private String gender;
    private int maxCardsInHand = DEFAULT_HAND_LIMIT;
    private static Random random = new Random();
    
    // Race and Class - core Munchkin mechanics
    private Race race;
    private CharacterClass characterClass;
    
    // Equipment slots
    private Card headgear;
    private Card armor;
    private Card footgear;
    private Card leftHand;
    private Card rightHand;

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
        level = 1;
        strength = 0;
        inventory = new ArrayList<>();
        playerDeck = new ArrayList<>();
        
        StarterPack();
    }
    
    /**
     * Full constructor with race and class.
     */
    public Player(String name, String gender, Race race, CharacterClass characterClass) {
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.characterClass = characterClass;
        level = 1;
        strength = 0;
        inventory = new ArrayList<>();
        playerDeck = new ArrayList<>();
        
        // Apply race-specific hand limit
        if (race != null) {
            int handLimit = race.getHandLimitModifier();
            if (handLimit > 0) {
                this.maxCardsInHand = handLimit;
            }
        }
        
        StarterPack();
    }

    public void getCard() {
        if (inventory.size() >= maxCardsInHand) {
            System.out.println("Nie możesz wziąć więcej kart, Twój ekwipunek jest pełny.");
            return;
        }else{
            inventory.add(Card.getRandomCardFromJson("DoorCards.json"));
        }
    }

    private void StarterPack(){
        for (int i = 0; i < 4; i++) getCard();
        showInventory();
    }

    public void showInventory(){
        System.out.println("Ekwipunek gracza " + name + ":");
        for (Card card : inventory) {
            System.out.printf("- %s (Typ: %s, Opis: %s)%n", card.getName(), card.getType(), card.getDescription());
        }
    }

    public void selectCard(){
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

    public void useCard(Card card){
        System.out.println("Używasz karty: " + card.getName());
        inventory.remove(card);
        playerDeck.add(card);
    }
    
    /**
     * Discards a card from inventory.
     * @param card the card to discard
     * @return true if card was discarded
     */
    public boolean discardCard(Card card) {
        return inventory.remove(card);
    }
    
    /**
     * Adds a level to the player.
     * @param amount number of levels to add
     */
    public void addLevel(int amount) {
        this.level += amount;
        if (this.level > MAX_LEVEL) {
            this.level = MAX_LEVEL;
        }
    }
    
    /**
     * Loses levels (minimum level is 1).
     * @param amount number of levels to lose
     */
    public void loseLevel(int amount) {
        this.level -= amount;
        if (this.level < MIN_LEVEL) {
            this.level = MIN_LEVEL;
        }
    }
    
    /**
     * Sets the player's race.
     * @param race the race to set
     */
    public void setRace(Race race) {
        this.race = race;
        // Update hand limit based on race
        if (race != null) {
            int handLimit = race.getHandLimitModifier();
            if (handLimit > 0) {
                this.maxCardsInHand = handLimit;
            }
        }
    }
    
    /**
     * Sets the player's class.
     * @param characterClass the class to set
     */
    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }
    
    /**
     * Calculates total combat strength including equipment and bonuses.
     * @return total combat strength
     */
    public int calculateCombatStrength() {
        return level + strength;
    }
    
    /**
     * Checks if player has won the game (reached level 10).
     * @return true if player has won
     */
    public boolean hasWon() {
        return level >= MAX_LEVEL;
    }
    
    /**
     * Gets a description of the player's current state.
     * @return player description
     */
    public String getPlayerDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("【%s】 Poziom %d\n", name, level));
        sb.append(String.format("  Płeć: %s\n", getGenderDisplayName(gender)));
        if (race != null) {
            sb.append(String.format("  Rasa: %s\n", race.getName()));
        }
        if (characterClass != null) {
            sb.append(String.format("  Klasa: %s\n", characterClass.getName()));
        }
        sb.append(String.format("  Karty na ręce: %d/%d\n", inventory.size(), maxCardsInHand));
        return sb.toString();
    }

    // Getters
    public String getName() {
        return name;
    }
    
    public int getLevel() {
        return level;
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
    
    public Race getRace() {
        return race;
    }
    
    public CharacterClass getCharacterClass() {
        return characterClass;
    }
    
    public int getMaxCardsInHand() {
        return maxCardsInHand;
    }
    
    public List<Card> getPlayerDeck() {
        return playerDeck;
    }
    
    @Override
    public String toString() {
        return getPlayerDescription();
    }
}