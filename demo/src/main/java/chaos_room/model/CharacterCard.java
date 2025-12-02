package chaos_room.model;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class CharacterCard extends Card {
    private List<Ability> abilities;

    public CharacterCard() {
    }

    public CharacterCard(String id, String name, CardType type, String description, List<Ability> abilities) {
        super(id, name, type, description);
        this.abilities = abilities;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public CharacterCard setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ability {
        private String id;
        private String name;
        private String description;
        private Map<String, Object> effects;    
        private Conditions conditions;          

        public Ability() {
        }

        public Ability(String id, String name, String description, Map<String, Object> effects, Conditions conditions) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.effects = effects;
            this.conditions = conditions;
        }

        public String getId() {
            return id;
        }

        public Ability setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Ability setName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Ability setDescription(String description) {
            this.description = description;
            return this;
        }

        public Map<String, Object> getEffects() {
            return effects;
        }

        public Ability setEffects(Map<String, Object> effects) {
            this.effects = effects;
            return this;
        }

        public Conditions getConditions() {
            return conditions;
        }

        public Ability setConditions(Conditions conditions) {
            this.conditions = conditions;
            return this;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Conditions {
        private Boolean oncePerTarget;
        private Boolean requiresDice;

        public Conditions() {
        }

        public Conditions(Boolean oncePerTarget, Boolean requiresDice) {
            this.oncePerTarget = oncePerTarget;
            this.requiresDice = requiresDice;
        }

        public Boolean getOncePerTarget() {
            return oncePerTarget;
        }

        public Conditions setOncePerTarget(Boolean oncePerTarget) {
            this.oncePerTarget = oncePerTarget;
            return this;
        }

        public Boolean getRequiresDice() {
            return requiresDice;
        }

        public Conditions setRequiresDice(Boolean requiresDice) {
            this.requiresDice = requiresDice;
            return this;
        }
    }

    @Override
    public boolean use(Player player, chaos_room.view.GameView view) {
        Card existing = null;
        for (Card c : player.getPlayerDeck()) {
            if (c.getType() == this.getType()) {
                existing = c;
                break;
            }
        }

        if (existing != null) {
            int choice = view.displayMenu(new String[]{"Yes, replace it", "No, keep current"}, 
                "You already have a " + this.getType() + ": " + existing.getName() + ". Replace it with " + this.getName() + "?");
            if (choice == 0) {
                player.getPlayerDeck().remove(existing);
                view.displayMessage("Discarded " + existing.getName());
                player.addCardToPlayerDeck(this);
                player.getInventory().remove(this);
                view.displayMessage(String.format("Card %s added to player's deck.", this.getName()));
            } else {
                view.displayMessage("Cancelled.");
            }
        } else {
            player.addCardToPlayerDeck(this);
            player.getInventory().remove(this);
            view.displayMessage(String.format("Card %s added to player's deck.", this.getName()));
        }
        return false;
    }
}
