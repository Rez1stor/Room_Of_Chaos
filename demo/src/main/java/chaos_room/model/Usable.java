package chaos_room.model;

import chaos_room.view.GameView;

public interface Usable {
    boolean use(Player player, GameView view);
}
