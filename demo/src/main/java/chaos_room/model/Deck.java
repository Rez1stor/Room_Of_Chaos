package chaos_room.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Deck {
    public List<Card> cards;
    public List<Card> treasureCards;
    
    public Deck() {
        cards = new ArrayList<>();
        treasureCards = new ArrayList<>();

        List<MonsterCard> monsterCards = getCardsFromJson("MonsterCards.json", MonsterCard.class);    
        if (monsterCards != null) cards.addAll(monsterCards);
        
        List<CharacterCard> characterCards = getCardsFromJson("CharacterCards.json", CharacterCard.class);
        if (characterCards != null) cards.addAll(characterCards);
        
        List<OneTimeBonusCard> bonusCards = getCardsFromJson("OneTimeBonusCards.json", OneTimeBonusCard.class);
        if (bonusCards != null) cards.addAll(bonusCards);

        List<EquipmentCard> equipmentCards = getCardsFromJson("EquipmentCards.json", EquipmentCard.class);
        if (equipmentCards != null) treasureCards.addAll(equipmentCards);

        List<TreasureCard> treasureCardList = getCardsFromJson("TreasureCards.json", TreasureCard.class);
        if (treasureCardList != null) treasureCards.addAll(treasureCardList);
    }

    public static <T> List<T> getCardsFromJson(String jsonFilePath, Class<T> contentClass) {
        try{
            FileReader reader = new FileReader(Card.class.getClassLoader().getResource(jsonFilePath).getPath());
            ObjectMapper json = new ObjectMapper();
            CollectionType listType = json.getTypeFactory().constructCollectionType(List.class, contentClass);
            List<T> cards = json.readValue(reader, listType); 
            reader.close();
            return cards;
        } catch (IOException e) {
            // e.printStackTrace();
            return new ArrayList<>();
        }
    }



}
