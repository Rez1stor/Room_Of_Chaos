package chaos_room.model;

/**
 * Interface for player interaction capabilities.
 * Demonstrates OOP principle: interface segregation.
 */
public interface PlayerInteraction {
    
    /**
     * Help another player in combat.
     * @param targetPlayer The player being helped
     * @param helpStrength The strength contribution
     */
    void helpPlayer(Player targetPlayer, int helpStrength);
    
    /**
     * Trade cards with another player.
     * @param otherPlayer Player to trade with
     * @param cardToGive Card to give
     * @param cardToReceive Card to receive
     * @return true if trade was successful
     */
    boolean tradeCard(Player otherPlayer, Card cardToGive, Card cardToReceive);
    
    /**
     * Request help from other players.
     * @return true if help request was made
     */
    boolean requestHelp();
}
