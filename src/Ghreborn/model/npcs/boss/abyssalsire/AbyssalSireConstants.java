package Ghreborn.model.npcs.boss.abyssalsire;

import Ghreborn.model.Location;
import Ghreborn.model.players.Boundary;

public interface AbyssalSireConstants {

	Boundary BOUNDARY = new Boundary(2950, 4800, 3020, 4870, 0);
	Location INITIAL_LOCATION = new Location(2977, 4854, 0);

	int SLEEPING_NPC_ID = 5888;
	int MELEE_NPC_ID = 5890;
	int CHARGING_NPC_ID = 5891;
	int PORTAL_NPC_ID = 5908;

	int SLEEP_ANIMATION = 4527;
	int RISING_ANIMATION = 4528;
	int ATTACK_ANIMATION = 4530;
	int SPAWN_ANIMATION = 4530;
	int FUMES_ANIMATION = 4531;
	int PORTAL_ANIMATION = 4532;

	int DEATH_ANIMATION = 7100;

}
