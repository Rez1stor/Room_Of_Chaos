package chaos_room.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of cards in the game.
 * Demonstrates OOP principles: encapsulation (private list with controlled access).
 */
public class Deck {
    private List<Card> cards;
    private List<Card> discardPile;
    private String deckType;

    public Deck(String deckType) {
        this.deckType = deckType;
        this.cards = new ArrayList<>();
        this.discardPile = new ArrayList<>();
    }

    /**
     * Add a card to the deck.
     * @param card Card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Draw the top card from the deck.
     * @return Top card, or null if deck is empty
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            reshuffleDiscard();
        }
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Discard a card to the discard pile.
     * @param card Card to discard
     */
    public void discardCard(Card card) {
        discardPile.add(card);
    }

    /**
     * Shuffle the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Reshuffle the discard pile into the deck.
     */
    public void reshuffleDiscard() {
        cards.addAll(discardPile);
        discardPile.clear();
        shuffle();
        System.out.println("Talia " + deckType + " została przetasowana z odrzuconymi kartami.");
    }

    /**
     * Get the number of remaining cards in the deck.
     * @return Number of cards
     */
    public int remainingCards() {
        return cards.size();
    }

    /**
     * Get the deck type.
     * @return Deck type name
     */
    public String getDeckType() {
        return deckType;
    }

    /**
     * Get the discard pile for viewing (e.g., for Cleric ability).
     * @return Discard pile
     */
    public List<Card> getDiscardPile() {
        return new ArrayList<>(discardPile);
    }
}
