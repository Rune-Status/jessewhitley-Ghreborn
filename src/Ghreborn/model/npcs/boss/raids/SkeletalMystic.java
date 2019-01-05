package Ghreborn.model.npcs.boss.raids;

import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class SkeletalMystic {
	
	public static boolean needRespawn = false;
	public static int respawnTimer = 0;
	public static int deathCount = 0;
	
	public static void rewardPlayers(Client player) {
		PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.SKELETAL_MYSTICS))
		.forEach(p -> {
			if (deathCount == 4) {
				int reward = p.getSkeletalMysticDamageCounter();
				p.sendMessage("@dre@You dealt " + p.getSkeletalMysticDamageCounter() + " damage toward skeletal mystics; granting " + reward + " raid points.");
				//p.getItems().addItemUnderAnyCircumstance(995, reward);
				p.setRaidPoints(p.getRaidPoints() + p.getSkeletalMysticDamageCounter());
				//DailyTasks.increase(p, PossibleTasks.SKELETAL_MYSTICS_RAID);
				
				p.setSkeletalMysticDamageCounter(0);
			}
		});
		
		if (deathCount == 4) {
			deathCount = 0;
			respawnTimer = 20;
			needRespawn = true;
		}
	}

}
