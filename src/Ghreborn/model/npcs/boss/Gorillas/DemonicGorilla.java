package Ghreborn.model.npcs.boss.Gorillas;

import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;

public class DemonicGorilla {

	public static boolean isDemonicGorilla(NPC npc) {
		switch (npc.npcType) {
		case 7151:
		case 7152:
		case 7153:
			return true;
		}
		return false;
	}
	
	public static void switchPrayer(NPC npc) {
		int stage = npc.HP / 10;
		if (stage == npc.stage) {
			return;
		}

		int transformId = npc.npcType + 1;
		if (transformId > 7153) {
			transformId = 7151;
		}

		npc.stage = stage;
		npc.requestTransform(transformId);
	}

	public static boolean isProtecting(Client player, NPC npc) {
		boolean protecting = false;
		
		switch (npc.npcType) {
		case 7151:
			protecting =  player.usingCross || player.usingBow || player.usingOtherRangeWeapons;
			break;
		case 7152:
			protecting = player.usingMagic;
			break;
		case 7153:
			protecting = !player.usingCross && !player.usingBow && !player.usingMagic && !player.usingOtherRangeWeapons;
			break;
		}
		
		return protecting;
	}

}