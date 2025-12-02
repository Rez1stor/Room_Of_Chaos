import chaos_room.model.*;
import chaos_room.view.ConsoleView;
import chaos_room.controller.GameController;

/**
 * Main entry point for the LootQuest: Komnata Chaosu game.
 * Demonstrates the use of MVC pattern and OOP principles.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConsoleView view = new ConsoleView();
        view.startGameSetup();
    }
}
