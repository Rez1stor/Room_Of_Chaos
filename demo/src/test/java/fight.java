import chaos_room.model.*;
import chaos_room.controller.GameController;

/**
 * Test class for game combat mechanics.
 * Demonstrates testing of multiplayer interactions.
 */
public class fight {
    
    /**
     * Test duel between two players.
     */
    public void duel() {
        // Create players with different classes and races
        Player player1 = new Player("Tyoma", "Male", CharacterClass.WARRIOR, Race.DWARF);
        Player player2 = new Player("Vova", "Male", CharacterClass.MAGE, Race.ELF);

        System.out.println("=== Test gry dla dwoch graczy ===");
        System.out.println("Gracz 1: " + player1);
        System.out.println("Gracz 2: " + player2);
        
        // Test combat strength
        System.out.println("\nSila walki gracza 1: " + player1.getCombatStrength());
        System.out.println("Sila walki gracza 2: " + player2.getCombatStrength());
        
        // Test class abilities
        System.out.println("\n" + player1.getName() + " (Wojownik) wygrywa remisy: " + player1.getCharacterClass().canWinOnTie());
        System.out.println(player2.getName() + " (Elf) otrzymuje poziom za pomoc: " + player2.getRace().getLevelForHelping());
        
        // Test helping mechanism
        System.out.println("\n=== Test mechaniki pomocy ===");
        player1.helpPlayer(player2, player1.getCombatStrength());
    }
    
    /**
     * Test multiplayer game with controller.
     */
    public void testMultiplayerSetup() {
        GameController controller = new GameController();
        
        // Add multiple players
        Player p1 = new Player("Player1", "Male", CharacterClass.WARRIOR, Race.DWARF);
        Player p2 = new Player("Player2", "Female", CharacterClass.THIEF, Race.HALFLING);
        Player p3 = new Player("Player3", "Male", CharacterClass.CLERIC, Race.ELF);
        
        controller.addPlayer(p1);
        controller.addPlayer(p2);
        controller.addPlayer(p3);
        
        System.out.println("\n=== Test wielu graczy ===");
        System.out.println("Liczba graczy: " + controller.getPlayers().size());
        System.out.println("Aktualny gracz: " + controller.getCurrentPlayer().getName());
        
        // Test turn switching
        controller.nextPlayer();
        System.out.println("Po zmianie tury: " + controller.getCurrentPlayer().getName());
    }
    
    /**
     * Main method to run tests.
     */
    public static void main(String[] args) {
        fight test = new fight();
        test.duel();
        test.testMultiplayerSetup();
    }
}
