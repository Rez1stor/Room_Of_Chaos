package chaos_room.model;
import chaos_room.view.ConsoleView;
import chaos_room.model.Card;
import java.util.List;
import chaos_room.model.Player;

public class Deck {
    private List<Card> cards;
    private List<Player> owner;

    public Deck(List<Player> owner) {
        this.owner = owner;
    }

    private void addCard(Card card){
        cards.add(card);
    }


}
