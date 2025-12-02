package chaos_room.model;

import java.util.List;

public class Game {
    private int turn;
    private Player currentPlayer;
    private List<Player> players;
    private int currentPlayerIndex = 0;

    public Game(List<Player> players) {
        turn = 1;
        this.players = players;
        currentPlayer = players.get(0);
    }

    public void nextTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        turn++;
    }

    public void checkMonsterTags(MonsterCard monster, Player player) {
        if (monster.getTags() == null) {
            return;
        } else {
            for (String key : monster.getTags().keySet()){
                switch (key){
                    case "vsWarrior":
                        if (player.getPlayerDeck().stream().anyMatch(card -> card.getId().equals("class-warrior"))) monster.setStrength(monster.getStrength() + (int) monster.getTags().get("vsWarrior"));
                        break;
                    case "vsMage":
                        if (player.getPlayerDeck().stream().anyMatch(card -> card.getId().equals("class-mage"))) monster.setStrength(monster.getStrength() + (int) monster.getTags().get("vsMage"));
                        break;
                    case "vsHalfling":
                        if (player.getPlayerDeck().stream().anyMatch(card -> card.getId().equals("race-halfling"))) monster.setStrength(monster.getStrength() + (int) monster.getTags().get("vsHalfling"));
                        break;
                    case "vsElf":
                        if (player.getPlayerDeck().stream().anyMatch(card -> card.getId().equals("race-elf"))) monster.setStrength(monster.getStrength() + (int) monster.getTags().get("vsElf"));
                        break;
                    case "vsDwarf":
                        if (player.getPlayerDeck().stream().anyMatch(card -> card.getId().equals("race-dwarf"))) monster.setStrength(monster.getStrength() + (int) monster.getTags().get("vsDwarf"));
                        break;
                    case "vsCleric":
                        if (player.getPlayerDeck().stream().anyMatch(card -> card.getId().equals("class-cleric"))) monster.setStrength(monster.getStrength() + (int) monster.getTags().get("vsCleric"));
                        break;
                    case "escapeBonus":
                        player.setEscapeChances(player.getEscapeChances() - (int) monster.getTags().get("escapeBonus"));
                        break;
                    case "noEscape":
                        player.setEscapeChances(0);
                        break;
                    default:
                        break;
                    }
                }
        }
    }

    public int getTurn() {
        return turn;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }
}