package Ghreborn.model.content.randomevents;

import Ghreborn.Server;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.util.Misc;

public class RandomEvent {
	
	public RandomEvent(Client player, int npcId) {
		final NPC npc = new NPC(npcId,	NPCHandler.npcs[npcId].npcType);
		Server.npcHandler.spawnNpc(player, npcId, player.absX, player.absY,
				player.heightLevel, 0, 120, 7, 70, 70, false, false);
		npc.forceChat(player.getRandomInterfaceClick().getEvents(npcId).cycleMessages()[0].replaceAll("%", Misc.formatPlayerName(player.getName())));
		//player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
		player.getRandomInterfaceClick().sendCycleAdvertisement(npc);

	}

	public static void resetEvents(Client player) {
		player.getRandomInterfaceClick().completed = false;
	}

	public static void destroyEventNpc(Client player) {
		if (player.getRandomEventNpc() != null && !player.getRandomEventNpc().isDead()) {
			//player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, player.getRandomEventNpc().getPosition(), 100 << 16);
			//NpcLoader.destroyNpc(player.getRandomEventNpc());
			player.setRandomEventNpc(null);
		}
	}

	public static void startRandomEvent(final Client player) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				if (player.getRandomEventNpc() != null /*|| player.cantTeleport()*/) {
					return;
				}
				int random = Misc.random(100);
				switch(random) {
					case 0 : //Wasp
						//player.getPjTimer().setWaitDuration(0);
						//player.getPjTimer().reset();
						//NPCHandler.spawnNpc(player, 411, true, false);
						break;
					case 1 :
						//TalkToEvent.spawnNpc(player, TalkToNpc.DRUNKEN_DWARF);
						break;
					case 2 :
						//TalkToEvent.spawnNpc(player, TalkToNpc.GENIE);
						break;
					case 3 :
						//TalkToEvent.spawnNpc(player, TalkToNpc.JEKYLL);
						break;
					case 4 :
						//TalkToEvent.spawnNpc(player, TalkToNpc.RICK);
						break;
				}
			}
			@Override
			public void stop() {
			}
		}, 1000);
	}

}
