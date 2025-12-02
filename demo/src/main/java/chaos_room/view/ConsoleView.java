package chaos_room.view;
import chaos_room.model.*;
import chaos_room.controller.GameController;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Console View - handles all console input/output.
 * Implements MVC pattern by separating display logic from game logic.
 * Game logic is now delegated to GameController.
 */
public class ConsoleView {
    private GameController controller;
    private Scanner scanner;

    public ConsoleView() {
        this.controller = new GameController();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display welcome message with animated text.
     */
    public void displayWelcomeMessage() throws InterruptedException {
        String text = "Witaj w LootQuest: Komnata Chaosu!\n" +
                        "Powodzenia, bohaterze!";
        printWithDelay(text, 50);
        System.out.println("------------------------------");
    }

    /**
     * Print text with delay animation.
     */
    public void printWithDelay(String text, int delayMillis) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(delayMillis);
        }
        System.out.println();
    }

    /**
     * Start the game setup with multiple players.
     */
    public void startGameSetup() throws InterruptedException {
        displayWelcomeMessage();
        
        System.out.print("Ile graczy bedzie grac? (2-6): ");
        int numPlayers = getNumberInput(2, 6);
        
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("\n=== Rejestracja gracza " + i + " ===");
            Player player = registration();
            controller.addPlayer(player);
            players.add(player);
        }
        
        System.out.println("\n=== Wszyscy gracze zarejestrowani! ===");
        for (Player p : players) {
            System.out.println("  - " + p);
        }
        
        System.out.println("\nCzy chcesz rozpoczac gre?");
        String[] options = {"Tak, zaczynamy!", "Nie, wyjdz"};
        int choice = Menu.displayMenu(options);
        
        if (choice == 0) {
            controller.startGame();
        } else {
            System.out.println("Do zobaczenia!");
        }
    }

    /**
     * Register a single player with class and race selection.
     */
    public Player registration() {
        System.out.print("Podaj nazwe swojego bohatera: ");
        String name = scanner.nextLine();
        
        // Gender selection
        System.out.println("Wybierz plec:");
        int genderSelected = Menu.displayMenu(new String[]{"Mezczyzna", "Kobieta"});
        String gender = genderSelected == 0 ? "Male" : "Female";
        
        // Class selection
        System.out.println("Wybierz klase postaci:");
        CharacterClass[] classes = CharacterClass.values();
        String[] classOptions = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            classOptions[i] = classes[i].toString();
        }
        int classSelected = Menu.displayMenu(classOptions);
        CharacterClass selectedClass = classes[classSelected];
        
        // Race selection
        System.out.println("Wybierz rase postaci:");
        Race[] races = Race.values();
        String[] raceOptions = new String[races.length];
        for (int i = 0; i < races.length; i++) {
            raceOptions[i] = races[i].toString();
        }
        int raceSelected = Menu.displayMenu(raceOptions);
        Race selectedRace = races[raceSelected];
        
        Player player = new Player(name, gender, selectedClass, selectedRace);
        System.out.println("\nBohater utworzony: " + player);
        
        return player;
    }

    /**
     * Get a number input within range.
     */
    private int getNumberInput(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine();
                int num = Integer.parseInt(input);
                if (num >= min && num <= max) {
                    return num;
                }
                System.out.print("Podaj liczbe od " + min + " do " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Nieprawidlowa liczba. Sprobuj ponownie: ");
            }
        }
    }

    /**
     * Display player status.
     */
    public void displayPlayerStatus(Player player) {
        System.out.println("\n--- Status gracza: " + player.getName() + " ---");
        System.out.println("Poziom: " + player.getLevel());
        System.out.println("Klasa: " + player.getCharacterClass().getName());
        System.out.println("Rasa: " + player.getRace().getName());
        System.out.println("Sila walki: " + player.getCombatStrength());
        System.out.println("Karty w rece: " + player.getInventory().size() + "/" + player.getMaxCardsInHand());
    }

    /**
     * Get the game controller.
     */
    public GameController getController() {
        return controller;
    }
}
