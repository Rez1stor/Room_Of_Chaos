package chaos_room.model;
import chaos_room.view.Menu;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;


public class Player {
    private String name;
    private int level;
    private int strength;
    private List<Card> inventory;
    private List<Card> playerDeck;
    private String gender;
    private int maxCardsInHand = 5;
    private static Random random = new Random();

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
        level = 1;
        inventory = new ArrayList<>();   
        
        StarterPack();
    }

    public void getCard() {
        if (inventory.size() >= maxCardsInHand) {
            System.out.println("Nie możesz wziąć więcej kart, Twój ekwipunek jest pełny.");
            return;
        }else{
            inventory.add(Card.getRandomCardFromJson("DoorCards.json"));
        }
    }

    private void StarterPack(){
        for (int i = 0; i < 4; i++) getCard();
        showInventory();
    }

    public void showInventory(){
        System.out.println("Ekwipunek gracza " + name + ":");
        for (Card card : inventory) {
            System.out.printf("- %s (Typ: %s, Opis: %s)%n", card.getName(), card.getType(), card.getDescription());
        }
    }

    public void selectCard(){
        int selected = Menu.displayMenu(inventory.toArray());
        Card chosenCard = inventory.get(selected);
        System.out.printf("Wybrałeś kartę: %s%n \n%s", chosenCard.getName(), chosenCard.getDescription());
        int options = Menu.displayMenu(new String[]{"Użyj karty", "Odrzuć kartę", "Anuluj"});
        switch (options) {
            case 0:
                useCard(chosenCard);
                break;
            case 1:
                System.out.println("Odrzucasz kartę: " + chosenCard.getName());
                inventory.remove(chosenCard);
                break;
            case 2:
                System.out.println("Anulowano wybór karty.");
                break;
            default:
                System.out.println("Nieprawidłowa opcja.");
                break;
        }
    }

    public void useCard(Card card){
        System.out.println("Używasz karty: " + card.getName());
        inventory.remove(card);
        playerDeck.add(card);
    }

    public String getName() {
        return name;
    }
    
    public int getLevel() {
        return level;
    }

    public int getStrength() {
        return strength;
    }

    public String getGender() {
        return gender;         
    }

    public List<Card> getInventory() {
        return inventory;
    }
}