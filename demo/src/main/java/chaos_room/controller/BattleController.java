package chaos_room.controller;

import chaos_room.model.*;
import chaos_room.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleController {
    private Game game;
    private GameView view;

    public BattleController(Game game, GameView view) {
        this.game = game;
        this.view = view;
    }

    public void handleFight(MonsterCard monster, Player player) {
        // Reset monster strength
        monster.setStrength(monster.getLevel());
        game.checkMonsterTags(monster, player);
        
        boolean fightOver = false;
        int playerBonus = 0;
        int warriorBonusUsed = 0;

        while (!fightOver) {
            int currentStrength = Math.max(1, player.getStrength() + playerBonus);
            String status = String.format("Battle! Player Strength: %d (Lvl %d + Equip %d + Bonus %d) vs Monster Strength: %d\n |   |  Choose combat action:", 
                currentStrength, player.getLevel(), player.getStrength() - player.getLevel(), playerBonus, monster.getStrength());
            
            boolean isWarrior = player.getPlayerDeck().stream().anyMatch(c -> "class-warrior".equals(c.getId()));
            String[] options;
            // Warrior can discard cards for bonus
            if (isWarrior && warriorBonusUsed < 3) {
                options = new String[]{"Fight", "Use Bonus Card", "Warrior Rage (Discard for +3)"};
            } else {
                options = new String[]{"Fight", "Use Bonus Card"};
            }

            int choice = view.displayMenu(options, status);
            
            if (!isWarrior || warriorBonusUsed >= 3) {
                if (choice == 2) choice = 3; 
            }

            switch (choice) {
                case 0:
                    boolean win = isWarrior ? currentStrength >= monster.getStrength() : currentStrength > monster.getStrength();
                    
                    if (win) {
                        handleMonsterDefeated(monster, player);
                        fightOver = true;
                    } else {
                        view.displayMessage("You are too weak! You lost the fight!");
                        Random rand = new Random();
                        int dieFace = rand.nextInt(6) + 1; 
                        view.displayMessage("You try to run away... Rolled a " + dieFace);
                        
                        if (dieFace >= player.getEscapeChances()) {
                            handlePlayerEscaped(monster, player);
                        } else {
                            view.displayMessage("Escape failed!");
                            view.waitForInput();
                            handlePlayerDefeated(monster, player);
                        }
                        fightOver = true;
                    }
                    break;
                case 1: 
                    Card selected = promptBonusCardSelection(player);
                    if (selected != null) {
                        playerBonus += ((OneTimeBonusCard)selected).getBattleBonus();
                        player.getInventory().remove(selected);
                        view.displayMessage("Used " + selected.getName() + "! Bonus +" + ((OneTimeBonusCard)selected).getBattleBonus());
                        view.waitForInput();
                    }
                    break;
                case 2:
                    Card toDiscard = promptCardSelection(player);
                    if (toDiscard != null) {
                        player.getInventory().remove(toDiscard);
                        playerBonus += 3;
                        warriorBonusUsed++;
                        view.displayMessage("Warrior Rage! Discarded " + toDiscard.getName() + " for +3 Strength.");
                        view.waitForInput();
                    }
                    break;
            }
        }
    }

    private void handleMonsterDefeated(MonsterCard monster, Player player) {
        view.displayMessage("Congratulations! You have defeated the monster: " + monster.getName());
        view.displayMessage("You gain " + monster.getTreasure() + " treasures and " + monster.getLevelsGained() + " levels.");
        
        player.levelUp(monster.getLevelsGained());
        view.displayMessage(String.format("Player %s leveled up to level %d!", player.getName(), player.getLevel()));

        for (int i = 0; i < monster.getTreasure(); i++) {
            if (player.getInventory().size() >= player.getMaxCardsInHand()) {
                view.displayMessage("You cannot take more cards, your inventory is full.");
            } else {
                Card treasure = player.drawTreasureCard();
                player.getInventory().add(treasure);
                view.displayMessage("Player " + player.getName() + " took a treasure card: " + treasure.getName());
            }
        }
        view.waitForInput();
    }

    private void handlePlayerEscaped(MonsterCard monster, Player player) {
        view.displayMessage("You successfully escaped from the monster: " + monster.getName());
        view.waitForInput();
    }

    private void handlePlayerDefeated(MonsterCard monster, Player player) {
        view.displayMessage("Bad Stuff: " + monster.getNastyEffect());
        
        int lost = 0;
        Object levelsLostObj = monster.getLevelsLost();

        if (levelsLostObj instanceof Number) {
            lost = ((Number) levelsLostObj).intValue();
        } else if (levelsLostObj instanceof String) {
            String s = (String) levelsLostObj;
            if ("dynamic".equalsIgnoreCase(s)) {
                Random rand = new Random();
                lost = rand.nextInt(6) + 1;
                view.displayMessage("Rolling die for level loss... Result: " + lost);
            } else {
                try {
                    lost = Integer.parseInt(s);
                } catch (NumberFormatException e) {}
            }
        }

        if (lost > 0) {
            int oldLevel = player.getLevel();
            int newLevel = Math.max(1, oldLevel - lost);
            player.setLevel(newLevel);
            if (oldLevel > newLevel) {
                view.displayMessage("You lost " + (oldLevel - newLevel) + " levels. Current level: " + newLevel);
            } else {
                view.displayMessage("You are already at level 1, so you cannot lose more levels.");
            }
        }
        
        view.waitForInput();
    }

    private Card promptCardSelection(Player player) {
        List<Card> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            view.displayMessage("Inventory is empty.");
            return null;
        }
        
        String[] cardNames = new String[inventory.size() + 1];
        for (int i = 0; i < inventory.size(); i++) {
            Card c = inventory.get(i);
            String details = c.getDetails();
            cardNames[i] = c.getName() + " (Type: " + c.getClass().getSimpleName() + ")" + details;
        }
        cardNames[inventory.size()] = "Cancel";

        int selected = view.displayMenu(cardNames, "[" + player.getName() + "] Select a card:");
        
        if (selected == inventory.size()) return null;
        
        return inventory.get(selected);
    }

    private Card promptBonusCardSelection(Player player) {
        List<Card> inventory = player.getInventory();
        List<Card> bonusCards = new ArrayList<>();
        for (Card c : inventory) {
            if (c.getType() == CardType.BONUS) {
                bonusCards.add(c);
            }
        }

        if (bonusCards.isEmpty()) {
            view.displayMessage("You have no one-time bonus cards!");
            view.waitForInput();
            return null;
        }

        String[] cardNames = new String[bonusCards.size() + 1];
        for (int i = 0; i < bonusCards.size(); i++) {
            Card c = bonusCards.get(i);
            cardNames[i] = c.getName() + c.getDetails();
        }
        cardNames[bonusCards.size()] = "Cancel";

        int selected = view.displayMenu(cardNames, "[" + player.getName() + "] Select a bonus card to use:");
        
        if (selected == bonusCards.size()) return null;
        
        return bonusCards.get(selected);
    }
}
