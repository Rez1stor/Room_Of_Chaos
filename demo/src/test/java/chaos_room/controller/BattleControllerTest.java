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

class BattleControllerTest {

    private BattleController controller;
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
        controller = new BattleController(game, mockView);
    }

    @Test
    void testHandleMonsterDefeated() throws Exception {
        MonsterCard monster = new MonsterCard("m1", "Slime", CardType.MONSTER, "Weak", 1, "None", 1, 1, "0", null, "drawPile", 1);
        
        Method method = BattleController.class.getDeclaredMethod("handleMonsterDefeated", MonsterCard.class, Player.class);
        method.setAccessible(true);
        method.invoke(controller, monster, player);

        assertEquals(2, player.getLevel()); // Started at 1, gained 1
        verify(mockView, atLeastOnce()).displayMessage(contains("Congratulations"));
    }

    @Test
    void testHandlePlayerDefeatedWithLevelLoss() throws Exception {
        player.setLevel(5);
        MonsterCard monster = new MonsterCard("m2", "Dragon", CardType.MONSTER, "Strong", 10, "Lose 2 levels", 2, 1, "2", null, "drawPile", 10);

        Method method = BattleController.class.getDeclaredMethod("handlePlayerDefeated", MonsterCard.class, Player.class);
        method.setAccessible(true);
        method.invoke(controller, monster, player);

        assertEquals(3, player.getLevel());
        verify(mockView, atLeastOnce()).displayMessage(contains("You lost 2 levels"));
    }
}
