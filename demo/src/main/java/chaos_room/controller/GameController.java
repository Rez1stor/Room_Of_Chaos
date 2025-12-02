package chaos_room.controller;

import chaos_room.model.*;
import chaos_room.view.Menu;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Game Controller - manages game flow and player interactions.
 * Implements MVC pattern by separating game logic from view.
 * Demonstrates OOP principles: abstraction, encapsulation, and single responsibility.
 */
public class GameController {
    private List<Player> players;
    private int currentPlayerIndex;
    private int turnNumber;
    private Random random;
    private boolean gameOver;
    private static final int WINNING_LEVEL = 10;

    public GameController() {
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.turnNumber = 0;
        this.random = new Random();
        this.gameOver = false;
    }

    /**
     * Add a player to the game.
     * @param player Player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
        System.out.println("Gracz " + player.getName() + " dołączył do gry!");
    }

    /**
     * Get the current active player.
     * @return Current player
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    /**
     * Get all players in the game.
     * @return List of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Start a new game with registered players.
     */
    public void startGame() {
        if (players.size() < 2) {
            System.out.println("Potrzeba co najmniej 2 graczy, aby rozpocząć grę!");
            return;
        }
        
        System.out.println("\n=== GRA ROZPOCZYNA SIĘ ===");
        System.out.println("Gracze w grze:");
        for (Player p : players) {
            System.out.println("  - " + p);
        }
        System.out.println("\nPierwszy gracz: " + getCurrentPlayer().getName());
        
        gameLoop();
    }

    /**
     * Main game loop.
     */
    private void gameLoop() {
        while (!gameOver) {
            executeTurn();
            checkWinCondition();
            nextPlayer();
        }
    }

    /**
     * Execute a single turn for the current player.
     */
    public void executeTurn() {
        Player current = getCurrentPlayer();
        turnNumber++;
        current.resetTurnAbilities();
        
        System.out.println("\n--- Tura " + turnNumber + ": " + current.getName() + " ---");
        System.out.println("Poziom: " + current.getLevel() + " | Klasa: " + current.getCharacterClass().getName() + " | Rasa: " + current.getRace().getName());
        
        // Phase 1: Draw card (first action)
        System.out.println("\n[Faza 1] Ciągniesz kartę z drzwi...");
        current.drawCard();
        
        // Phase 2: Additional action - draw or discard
        System.out.println("\n[Faza 2] Wybierz dodatkową akcję:");
        String[] options = {"Ciągnij kolejną kartę", "Odrzuć kartę (zakrytą)", "Pomiń"};
        int choice = Menu.displayMenu(options);
        
        switch (choice) {
            case 0:
                current.drawCard();
                break;
            case 1:
                handleDiscardCard(current);
                break;
            case 2:
                System.out.println("Pomijasz dodatkową akcję.");
                break;
        }
        
        // Phase 3: Player interaction options
        handlePlayerInteraction(current);
    }

    /**
     * Handle card discard action.
     */
    private void handleDiscardCard(Player player) {
        if (player.getInventory().isEmpty()) {
            System.out.println("Nie masz kart do odrzucenia.");
            return;
        }
        
        int cardIndex = Menu.displayMenu(player.getInventory().toArray());
        Card discarded = player.discardCard(cardIndex);
        if (discarded != null) {
            System.out.println("Odrzuciłeś kartę: " + discarded.getName() + " (zakrytą)");
        }
    }

    /**
     * Handle player interaction options.
     */
    private void handlePlayerInteraction(Player current) {
        String[] options = {"Poproś o pomoc innego gracza", "Handluj z innym graczem", "Użyj karty", "Zakończ turę"};
        
        System.out.println("\n[Faza 3] Interakcja z graczami:");
        int choice = Menu.displayMenu(options);
        
        switch (choice) {
            case 0:
                requestHelp(current);
                break;
            case 1:
                tradeWithPlayer(current);
                break;
            case 2:
                current.selectCard();
                break;
            case 3:
                System.out.println("Kończysz turę.");
                break;
        }
    }

    /**
     * Request help from another player in combat.
     */
    public void requestHelp(Player requestingPlayer) {
        List<Player> otherPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p != requestingPlayer) {
                otherPlayers.add(p);
            }
        }
        
        if (otherPlayers.isEmpty()) {
            System.out.println("Nie ma innych graczy, którzy mogliby pomóc.");
            return;
        }
        
        System.out.println(requestingPlayer.getName() + " prosi o pomoc!");
        String[] playerNames = otherPlayers.stream().map(Player::getName).toArray(String[]::new);
        int selected = Menu.displayMenu(playerNames);
        
        Player helper = otherPlayers.get(selected);
        String[] response = {"Pomogę!", "Odmawiam"};
        System.out.println("\n" + helper.getName() + ", czy chcesz pomóc?");
        int helpChoice = Menu.displayMenu(response);
        
        if (helpChoice == 0) {
            int helpStrength = helper.getCombatStrength();
            helper.helpPlayer(requestingPlayer, helpStrength);
            System.out.println("Łączna siła walki: " + (requestingPlayer.getCombatStrength() + helpStrength));
        } else {
            System.out.println(helper.getName() + " odmawia pomocy.");
        }
    }

    /**
     * Trade cards with another player.
     */
    public void tradeWithPlayer(Player trader) {
        List<Player> otherPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p != trader) {
                otherPlayers.add(p);
            }
        }
        
        if (otherPlayers.isEmpty()) {
            System.out.println("Nie ma innych graczy do handlu.");
            return;
        }
        
        System.out.println("Wybierz gracza do handlu:");
        String[] playerNames = otherPlayers.stream().map(Player::getName).toArray(String[]::new);
        int selected = Menu.displayMenu(playerNames);
        
        Player partner = otherPlayers.get(selected);
        System.out.println("Rozpoczynasz handel z " + partner.getName());
        // Trade implementation would go here
        System.out.println("(Funkcja handlu w trakcie implementacji)");
    }

    /**
     * Simulate combat between player and monster.
     */
    public boolean combat(Player player, MonsterCard monster, Player helper) {
        int playerStrength = player.getCombatStrength();
        if (helper != null) {
            playerStrength += helper.getCombatStrength();
        }
        
        int monsterStrength = monster.getLevel();
        
        System.out.println("\n=== WALKA ===");
        System.out.println(player.getName() + " (siła: " + playerStrength + ") vs " + monster.getName() + " (poziom: " + monsterStrength + ")");
        
        if (playerStrength > monsterStrength) {
            System.out.println("ZWYCIĘSTWO!");
            player.levelUp();
            if (helper != null && helper.getRace().getLevelForHelping()) {
                helper.levelUp();
            }
            return true;
        } else if (playerStrength == monsterStrength && player.getCharacterClass().canWinOnTie()) {
            System.out.println("Remis - ale " + player.getCharacterClass().getName() + " wygrywa remisy!");
            player.levelUp();
            return true;
        } else {
            System.out.println("PORAŻKA! Musisz spróbować ucieczki.");
            return attemptEscape(player, monster);
        }
    }

    /**
     * Attempt to escape from a monster.
     */
    private boolean attemptEscape(Player player, MonsterCard monster) {
        int roll = random.nextInt(6) + 1;
        int escapeBonus = player.getEscapeBonus();
        int totalRoll = roll + escapeBonus;
        
        System.out.println("Rzut na ucieczkę: " + roll + " + " + escapeBonus + " = " + totalRoll);
        
        if (totalRoll >= 5) {
            System.out.println("Udało się uciec!");
            return true;
        } else {
            System.out.println("Nie udało się uciec...");
            
            // Second escape attempt for Halfling
            if (player.trySecondEscape()) {
                System.out.println(player.getName() + " (Niziołek) próbuje ponownie!");
                return attemptEscape(player, monster);
            }
            
            // Apply nasty effect
            System.out.println("Efekt paskudztwa: " + monster.getNastyEffect());
            return false;
        }
    }

    /**
     * Move to the next player.
     */
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Check if any player has reached the winning level.
     */
    private void checkWinCondition() {
        for (Player p : players) {
            if (p.getLevel() >= WINNING_LEVEL) {
                System.out.println("\n=== KONIEC GRY ===");
                System.out.println("ZWYCIĘZCA: " + p.getName() + " osiągnął poziom " + p.getLevel() + "!");
                gameOver = true;
                return;
            }
        }
    }

    /**
     * Check if game is over.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Get current turn number.
     */
    public int getTurnNumber() {
        return turnNumber;
    }
}
