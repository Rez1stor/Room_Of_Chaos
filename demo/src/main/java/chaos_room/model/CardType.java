package chaos_room.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CardType {
    @JsonProperty("monster") MONSTER,
    @JsonProperty("class") CLASS,
    @JsonProperty("race") RACE,
    @JsonProperty("treasure") TREASURE,
    @JsonProperty("equipment") EQUIPMENT,
    @JsonProperty("bonus") BONUS,
    UNKNOWN
}
