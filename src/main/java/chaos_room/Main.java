package chaos_room;

import chaos_room.model.Card;
import chaos_room.view.ConsoleView;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Main entry point for the Room of Chaos application.
 */
public class Main {
    public static void main(String[] args) {
        ConsoleView consoleView = new ConsoleView();
        
        try {
            consoleView.display("Welcome to the Room of Chaos!");
            consoleView.display("================================");
            
            // Create a sample card
            Card card = new Card(1, "Chaos Card", "A card of pure chaos", 100);
            consoleView.display("Created card: " + card);
            
            // Demonstrate JSON serialization
            try {
                String json = card.toJson();
                consoleView.display("Card as JSON: " + json);
                
                // Demonstrate JSON deserialization
                Card parsedCard = Card.fromJson(json);
                consoleView.display("Parsed card: " + parsedCard);
            } catch (JsonProcessingException e) {
                consoleView.display("Error processing JSON: " + e.getMessage());
            }
            
            consoleView.display("================================");
            consoleView.display("Application started successfully!");
            
        } finally {
            consoleView.close();
        }
    }
}
