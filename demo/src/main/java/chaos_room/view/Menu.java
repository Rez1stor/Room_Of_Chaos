package chaos_room.view;
import java.util.Scanner;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import chaos_room.model.Player;

public class Menu {
    public static int displayMenu(Object[] options) {
        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();
            NonBlockingReader reader = terminal.reader()) {
            
            int selected = 0;
            while (true) {
                terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
                terminal.writer().println("Wybierz opcję (strzałki ↑↓, Enter):");

                for (int i = 0; i < options.length; i++) {
                    if (i == selected) {
                        terminal.writer().println("> " + options[i]);
                    } else {
                        terminal.writer().println("  " + options[i]);
                    }
                }
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
            System.out.println("Błąd wejścia/wyjścia podczas wyświetlania menu.");
            return -1;
        }
    }
}