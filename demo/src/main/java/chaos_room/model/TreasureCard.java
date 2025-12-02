package chaos_room.model;

public class TreasureCard extends Card {
    private int goldValue;
    private String specialAbility;

    public TreasureCard() {
        super();
    }

    public TreasureCard(String id, String name, CardType type, String description, int goldValue, String specialAbility) {
        super(id, name, type, description);
        this.goldValue = goldValue;
        this.specialAbility = specialAbility;
    }

    public int getGoldValue() {
        return goldValue;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }

    public String getSpecialAbility() {
        return specialAbility;
    }

    public void setSpecialAbility(String specialAbility) {
        this.specialAbility = specialAbility;
    }
    
}
