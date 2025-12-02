package chaos_room.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("TestHero", "Male");
    }

    @Test
    void testLevelUp() {
        assertEquals(1, player.getLevel());
        player.levelUp(1);
        assertEquals(2, player.getLevel());
    }

    @Test
    void testStrengthCalculationWithoutEquipment() {
        player.setLevel(5);
        assertEquals(5, player.getStrength());
    }

    @Test
    void testStrengthCalculationWithEquipment() {
        player.setLevel(3);
        
        EquipmentCard sword = new EquipmentCard("sword", "Sword of Testing", CardType.EQUIPMENT, "Sharp blade", 3, "Hand", 150, "drawPile");
        player.addCardToPlayerDeck(sword);

        assertEquals(6, player.getStrength()); // 3 (Level) + 3 (Sword)
    }

    @Test
    void testEquipItem() {
        EquipmentCard helmet = new EquipmentCard("helm", "Helmet", CardType.EQUIPMENT, "Hard", 2, "Head", 100, "drawPile");
        
        // Add to inventory first (simulating drawing it)
        player.getInventory().add(helmet);
        
        player.addCardToPlayerDeck(helmet);
        assertTrue(player.getPlayerDeck().contains(helmet));
        assertEquals(3, player.getStrength()); // 1 (Level) + 2 (Helmet)
    }

    @Test
    void testInventoryManagement() {
        Card card = new Card("potion", "Health Potion", CardType.TREASURE, "Restores health") {};
        player.getInventory().add(card);
        
        assertEquals(1, player.getInventory().size());
        assertTrue(player.getInventory().contains(card));
        
        player.getInventory().remove(card);
        assertTrue(player.getInventory().isEmpty());
    }
}
