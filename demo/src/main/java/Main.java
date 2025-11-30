import chaos_room.model.*;
import chaos_room.view.ConsoleView;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConsoleView view = new ConsoleView();
        view.displayWelcomeMessage();
        // Card card = Card.getRandomCardFromJson(Main.class.getClassLoader().getResource("CharacterCards.json").getPath());
        // System.out.println(card);
    }
}
