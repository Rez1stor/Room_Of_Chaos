package chaos_room.model;

public class EquipmentCard extends Card {
    private int bonus;
    private String slot; // "Head", "Body", "Feet", "Hand"
    private int value; // Gold value
    private String inGame;

    public EquipmentCard() {
        super();
    }

    public EquipmentCard(String id, String name, CardType type, String description, int bonus, String slot, int value, String inGame) {
        super(id, name, type, description);
        this.bonus = bonus;
        this.slot = slot;
        this.value = value;
        this.inGame = inGame;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getInGame() {
        return inGame;
    }

    public void setInGame(String inGame) {
        this.inGame = inGame;
    }

    @Override
    public String getDetails() {
        return " [Slot: " + getSlot() + ", Bonus: " + getBonus() + "]";
    }

    @Override
    public boolean use(Player player, chaos_room.view.GameView view) {
        EquipmentCard existing = null;
        for (Card c : player.getPlayerDeck()) {
            if (c instanceof EquipmentCard) {
                EquipmentCard eq = (EquipmentCard) c;
                if (eq.getSlot().equals(this.getSlot())) {
                    existing = eq;
                    break;
                }
            }
        }

        if (existing != null) {
            int choice = view.displayMenu(new String[]{"Yes, replace it", "No, keep current"}, 
                "You already have an item in slot " + this.getSlot() + ": " + existing.getName() + ". Replace it with " + this.getName() + "?");
            if (choice == 0) {
                player.getPlayerDeck().remove(existing);
                player.getInventory().add(existing); // Return old item to inventory
                view.displayMessage("Unequipped " + existing.getName());
                player.addCardToPlayerDeck(this);
                // view.displayMessage("Card added to inventory: " + this.getName()); // Removed redundant message
                player.getInventory().remove(this);
                view.displayMessage(String.format("Equipped %s.", this.getName()));
            } else {
                view.displayMessage("Cancelled.");
            }
        } else {
            player.addCardToPlayerDeck(this);
            // view.displayMessage("Card added to inventory: " + this.getName());
            player.getInventory().remove(this);
            view.displayMessage(String.format("Equipped %s.", this.getName()));
        }
        return false;
    }
}
