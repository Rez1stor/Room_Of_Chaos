package chaos_room.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("P1", "Male");
        player2 = new Player("P2", "Female");
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        game = new Game(players);
    }

    @Test
    void testNextTurn() {
        assertEquals(1, game.getTurn());
        assertEquals(player1, game.getCurrentPlayer());

        game.nextTurn();

        assertEquals(2, game.getTurn());
        assertEquals(player2, game.getCurrentPlayer());
        
        game.nextTurn();
        
        assertEquals(3, game.getTurn());
        assertEquals(player1, game.getCurrentPlayer());
    }
}
