package chaos_room.view;
import chaos_room.model.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ConsoleView implements GameView {

    private static final int TERMINAL_WIDTH = 120;
    private static final String LEFT_BORDER = " |   |  ";
    private static final String RIGHT_BORDER = "  |   | ";
    private static final int INNER_WIDTH = TERMINAL_WIDTH - LEFT_BORDER.length() - RIGHT_BORDER.length();

    private String getTopBorder() {
        return "( ___ )" + repeatString("-", TERMINAL_WIDTH - 14) + "( ___ )";
    }

    private String getBottomBorder() {
        return "(_____)" + repeatString("-", TERMINAL_WIDTH - 14) + "(_____)";
    }

    private List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }

    public void displayWelcomeMessage() throws InterruptedException {
        displayLogo();
        String[] lines = {
            "Welcome to 'LootQuest: Chamber of Chaos'!",
            "Your goal: reach level 10.",
            "Choose a class, prepare gear, and rush into battle!",
            "Open dungeon doors, fight monsters, loot treasures!",
            "Humor and wit might be more important than strength!",
            "Good luck, hero!"
        };
        
        for (String line : lines) {
            int leftPad = Math.max(0, (INNER_WIDTH - line.length()) / 2);
            int rightPad = Math.max(0, INNER_WIDTH - line.length() - leftPad);
            printWithDelay(LEFT_BORDER + repeatString(" ", leftPad) + line + repeatString(" ", rightPad) + RIGHT_BORDER + "\n", 20);
        }
        displaySeparator();
    }

    public int askForNumberOfPlayers() {    
        Scanner scanner = new Scanner(System.in);
        int n = 0;
        while (true) {
            displayPrompt("Enter number of players (1-6): ");
            try {
                String input = scanner.nextLine();
                reprintInputLine("Enter number of players (1-6): ", input);
                n = Integer.parseInt(input);
                if (n >= 1 && n <= 6) return n;
                displayMessage("Please enter a number between 1 and 6.");
            } catch (NumberFormatException e) {
                displayMessage("Invalid number.");
            }
        }
    }

    public void printWithDelay(String text, int delayMillis) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(delayMillis);
        }
    }
    
    public Player registration() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        displayPrompt("Enter your hero's name: ");
        String name = scanner.nextLine();
        reprintInputLine("Enter your hero's name: ", name);
        int selected = displayMenu(new String[]{"Male", "Female"}, "Choose your hero's gender:");
        String gender = selected == 0 ? "Male" : "Female";
        return new Player(name, gender);
    }

    public void displayPrompt(String message) {
        String top = getTopBorder();
        System.out.println(top);
        System.out.print(LEFT_BORDER + message);
    }

    public void displayMenuTitle(String title) {
        String top = getTopBorder();
        
        System.out.println(top);
        
        String[] lines = title.split("\n");
        for (String line : lines) {
            // Clean up if the user manually added the border to the string
            if (line.trim().startsWith("|   |")) {
                 line = line.replace("|   |", "").trim();
            }
            
            List<String> wrappedLines = wrapText(line, INNER_WIDTH);
            for (String wrappedLine : wrappedLines) {
                int leftPad = Math.max(0, (INNER_WIDTH - wrappedLine.length()) / 2);
                int rightPad = Math.max(0, INNER_WIDTH - wrappedLine.length() - leftPad);
                System.out.println(LEFT_BORDER + repeatString(" ", leftPad) + wrappedLine + repeatString(" ", rightPad) + RIGHT_BORDER);
            }
        }
        
        System.out.println(top);
    }

    public void displayMenuOption(String option, boolean selected) {
        String prefix = selected ? "> " : "  ";
        String content = prefix + option;
        
        int padding = Math.max(0, INNER_WIDTH - content.length());
        
        System.out.println(LEFT_BORDER + content + repeatString(" ", padding) + RIGHT_BORDER);
    }

    public void displayMenuBottom() {
        System.out.println(getBottomBorder());
    }

    public void displayMessage(String message) {
        String top = getTopBorder();
        String bottom = getBottomBorder();

        System.out.println(top);
        
        String[] paragraphs = message.split("\n");
        for (String paragraph : paragraphs) {
            List<String> lines = wrapText(paragraph, INNER_WIDTH);
            for (String line : lines) {
                int leftPad = Math.max(0, (INNER_WIDTH - line.length()) / 2);
                int rightPad = Math.max(0, INNER_WIDTH - line.length() - leftPad);
                System.out.println(LEFT_BORDER + repeatString(" ", leftPad) + line + repeatString(" ", rightPad) + RIGHT_BORDER);
            }
        }
        System.out.println(bottom);
    }

    public void waitForInput() {
        displayPrompt("Press Enter to continue...");
        try {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        } catch (Exception e) {}
        reprintInputLine("Press Enter to continue...", "");
        System.out.println(getBottomBorder());
    }

    public void displayLogo() throws InterruptedException {
        String[] logoLines = {
            "                                                            ",
            "   _  __                                      _             ",
            "  | |/ /   ___    _ __ ___    _ __     __ _  | |_    __ _   ",
            "  | ' /   / _ \\  | '_ ` _ \\  | '_ \\   / _` | | __|  / _` |  ",
            "  | . \\  | (_) | | | | | | | | | | | | (_| | | |_  | (_| |  ",
            "  |_|\\_\\  \\___/  |_| |_| |_| |_| |_|  \\__,_|  \\__|  \\__,_|  ",
            "    ____   _                                                ",
            "   / ___| | |__     __ _    ___    ___   _   _              ",
            "  | |     | '_ \\   / _` |  / _ \\  / __| | | | |             ",
            "  | |___  | | | | | (_| | | (_) | \\__ \\ | |_| |             ",
            "   \\____| |_| |_|  \\__,_|  \\___/  |___/  \\__,_|             ",
            "                                                            "
        };

        System.out.println(getTopBorder());
        for (String line : logoLines) {
            int leftPad = Math.max(0, (INNER_WIDTH - line.length()) / 2);
            int rightPad = Math.max(0, INNER_WIDTH - line.length() - leftPad);
            printWithDelay(LEFT_BORDER + repeatString(" ", leftPad) + line + repeatString(" ", rightPad) + RIGHT_BORDER + "\n", 2);
        }
        System.out.println(getBottomBorder());
    }
    
    public String getCardInfo(Card card){
        String info = String.format("Card: %s\nType: %s\nDescription: %s", card.getName(), card.getType(), card.getDescription());
        
        if (card instanceof MonsterCard) {
            MonsterCard m = (MonsterCard) card;
            info += String.format("\nLevel: %d\nTreasures: %d\nBad Stuff: %s\nLevels Lost: %s", 
                m.getLevel(), m.getTreasure(), m.getNastyEffect(), m.getLevelsLost());
        } else if (card instanceof OneTimeBonusCard) {
            OneTimeBonusCard b = (OneTimeBonusCard) card;
            info += String.format("\nBattle Bonus: +%d\nTreasure Bonus: +%d", b.getBattleBonus(), b.getTreasureBonus());
        } else if (card instanceof EquipmentCard) {
            EquipmentCard e = (EquipmentCard) card;
            info += String.format("\nSlot: %s\nBonus: +%d", e.getSlot(), e.getBonus());
        }
        
        return info;
    }

    @Override
    public int displayMenu(Object[] options, String title) {
        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();
            NonBlockingReader reader = terminal.reader()) {
            
            int selected = 0;
            while (true) {
                terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
                
                displayMenuTitle(title);
                
                for (int i = 0; i < options.length; i++) {
                    displayMenuOption(options[i].toString(), i == selected);
                }
                displayMenuBottom();
                
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
            displayMessage("Error displaying menu.");
            return -1;
        }
    }

    public void displayGameOver(Player player) {
        try {
            clearScreen();
            String gameOverArt =
                " .--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--. \n" +
                "/ .. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\\n" +
                " \\ \\/\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ \\/ /\n" +
                " \\/ /`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'\\/ / \n" +
                " / /\\                                                                                                        / /\\ \n" +
                "/ /\\ \\                                                                                                      / /\\ \\\n" +
                "\\ \\/ /                                                        )                                             \\ \\/ /\n" +
                " \\/ /                         (                            ( /(                                              \\/ / \n" +
                " / /\\                         )\\ )       )     )      (    )\\())   )      (   (                              / /\\ \n" +
                "/ /\\ \\                       (()/(    ( /(    (      ))\\  ((_)\\   /((    ))\\  )(                            / /\\ \\\n" +
                "\\ \\/ /                        /(_))_  )(_))   )\\  ' /((_)   ((_) (_))\\  /((_)(()\\                           \\ \\/ /\n" +
                " \\/ /                        (_)) __|((_)_  _((_)) (_))    / _ \\ _)((_)(_))   ((_)                           \\/ / \n" +
                " / /\\                          | (_ |/ _` || '  \\()/ -_)  | (_) |\\ V / / -_) | '_|                           / /\\ \n" +
                "/ /\\ \\                          \\___|\\__,_||_|_|_| \\___|   \\___/  \\_/  \\___| |_|                            / /\\ \\\n" +
                "\\ \\/ /                                                                                                      \\ \\/ /\n" +
                " \\/ /                                                                                                        \\/ / \n" +
                " / /\\.--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--..--./ /\\ \n" +
                "/ /\\ \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\.. \\/\\ \\\n" +
                "\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `'\\ `' /\n" +
                " `--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--'`--' \n";
                StringBuilder sb = new StringBuilder();
                for (String line : gameOverArt.split("\n")) {
                    int padding = Math.max(0, (TERMINAL_WIDTH - line.length()) / 2);
                    sb.append(repeatString(" ", padding)).append(line).append("\n");
                }
                gameOverArt = sb.toString();
            printWithDelay(gameOverArt, 2);
            displayMessage("The winner is " + player.getName() + "!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reprintInputLine(String prompt, String input) {
        // Move cursor up 1 line
        System.out.print("\033[1A");
        // Clear the line
        System.out.print("\033[2K\r");

        String content = prompt + input;
        int padding = INNER_WIDTH - content.length();
        
        if (padding >= 0) {
            System.out.println(LEFT_BORDER + content + repeatString(" ", padding) + RIGHT_BORDER);
        } else {
            System.out.println(LEFT_BORDER + content + RIGHT_BORDER);
        }
    }

    public String repeatString(String str, int times) {
        if (times <= 0) return "";
        return new String(new char[times]).replace("\0", str);
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void displaySeparator() {
        System.out.println("( ___ )" + repeatString("=", TERMINAL_WIDTH - 14) + "( ___ )");
    }
}
