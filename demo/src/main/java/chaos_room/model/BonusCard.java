package chaos_room.model;

public class BonusCard extends Card {
    private int treasureBonus;
    private int battleBonus;
    private String inGame;

    public BonusCard() {
        super();
    }

    public BonusCard(String id, String name, CardType type, String description, int treasureBonus, int battleBonus, String inGame) {
        super(id, name, type, description);
        this.treasureBonus = treasureBonus;
        this.battleBonus = battleBonus;
        this.inGame = inGame;
    }

    public int getTreasureBonus() {
        return treasureBonus;
    }

    public void setTreasureBonus(int treasureBonus) {
        this.treasureBonus = treasureBonus;
    }

    public int getBattleBonus() {
        return battleBonus;
    }

    public void setBattleBonus(int battleBonus) {
        this.battleBonus = battleBonus;
    }

    public String getInGame() {
        return inGame;
    }

    public void setInGame(String inGame) {
        this.inGame = inGame;
    }
}