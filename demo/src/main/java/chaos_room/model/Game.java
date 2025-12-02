package chaos_room.model;
import chaos_room.controller.GameController;
import java.util.List;
import java.util.Random;

/**
 * Main Game class - represents the game state.
 * Demonstrates OOP principle: composition (has-a GameController).
 */
public class Game {
    private GameController controller;
    private boolean isRunning;

    public Game() {
        this.controller = new GameController();
        this.isRunning = false;
    }

    /**
     * Initialize the game with players.
     * @param players List of players
     */
    public void initialize(List<Player> players) {
        for (Player player : players) {
            controller.addPlayer(player);
        }
    }

    /**
     * Start the game loop.
     */
    public void start() {
        isRunning = true;
        controller.startGame();
    }

    /**
     * Get the game controller.
     * @return GameController instance
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Check if the game is running.
     * @return true if game is in progress
     */
    public boolean isRunning() {
        return isRunning && !controller.isGameOver();
    }
}
