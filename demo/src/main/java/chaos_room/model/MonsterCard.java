package chaos_room.model;

import java.util.Map;

public class MonsterCard {
    public String id;
    public String type;
    public String name;
    public int level;
    public int treasure;
    public int levelsGained;
    public String description;
    public String nastyEffect;
    public Object levelsLost; 
    public Map<String, Object> tags;
    public String inGame;

    public MonsterCard(String name, int level, String description, String nastyEffect) {
        this.name = name;
        this.level = level;
        this.description = description;
        this.nastyEffect = nastyEffect;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public String getNastyEffect() {
        return nastyEffect;
    }


}
