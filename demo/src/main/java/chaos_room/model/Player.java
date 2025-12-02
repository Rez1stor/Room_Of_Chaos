package chaos_room.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements Combatant {
    private String name;
    private int level;
    private int strength;
    private List<Card> inventory;
    private List<Card> playerDeck;
    private String gender;
    private int maxCardsInHand = 5;
    private int escapeChances = 4;
    private static Random random = new Random();

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
        level = 1;
        strength = level;
        inventory = new ArrayList<>();   
        playerDeck = new ArrayList<>();
    }

    public Card drawDoorCard() {
        Deck deck = new Deck();
        int randomIndex = random.nextInt(deck.cards.size());
        return deck.cards.get(randomIndex);
    }

    public Card drawTreasureCard() {
        Deck deck = new Deck();
        int randomIndex = random.nextInt(deck.treasureCards.size());
        return deck.treasureCards.get(randomIndex);
    }

    public void starterPack(){
        for (int i = 0; i < 4; i++) {
            Card card = drawDoorCard();
            inventory.add(card);
        }
    }

    public void addCardToPlayerDeck(Card card) {
        playerDeck.add(card);
    }

    public String getName() {
        return name;
    }
    
    public int getEscapeChances() {
        return escapeChances;
    }

    public int getLevel() {
        return level;
    }

    public int getStrength() {
        int totalStrength = level;
        for (Card c : playerDeck) {
            if (c instanceof EquipmentCard) {
                totalStrength += ((EquipmentCard) c).getBonus();
            }
        }
        return totalStrength;
    }

    public String getGender() {
        return gender;         
    }

    public List<Card> getInventory() {
        return inventory;
    }

    public List<Card> getPlayerDeck() {
        return playerDeck;
    }

    public int getMaxCardsInHand() {
        return maxCardsInHand;
    }

    public int setEscapeChances(int escapeChances) {
        return this.escapeChances = escapeChances;
    }

    public int setStrength(){
        return strength;
    }

    public void levelUp(int increment) {
        level += increment;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlayerCharacter() {
        for (Card c : playerDeck) {
            if (c instanceof CharacterCard) {
                return ((CharacterCard) c).getName();
            }
        }
        return null;
    }
}