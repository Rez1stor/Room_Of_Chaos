package chaos_room.view;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * Handles console/terminal interactions using JLine.
 */
public class ConsoleView {
    private Terminal terminal;
    private LineReader lineReader;

    public ConsoleView() {
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
        } catch (IOException e) {
            System.err.println("Failed to initialize terminal: " + e.getMessage());
            terminal = null;
            lineReader = null;
        }
    }

    /**
     * Display a message to the console.
     */
    public void display(String message) {
        if (terminal != null) {
            terminal.writer().println(message);
            terminal.writer().flush();
        } else {
            System.out.println(message);
        }
    }

    /**
     * Read user input from the console.
     */
    public String readInput(String prompt) {
        if (lineReader != null) {
            return lineReader.readLine(prompt);
        } else {
            System.out.print(prompt);
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            return scanner.nextLine();
        }
    }

    /**
     * Close the terminal resources.
     */
    public void close() {
        if (terminal != null) {
            try {
                terminal.close();
            } catch (IOException e) {
                System.err.println("Error closing terminal: " + e.getMessage());
            }
        }
    }

    /**
     * Check if the terminal is available.
     */
    public boolean isTerminalAvailable() {
        return terminal != null;
    }
}
