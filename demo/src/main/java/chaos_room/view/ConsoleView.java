package chaos_room.view;
import chaos_room.model.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import java.util.Scanner;

public class ConsoleView {

    public void displayWelcomeMessage() throws InterruptedException {
        String text = "Witaj w „LootQuest: Komnata Chaosu”!\n" +
                        // "Twoim celem: zdobyć 10 poziomów doświadczenia.\n" +
                        // "Wybierz klasę, przygotuj ekwipunek i ruszaj do boju!\n" +
                        // "Otwórz drzwi lochu, walcz z potworami, zdobywaj skarby i uważaj na klątwy.\n" +
                        // "Humor i spryt mogą być ważniejsze niż siła!\n" +
                        "Powodzenia, bohaterze!";
        printWithDelay(text, 50);
        System.out.println("------------------------------");
    }

    public void printWithDelay(String text, int delayMillis) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(delayMillis);
        }
        System.out.println();
        registration();
    }
    
    public Player registration() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj nazwę swojego bohatera: ");
        String name = scanner.nextLine();
        int selected = Menu.displayMenu(new String[]{"Mężczyzna", "Kobieta"});
        String gender = selected == 0 ? "Male" : "Female";
        return new Player(name, gender);
    }
}

