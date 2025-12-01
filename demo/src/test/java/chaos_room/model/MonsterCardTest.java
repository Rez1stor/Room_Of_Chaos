package chaos_room.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the MonsterCard class including tags and combat modifiers.
 */
public class MonsterCardTest {
    
    private MonsterCard monster;
    
    @Before
    public void setUp() {
        monster = new MonsterCard();
        monster.setId("MON-001");
        monster.setName("Test Monster");
        monster.setLevel(5);
        monster.setTreasure(2);
        monster.setLevelsGained(1);
        monster.setDescription("A test monster for unit testing");
        monster.setNastyEffect("You lose a level");
        monster.setLevelsLost(1);
    }
    
    @Test
    public void testBasicMonsterProperties() {
        assertEquals("MON-001", monster.getId());
        assertEquals("Test Monster", monster.getName());
        assertEquals(5, monster.getLevel());
        assertEquals(2, monster.getTreasure());
        assertEquals(1, monster.getLevelsGained());
    }
    
    @Test
    public void testUndeadTag() {
        assertFalse(monster.isUndead());
        
        Map<String, Object> tags = new HashMap<>();
        tags.put("undead", true);
        monster.setTags(tags);
        
        assertTrue(monster.isUndead());
    }
    
    @Test
    public void testNoEscapeTag() {
        assertFalse(monster.preventsEscape());
        
        Map<String, Object> tags = new HashMap<>();
        tags.put("noEscape", true);
        monster.setTags(tags);
        
        assertTrue(monster.preventsEscape());
    }
    
    @Test
    public void testNoHelpTag() {
        assertFalse(monster.preventsHelp());
        
        Map<String, Object> tags = new HashMap<>();
        tags.put("noHelp", true);
        monster.setTags(tags);
        
        assertTrue(monster.preventsHelp());
    }
    
    @Test
    public void testMagicResistTag() {
        assertFalse(monster.isMagicResistant());
        
        Map<String, Object> tags = new HashMap<>();
        tags.put("magicResist", true);
        monster.setTags(tags);
        
        assertTrue(monster.isMagicResistant());
    }
    
    @Test
    public void testEscapeModifier() {
        assertEquals(0, monster.getEscapeModifier());
        
        // Positive modifier (easier to escape)
        Map<String, Object> tags = new HashMap<>();
        tags.put("escapeBonus", 2);
        monster.setTags(tags);
        
        assertEquals(2, monster.getEscapeModifier());
        
        // Negative modifier (harder to escape)
        tags.put("escapeBonus", -3);
        monster.setTags(tags);
        
        assertEquals(-3, monster.getEscapeModifier());
    }
    
    @Test
    public void testBonusVsRace() {
        assertEquals(0, monster.getBonusVsRace("Dwarf"));
        
        Map<String, Object> tags = new HashMap<>();
        tags.put("vsDwarf", 5);
        monster.setTags(tags);
        
        assertEquals(5, monster.getBonusVsRace("Dwarf"));
        assertEquals(5, monster.getBonusVsRace("dwarf")); // Case insensitive
        assertEquals(0, monster.getBonusVsRace("Elf")); // Different race
    }
    
    @Test
    public void testBonusVsClass() {
        assertEquals(0, monster.getBonusVsClass("Cleric"));
        
        Map<String, Object> tags = new HashMap<>();
        tags.put("vsCleric", 3);
        monster.setTags(tags);
        
        assertEquals(3, monster.getBonusVsClass("Cleric"));
        assertEquals(0, monster.getBonusVsClass("Warrior")); // Different class
    }
    
    @Test
    public void testMultipleTags() {
        Map<String, Object> tags = new HashMap<>();
        tags.put("undead", true);
        tags.put("noEscape", true);
        tags.put("vsDwarf", 5);
        tags.put("escapeBonus", -2);
        monster.setTags(tags);
        
        assertTrue(monster.isUndead());
        assertTrue(monster.preventsEscape());
        assertEquals(5, monster.getBonusVsRace("Dwarf"));
        assertEquals(-2, monster.getEscapeModifier());
    }
    
    @Test
    public void testCalculateLevelsLost() {
        // Fixed levels lost
        monster.setLevelsLost(2);
        assertEquals(2, monster.calculateLevelsLost(1, 5));
        
        // Zero levels lost
        monster.setLevelsLost(0);
        assertEquals(0, monster.calculateLevelsLost(1, 5));
    }
    
    @Test
    public void testDynamicLevelsLost() {
        // Magic resistant monster - drops to lowest level
        Map<String, Object> tags = new HashMap<>();
        tags.put("magicResist", true);
        monster.setTags(tags);
        monster.setLevelsLost("dynamic");
        
        // Player level 8, lowest is 3, should lose 5 levels
        assertEquals(5, monster.calculateLevelsLost(3, 8));
    }
    
    @Test
    public void testMonsterToString() {
        String str = monster.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("Test Monster"));
        assertTrue(str.contains("Poziom 5"));
        assertTrue(str.contains("test monster for unit testing"));
    }
    
    @Test
    public void testNullTags() {
        monster.setTags(null);
        
        assertFalse(monster.isUndead());
        assertFalse(monster.preventsEscape());
        assertFalse(monster.preventsHelp());
        assertFalse(monster.isMagicResistant());
        assertEquals(0, monster.getEscapeModifier());
        assertEquals(0, monster.getBonusVsRace("Dwarf"));
        assertEquals(0, monster.getBonusVsClass("Cleric"));
    }
    
    @Test
    public void testEmptyTags() {
        monster.setTags(new HashMap<>());
        
        assertFalse(monster.isUndead());
        assertFalse(monster.preventsEscape());
        assertFalse(monster.preventsHelp());
        assertFalse(monster.isMagicResistant());
        assertEquals(0, monster.getEscapeModifier());
    }
}
