package Ghreborn.model.npcs.boss.Kalphite;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;

public class Queen {
	
	Client c;

	public Queen(Client c) {
		this.c = c;
	}
	
	public static boolean changePhase = false;

	public static void Phase_Change(NPC npc) {
		if (npc != null) {
			npc.isDead = false;
			npc.requestTransform(6720);
			npc.gfx0(1055);
			Client.transforming = true;
			CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (container.getTotalTicks() == 1) {
						npc.HP += 255;
						npc.requestTransform(965);
						npc.animation(6270);
					} else if (container.getTotalTicks() == 11) {
						Client.transforming = false;
						changePhase = false;
						container.stop();
					}
				}
			}, 1);
		}
	}
	
	public static void graphic(NPC npc, Client player) {
		if (player == null) {
			return;
		}
		if(player.RANGE_ABILITY == true) {
			return;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.gfx0(281);
				container.stop();
			}

		}, 4);
	}
	
	public static boolean usingRange;
	
	public static void graphic1(NPC npc, Client player) {
		if (player == null) {
			return;
		}
		player.RANGE_ABILITY = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				npc.gfx0(278);
				container.stop();
			}
			
			public void stop() {
				player.RANGE_ABILITY = false;
			}

		}, 1);
	}
}
