package chaos_room.view;
import java.util.Scanner;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import chaos_room.model.Player;

public class Menu {
    public static int displayMenu(Object[] options, String title) {
        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();
            NonBlockingReader reader = terminal.reader()) {
            ConsoleView view = new ConsoleView();
            
            int selected = 0;
            while (true) {
                terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
                // Use ConsoleView methods for consistent formatting
                view.displayMenuTitle(title);
                
                for (int i = 0; i < options.length; i++) {
                    view.displayMenuOption(options[i].toString(), i == selected);
                }
                view.displayMenuBottom();
                
                terminal.flush();

                int c = reader.read();
                if (c == 27) {
                    reader.read();
                    int arrow = reader.read();
                    if (arrow == 'A') selected = (selected - 1 + options.length) % options.length;
                    if (arrow == 'B') selected = (selected + 1) % options.length; 
                } else if (c == 10 || c == 13) {
                    return selected;
                }
            }

        } catch (java.io.IOException e) {
            new ConsoleView().displayMessage("Error displaying menu.");
            return -1;
        }
    }
}