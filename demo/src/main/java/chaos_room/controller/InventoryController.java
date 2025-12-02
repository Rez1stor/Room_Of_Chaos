package chaos_room.controller;

import chaos_room.model.*;
import chaos_room.view.GameView;

import java.util.List;

public class InventoryController {
    private GameView view;
    private BattleController battleController;

    public InventoryController(GameView view, BattleController battleController) {
        this.view = view;
        this.battleController = battleController;
    }

    public boolean handleInventoryManagement(Player player) {
        List<Card> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            view.displayMessage("Inventory is empty.");
            return false;
        }
        view.waitForInput();
        String[] cardNames = new String[inventory.size() + 1];
        for (int i = 0; i < inventory.size(); i++) {
            Card c = inventory.get(i);
            String details = c.getDetails();
            cardNames[i] = c.getName() + " (Type: " + c.getType() + ")" + details;
        }
        cardNames[inventory.size()] = "Back";

        int selected = view.displayMenu(cardNames, String.format("[%s] Select a card from inventory (%d/%d cards)", player.getName(), inventory.size(), player.getMaxCardsInHand()));
        
        if (selected == inventory.size()) return false;

        Card chosenCard = inventory.get(selected);
        
        int choice = view.displayMenu(new String[]{"Use card", "Discard card", "Cancel"}, view.getCardInfo(chosenCard));
        switch (choice) {
            case 0:
                return handleCardUsage(player, chosenCard);
            case 1:
                view.displayMessage("You discard the card: " + chosenCard.getName());
                inventory.remove(chosenCard);
                return false;
            case 2:
                view.displayMessage("Card selection canceled.");
                return false;
            default:
                view.displayMessage("Invalid option.");
                return false;
        }
    }

    private boolean handleCardUsage(Player player, Card card) {
        view.displayMessage("You use the card: " + card.getName());
        
        boolean result = card.use(player, view);

        // If player uses a Monster card from hand (e.g. "Looking for Trouble"), start a fight immediately
        if (card.getType() == CardType.MONSTER) {
            battleController.handleFight((MonsterCard) card, player);
            return true;
        }

        return result;
    }

    public void showPlayerDeck(Player player) {
        view.displayMessage("Deck of player " + player.getName() + ":");
        for (Card card : player.getPlayerDeck()) {
            String details = card.getDetails();
            view.displayMessage(String.format("- %s (Type: %s, Description: %s)%s", card.getName(), card.getType(), card.getDescription(), details));
        }
    }
}
