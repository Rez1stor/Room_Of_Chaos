package chaos_room.view;

import chaos_room.model.Card;
import chaos_room.model.Player;

public interface GameView {
    void displayMessage(String message);
    void waitForInput();
    String getCardInfo(Card card);
    void displayGameOver(Player player);
    int displayMenu(Object[] options, String title);
}
