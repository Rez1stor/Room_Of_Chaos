package chaos_room.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the Ability system including race and class abilities.
 */
public class AbilityTest {
    
    @Test
    public void testBaseAbilityCreation() {
        Map<String, Object> effects = new HashMap<>();
        effects.put("combatBonusPerDiscard", 1);
        
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("maxDiscards", 3);
        conditions.put("usableIn", "combat");
        
        BaseAbility ability = new BaseAbility(
            "warrior-fury",
            "Bujstwo",
            "Możesz odrzucić do trzech kart w walce. Każda daje +1 do siły.",
            effects,
            conditions
        );
        
        assertEquals("warrior-fury", ability.getId());
        assertEquals("Bujstwo", ability.getName());
        assertTrue(ability.isUsableInCombat());
        assertFalse(ability.isUsableInEscape());
    }
    
    @Test
    public void testWinOnTieAbility() {
        Map<String, Object> effects = new HashMap<>();
        effects.put("winOnTie", true);
        
        BaseAbility ability = new BaseAbility(
            "warrior-tie-win",
            "Zwycięstwo przy remisie",
            "Wygrywasz walkę przy remisie siły.",
            effects,
            new HashMap<>()
        );
        
        assertTrue(ability.winsOnTie());
    }
    
    @Test
    public void testEscapeAbility() {
        Map<String, Object> effects = new HashMap<>();
        effects.put("escapeBonus", 1);
        
        BaseAbility ability = new BaseAbility(
            "elf-escape",
            "+1 do ucieczki",
            "Masz +1 na smywkę.",
            effects,
            new HashMap<>()
        );
        
        Player player = new Player("Test", "Male");
        MonsterCard monster = new MonsterCard();
        monster.setName("Test Monster");
        monster.setLevel(1);
        
        CombatContext context = new CombatContext(player, monster);
        
        assertEquals(1, ability.getEscapeBonus(context));
    }
    
    @Test
    public void testSecondEscapeAbility() {
        Map<String, Object> effects = new HashMap<>();
        effects.put("secondEscapeAttempt", true);
        
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("requiresFirstFail", true);
        
        BaseAbility ability = new BaseAbility(
            "halfling-second-escape",
            "Druga próba",
            "Po nieudanej ucieczce możesz odrzucić kartę, aby spróbować ponownie.",
            effects,
            conditions
        );
        
        assertTrue(ability.allowsSecondEscape());
    }
    
    @Test
    public void testCombatBonusPerDiscard() {
        Map<String, Object> effects = new HashMap<>();
        effects.put("combatBonusPerDiscard", 1);
        
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("maxDiscards", 3);
        conditions.put("usableIn", "combat");
        
        BaseAbility ability = new BaseAbility(
            "warrior-fury",
            "Bujstwo",
            "Możesz odrzucić do trzech kart w walce. Każda daje +1 do siły.",
            effects,
            conditions
        );
        
        Player player = new Player("Test", "Male");
        MonsterCard monster = new MonsterCard();
        monster.setName("Test Monster");
        monster.setLevel(1);
        
        CombatContext context = new CombatContext(player, monster);
        
        // No cards discarded
        assertEquals(0, ability.getCombatBonus(context));
        
        // 2 cards discarded
        context.setCardsDiscarded(2);
        assertEquals(2, ability.getCombatBonus(context));
        
        // Max 3 cards even if more discarded
        context.setCardsDiscarded(5);
        assertEquals(3, ability.getCombatBonus(context));
    }
    
    @Test
    public void testClericBonusVsUndead() {
        Map<String, Object> effects = new HashMap<>();
        effects.put("bonusPerDiscardVsUndead", 3);
        
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("maxDiscards", 3);
        conditions.put("vsTag", "undead");
        
        BaseAbility ability = new BaseAbility(
            "cleric-banish",
            "Wygnanie",
            "Odrzuć do trzech kart w walce przeciw nieumarłym. Każda daje +3.",
            effects,
            conditions
        );
        
        Player player = new Player("Test", "Male");
        
        // Test against undead monster
        MonsterCard undeadMonster = new MonsterCard();
        undeadMonster.setName("Undead");
        undeadMonster.setLevel(5);
        Map<String, Object> tags = new HashMap<>();
        tags.put("undead", true);
        undeadMonster.setTags(tags);
        
        CombatContext context = new CombatContext(player, undeadMonster);
        context.setCardsDiscarded(2);
        
        // 2 cards * 3 bonus = 6
        assertEquals(6, ability.getCombatBonus(context));
        
        // Test against non-undead monster
        MonsterCard normalMonster = new MonsterCard();
        normalMonster.setName("Normal");
        normalMonster.setLevel(5);
        normalMonster.setTags(new HashMap<>());
        
        CombatContext context2 = new CombatContext(player, normalMonster);
        context2.setCardsDiscarded(2);
        
        // No bonus against non-undead
        assertEquals(0, ability.getCombatBonus(context2));
    }
    
    @Test
    public void testCharacterComponentTotalBonus() {
        CharacterClass warrior = new CharacterClass("class-warrior", "Wojownik", "Strong fighter");
        
        // Add combat bonus ability
        Map<String, Object> effects1 = new HashMap<>();
        effects1.put("combatBonusPerDiscard", 1);
        Map<String, Object> conditions1 = new HashMap<>();
        conditions1.put("maxDiscards", 3);
        BaseAbility fury = new BaseAbility("fury", "Fury", "Combat bonus", effects1, conditions1);
        warrior.addAbility(fury);
        
        // Add win on tie ability
        Map<String, Object> effects2 = new HashMap<>();
        effects2.put("winOnTie", true);
        BaseAbility winOnTie = new BaseAbility("winOnTie", "Win on Tie", "Win ties", effects2, new HashMap<>());
        warrior.addAbility(winOnTie);
        
        Player player = new Player("Test", "Male");
        MonsterCard monster = new MonsterCard();
        monster.setName("Test");
        monster.setLevel(1);
        
        CombatContext context = new CombatContext(player, monster);
        context.setCardsDiscarded(2);
        
        assertEquals(2, warrior.getTotalCombatBonus(context));
        assertTrue(warrior.hasWinOnTie());
    }
    
    @Test
    public void testRaceHandLimitModifier() {
        Race dwarf = new Race("race-dwarf", "Krasnolud", "Strong dwarf");
        
        Map<String, Object> effects = new HashMap<>();
        effects.put("handLimit", 6);
        
        BaseAbility handLimit = new BaseAbility(
            "dwarf-handlimit",
            "6 kart na koniec tury",
            "Możesz trzymać 6 kart na ręce na koniec tury.",
            effects,
            new HashMap<>()
        );
        dwarf.addAbility(handLimit);
        
        assertEquals(6, dwarf.getHandLimitModifier());
    }
    
    @Test
    public void testRaceSecondEscapeCheck() {
        Race halfling = new Race("race-halfling", "Niziołek", "Small but clever");
        
        Map<String, Object> effects = new HashMap<>();
        effects.put("secondEscapeAttempt", true);
        
        BaseAbility secondEscape = new BaseAbility(
            "halfling-second-escape",
            "Druga próba",
            "Second escape attempt",
            effects,
            new HashMap<>()
        );
        halfling.addAbility(secondEscape);
        
        assertTrue(halfling.hasSecondEscapeAttempt());
    }
}
