import chaos_room.model.*;
import chaos_room.view.ConsoleView;
import chaos_room.controller.GameController;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConsoleView view = new ConsoleView();
        view.displayWelcomeMessage();
        
        int playerCount = view.askForNumberOfPlayers();
        List<Player> players = new ArrayList<>();
        
        for (int i = 0; i < playerCount; i++) {
            view.displayMessage("Registering Player " + (i + 1));
            players.add(view.registration());
            view.displaySeparator();
        }
        
        Game game = new Game(players);
        GameController controller = new GameController(game, view);

        for (Player p : players) {
            p.starterPack();
            view.displayMessage("Inventory of player " + p.getName() + ":");
            for (Card card : p.getInventory()) {
                view.displayMessage(String.format("- %s (Type: %s, Description: %s)", card.getName(), card.getType(), card.getDescription()));
            }
        }
        controller.startGame();
    }
}
