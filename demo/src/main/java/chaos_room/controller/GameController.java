package chaos_room.controller;

import chaos_room.model.*;
import chaos_room.view.GameView;

public class GameController {
    private Game game;
    private GameView view;
    private BattleController battleController;
    private InventoryController inventoryController;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
        this.battleController = new BattleController(game, view);
        this.inventoryController = new InventoryController(view, this.battleController);
    }

    public void startGame() {
        while (true) {
            Player currentPlayer = game.getCurrentPlayer();
            view.displayMessage(String.format("Turn %d: Player %s's move", game.getTurn(), currentPlayer.getName()));
            playTurn(currentPlayer);
            game.nextTurn();
            view.displayMessage(currentPlayer.getName() + " has finished their turn.");
        }
    }

    private void playTurn(Player player) {
        // Win at level 10
        if (player.getLevel() >= 10) {
            try {
                gameOver(player);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        boolean turnOver = false;
        while (!turnOver) {
            String race = "Human";
            String pClass = "Citizen";

            for (Card c : player.getPlayerDeck()) {
                if (CardType.RACE == c.getType()) {
                    race = c.getName();
                } else if (CardType.CLASS == c.getType()) {
                    pClass = c.getName();
                }
            }
            String title = String.format("Turn %d | Player: %s (Lvl %d) (Strength %d)\n(Character %s %s) | Choose action:", game.getTurn(), player.getName(), player.getLevel(), player.getStrength(), race, pClass);
            int choice = view.displayMenu(new String[]{"Manage Inventory", "View Equipped Cards", "Kick Open Door (Draw Card)", "End Turn"}, title);
            switch (choice) {
                case 0:
                    boolean fought = inventoryController.handleInventoryManagement(player);
                    if (fought) turnOver = true;
                    break;
                case 1:
                    inventoryController.showPlayerDeck(player);
                    view.waitForInput();
                    break;
                case 2:
                    // Phase 1: Kick Open The Door
                    view.displayMessage(player.getName() + " kicks open the door!");
                    Card drawnCard = player.drawDoorCard(); 
                    view.displayMessage("Player " + player.getName() + " drew a card: " + drawnCard.getName());
                    
                    if (drawnCard instanceof MonsterCard) {
                        view.displayMessage("It's a monster! " + drawnCard.getName() + " (Level " + ((MonsterCard)drawnCard).getLevel() + ")");
                        view.waitForInput();
                        battleController.handleFight((MonsterCard) drawnCard, player);
                    } else {
                        if (player.getInventory().size() >= player.getMaxCardsInHand()) {
                            view.displayMessage("Inventory full! You cannot carry more cards. " + drawnCard.getName() + " is discarded.");
                        } else {
                            view.displayMessage("You found a " + drawnCard.getName() + " (" + drawnCard.getDescription() + "). Added to inventory.");
                            player.getInventory().add(drawnCard);
                        }
                        view.waitForInput();
                    }
                    turnOver = true;
                    break;
                case 3:
                    view.displayMessage(player.getName() + " ends their turn.");
                    turnOver = true;
                    break;
            }
        }
    }

    private void gameOver(Player player) throws InterruptedException {
        view.displayGameOver(player);
        System.exit(0);
    }
}
