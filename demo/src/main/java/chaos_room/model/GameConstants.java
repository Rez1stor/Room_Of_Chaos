package chaos_room.model;

/**
 * Constants used throughout the game mechanics.
 * Centralizes magic strings and values for maintainability.
 */
public final class GameConstants {
    
    // Private constructor to prevent instantiation
    private GameConstants() {}
    
    // Combat phase types
    public static final String PHASE_COMBAT = "combat";
    public static final String PHASE_ESCAPE = "escape";
    
    // Ability effect keys
    public static final String EFFECT_COMBAT_BONUS_PER_DISCARD = "combatBonusPerDiscard";
    public static final String EFFECT_ESCAPE_BONUS_PER_DISCARD = "escapeBonusPerDiscard";
    public static final String EFFECT_ESCAPE_BONUS = "escapeBonus";
    public static final String EFFECT_WIN_ON_TIE = "winOnTie";
    public static final String EFFECT_SECOND_ESCAPE_ATTEMPT = "secondEscapeAttempt";
    public static final String EFFECT_BONUS_VS_UNDEAD = "bonusPerDiscardVsUndead";
    public static final String EFFECT_LEVEL_FOR_HELPING = "levelForHelping";
    public static final String EFFECT_HAND_LIMIT = "handLimit";
    
    // Condition keys
    public static final String CONDITION_MAX_DISCARDS = "maxDiscards";
    public static final String CONDITION_USABLE_IN = "usableIn";
    
    // Monster tag keys
    public static final String TAG_UNDEAD = "undead";
    public static final String TAG_NO_ESCAPE = "noEscape";
    public static final String TAG_NO_HELP = "noHelp";
    public static final String TAG_MAGIC_RESIST = "magicResist";
    public static final String TAG_VS_PREFIX = "vs";
    
    // Special values
    public static final String LEVELS_LOST_DYNAMIC = "dynamic";
    public static final int MAX_DICE_ROLL = 6;
    public static final int ESCAPE_SUCCESS_THRESHOLD = 5;
    public static final int MAX_LEVEL = 10;
    public static final int MIN_LEVEL = 1;
    public static final int DEFAULT_HAND_LIMIT = 5;
    
    // Gender constants
    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    public static final String GENDER_MALE_DISPLAY = "Mężczyzna";
    public static final String GENDER_FEMALE_DISPLAY = "Kobieta";
    
    /**
     * Gets the display name for a gender.
     * @param gender internal gender code
     * @return display name
     */
    public static String getGenderDisplayName(String gender) {
        if (GENDER_MALE.equals(gender)) {
            return GENDER_MALE_DISPLAY;
        } else if (GENDER_FEMALE.equals(gender)) {
            return GENDER_FEMALE_DISPLAY;
        }
        return gender;
    }
}
