package chaos_room.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Card implements Usable {
    private String id;
    private String name;
    private CardType type;
    private String description;

    public Card() {}

    public Card(String id, String name, CardType type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }
    
    @Override
    public boolean use(Player player, chaos_room.view.GameView view) {
        // Default implementation: just add to deck/inventory if not handled by subclass?
        // Or throw exception?
        // In original code: "else { player.addCardToPlayerDeck(card); ... }"
        player.addCardToPlayerDeck(this);
        view.displayMessage("Card added to inventory: " + this.getName());
        view.displayMessage(String.format("Card %s added to player's deck.", this.getName()));
        player.getInventory().remove(this);
        return false;
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

    public Card setName(String name) {
        this.name = name;
        return this;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String getDetails() {
        return "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Card{id='%s', name='%s', type='%s', description='%s'}", id, name, type, description);
    }

}