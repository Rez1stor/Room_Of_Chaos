package chaos_room.model;

/**
 * Utility class for common string operations.
 */
public final class StringUtils {
    
    // Private constructor to prevent instantiation
    private StringUtils() {}
    
    /**
     * Capitalizes the first letter of a string.
     * @param str the string to capitalize
     * @return the capitalized string
     */
    public static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
