package chaos_room.controller;

import chaos_room.model.*;
import chaos_room.view.ConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

class GameControllerTest {

    private GameController controller;
    private Game game;
    private Player player;

    @Mock
    private ConsoleView mockView;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        player = new Player("TestHero", "Male");
        List<Player> players = new ArrayList<>();
        players.add(player);
        
        game = new Game(players);
        controller = new GameController(game, mockView);
    }

    @Test
    void testControllerInitialization() {
        assertNotNull(controller);
    }
}