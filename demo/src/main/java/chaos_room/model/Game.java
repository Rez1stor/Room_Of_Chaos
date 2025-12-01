package chaos_room.model;
import chaos_room.view.ConsoleView;
import chaos_room.view.Menu;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game controller that manages game flow, turns, and state.
 * Implements Munchkin-like gameplay mechanics.
 * Demonstrates OOP Facade pattern - provides simple interface to complex subsystem.
 */
public class Game {
    public int turn;
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameOver;
    private Random random;
    private ConsoleView view;
    
    // Decks
    private List<MonsterCard> doorDeck;
    private List<Card> treasureDeck;
    private List<Card> discardPile;
    
    // Available races and classes
    private List<Race> availableRaces;
    private List<CharacterClass> availableClasses;

    public Game() {
        this.turn = 0;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.random = new Random();
        this.discardPile = new ArrayList<>();
        
        // Load door deck (monsters)
        this.doorDeck = MonsterCard.loadMonstersFromJson("DoorCards.json");
        
        // Load available races and classes
        this.availableRaces = Race.loadRacesFromJson("CharacterCards.json");
        this.availableClasses = CharacterClass.loadClassesFromJson("CharacterCards.json");
    }
    
    /**
     * Starts the game with player creation.
     */
    public void GameStart(){
        System.out.println("=== ROZPOCZĘCIE GRY ===");
        System.out.println("Cel: Pierwszy gracz, który osiągnie 10 poziom, wygrywa!");
        System.out.println();
    }
    
    /**
     * Adds a player to the game.
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    /**
     * Creates a player with race and class selection.
     * @param name player name
     * @param gender player gender
     * @return the created player
     */
    public Player createPlayer(String name, String gender) {
        // Select race
        Race selectedRace = selectRace();
        
        // Select class
        CharacterClass selectedClass = selectClass();
        
        Player player = new Player(name, gender, selectedRace, selectedClass);
        addPlayer(player);
        
        System.out.println("\n=== BOHATER UTWORZONY ===");
        System.out.println(player.getPlayerDescription());
        
        return player;
    }
    
    /**
     * Displays race selection menu.
     * @return selected race
     */
    private Race selectRace() {
        if (availableRaces == null || availableRaces.isEmpty()) {
            return null;
        }
        
        String[] raceNames = new String[availableRaces.size()];
        for (int i = 0; i < availableRaces.size(); i++) {
            Race race = availableRaces.get(i);
            raceNames[i] = String.format("%s - %s", race.getName(), race.getDescription());
        }
        
        System.out.println("\n=== WYBÓR RASY ===");
        int selected = Menu.displayMenu(raceNames);
        if (selected >= 0 && selected < availableRaces.size()) {
            Race race = availableRaces.get(selected);
            System.out.println("Wybrano rasę: " + race.getName());
            printAbilities(race);
            return race;
        }
        return null;
    }
    
    /**
     * Displays class selection menu.
     * @return selected class
     */
    private CharacterClass selectClass() {
        if (availableClasses == null || availableClasses.isEmpty()) {
            return null;
        }
        
        String[] classNames = new String[availableClasses.size()];
        for (int i = 0; i < availableClasses.size(); i++) {
            CharacterClass charClass = availableClasses.get(i);
            classNames[i] = String.format("%s - %s", charClass.getName(), charClass.getDescription());
        }
        
        System.out.println("\n=== WYBÓR KLASY ===");
        int selected = Menu.displayMenu(classNames);
        if (selected >= 0 && selected < availableClasses.size()) {
            CharacterClass charClass = availableClasses.get(selected);
            System.out.println("Wybrano klasę: " + charClass.getName());
            printAbilities(charClass);
            return charClass;
        }
        return null;
    }
    
    /**
     * Prints the abilities of a character component.
     * @param component the race or class
     */
    private void printAbilities(CharacterComponent component) {
        if (component.getAbilities() != null && !component.getAbilities().isEmpty()) {
            System.out.println("Zdolności:");
            for (BaseAbility ability : component.getAbilities()) {
                System.out.printf("  • %s: %s%n", ability.getName(), ability.getDescription());
            }
        }
    }
    
    /**
     * Gets the current player.
     * @return current player
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Advances to the next player's turn.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        turn++;
    }
    
    /**
     * Executes a single turn for the current player.
     */
    public void executeTurn() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }
        
        System.out.println("\n=== TURA " + turn + " ===");
        System.out.println("Gracz: " + currentPlayer.getName());
        System.out.println(currentPlayer.getPlayerDescription());
        
        // Phase 1: Kick Open the Door
        System.out.println("\n--- FAZA 1: Wyważ Drzwi ---");
        MonsterCard monster = drawDoorCard();
        
        if (monster != null) {
            // Combat!
            System.out.println("Napotkano potwora!");
            System.out.println(monster.toString());
            
            Combat combat = new Combat(currentPlayer, monster);
            System.out.println(combat.getCombatDescription());
            
            // Player chooses action
            String[] combatOptions = {"Walcz!", "Uciekaj!", "Użyj karty"};
            int choice = Menu.displayMenu(combatOptions);
            
            switch (choice) {
                case 0:
                    resolveCombat(combat);
                    break;
                case 1:
                    attemptEscape(combat);
                    break;
                case 2:
                    useCardInCombat(combat);
                    break;
            }
        }
        
        // Phase 2: Look for Trouble / Loot the Room
        System.out.println("\n--- FAZA 2: Szukaj Kłopotów / Plądruj Pokój ---");
        
        // Phase 3: Charity
        System.out.println("\n--- FAZA 3: Dobroczynność ---");
        checkHandLimit(currentPlayer);
        
        // Check for victory
        if (currentPlayer.hasWon()) {
            gameOver = true;
            System.out.println("\n=== ZWYCIĘSTWO! ===");
            System.out.println(currentPlayer.getName() + " osiągnął poziom 10 i wygrał grę!");
        }
        
        nextTurn();
    }
    
    /**
     * Draws a door card (monster).
     * @return the drawn monster card
     */
    private MonsterCard drawDoorCard() {
        if (doorDeck == null || doorDeck.isEmpty()) {
            System.out.println("Talia drzwi jest pusta!");
            return null;
        }
        int idx = random.nextInt(doorDeck.size());
        return doorDeck.get(idx);
    }
    
    /**
     * Resolves combat between player and monster.
     * @param combat the combat instance
     */
    private void resolveCombat(Combat combat) {
        Combat.CombatResult result = combat.resolveCombat();
        
        if (result == Combat.CombatResult.VICTORY) {
            System.out.println("\n*** ZWYCIĘSTWO! ***");
            System.out.println("Pokonałeś " + combat.getMonster().getName() + "!");
            System.out.println("Zdobyte skarby: " + combat.getMonster().getTreasure());
            System.out.println("Zdobyte poziomy: " + combat.getMonster().getLevelsGained());
        } else {
            System.out.println("\n*** PORAŻKA! ***");
            System.out.println("Przegrałeś walkę z " + combat.getMonster().getName() + "!");
            System.out.println("Zły efekt: " + combat.getMonster().getNastyEffect());
            
            // Apply nasty effect
            int lowestLevel = getLowestPlayerLevel();
            combat.applyDefeat(lowestLevel);
        }
    }
    
    /**
     * Attempts to escape from combat.
     * @param combat the combat instance
     */
    private void attemptEscape(Combat combat) {
        int roll = combat.rollDice();
        System.out.println("Rzut kostką na ucieczkę: " + roll);
        
        boolean escaped = combat.attemptEscape();
        
        if (escaped) {
            System.out.println("*** UDANA UCIECZKA! ***");
        } else {
            System.out.println("*** NIEUDANA UCIECZKA! ***");
            
            // Check for second escape (Halfling)
            if (combat.getPlayer().getRace() != null && 
                combat.getPlayer().getRace().hasSecondEscapeAttempt()) {
                
                String[] options = {"Spróbuj ponownie (odrzuć kartę)", "Zaakceptuj porażkę"};
                int choice = Menu.displayMenu(options);
                
                if (choice == 0 && !combat.getPlayer().getInventory().isEmpty()) {
                    combat.getPlayer().discardCard(combat.getPlayer().getInventory().get(0));
                    roll = combat.rollDice();
                    System.out.println("Drugi rzut na ucieczkę: " + roll);
                    escaped = combat.attemptSecondEscape();
                    
                    if (escaped) {
                        System.out.println("*** UDANA UCIECZKA! ***");
                        return;
                    }
                }
            }
            
            // Apply nasty effect
            System.out.println("Zły efekt: " + combat.getMonster().getNastyEffect());
            int lowestLevel = getLowestPlayerLevel();
            combat.applyDefeat(lowestLevel);
        }
    }
    
    /**
     * Uses a card during combat.
     * @param combat the combat instance
     */
    private void useCardInCombat(Combat combat) {
        Player player = combat.getPlayer();
        if (player.getInventory().isEmpty()) {
            System.out.println("Nie masz kart do użycia!");
            return;
        }
        
        String[] cardNames = new String[player.getInventory().size() + 1];
        for (int i = 0; i < player.getInventory().size(); i++) {
            Card card = player.getInventory().get(i);
            cardNames[i] = card.getName() + " - " + card.getDescription();
        }
        cardNames[player.getInventory().size()] = "Anuluj";
        
        int choice = Menu.displayMenu(cardNames);
        
        if (choice < player.getInventory().size()) {
            Card card = player.getInventory().get(choice);
            combat.useCard(card);
            player.discardCard(card);
            System.out.println("Użyto karty: " + card.getName());
            
            // Recalculate and show new combat strength
            System.out.println("Nowa siła gracza: " + combat.getPlayerCombatStrength());
        }
    }
    
    /**
     * Checks if player has too many cards and must discard.
     * @param player the player to check
     */
    private void checkHandLimit(Player player) {
        while (player.getInventory().size() > player.getMaxCardsInHand()) {
            System.out.println("Masz za dużo kart! Musisz odrzucić jedną.");
            String[] cardNames = new String[player.getInventory().size()];
            for (int i = 0; i < player.getInventory().size(); i++) {
                cardNames[i] = player.getInventory().get(i).getName();
            }
            
            int choice = Menu.displayMenu(cardNames);
            Card discarded = player.getInventory().get(choice);
            player.discardCard(discarded);
            discardPile.add(discarded);
            System.out.println("Odrzucono: " + discarded.getName());
        }
    }
    
    /**
     * Gets the lowest player level (for certain nasty effects).
     * @return lowest level among all players
     */
    public int getLowestPlayerLevel() {
        int lowest = Integer.MAX_VALUE;
        for (Player player : players) {
            if (player.getLevel() < lowest) {
                lowest = player.getLevel();
            }
        }
        return lowest > 0 ? lowest : 1;
    }
    
    /**
     * Checks if the game is over.
     * @return true if game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Gets all players in the game.
     * @return list of players
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Gets the current turn number.
     * @return turn number
     */
    public int getTurn() {
        return turn;
    }
    
    /**
     * Gets available races.
     * @return list of races
     */
    public List<Race> getAvailableRaces() {
        return availableRaces;
    }
    
    /**
     * Gets available classes.
     * @return list of classes
     */
    public List<CharacterClass> getAvailableClasses() {
        return availableClasses;
    }
}
