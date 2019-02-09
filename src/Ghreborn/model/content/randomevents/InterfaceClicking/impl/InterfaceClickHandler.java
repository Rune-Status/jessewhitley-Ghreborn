package Ghreborn.model.content.randomevents.InterfaceClicking.impl;

import java.util.*;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.randomevents.EventsConstants;
import Ghreborn.model.content.randomevents.RandomEvent;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.util.Misc;

public class InterfaceClickHandler {

	private Client player;

	public InterfaceClickHandler(Client player) {
		this.player = player;
	}

	public boolean completed = false;
	public int randomNumber;
	public int stage = 0;

	private static final Map<Integer, InterfaceClickEvent> events = new HashMap<Integer, InterfaceClickEvent>();

	public InterfaceClickEvent getEvents(int npcId) {
		return events.get(npcId);
	}

	static {
		events.put(new SandwichLady(false).npcId(), new SandwichLady(false));
	}

	public void sendEventRandomly() {
		new RandomEvent(player, EventsConstants.interfaceClickNpcs[Misc.random(EventsConstants.interfaceClickNpcs.length - 1)]);
	}

	public void sendCycleAdvertisement(final NPC npc) {
		final InterfaceClickEvent interfaceClickEvent = getEvents(npc.index);

		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			int cycles = 0;
			@Override
			public void execute(CycleEventContainer container) {
				if (cycles == interfaceClickEvent.cycleMessages().length - 1) {
					sayGoodBye(npc, true);
					container.stop();
					return;
				}
				if (player.getRandomInterfaceClick().completed || !npc.isVisible()) {
					container.stop();
					return;
				}
				cycles++;
				npc.forceChat(interfaceClickEvent.cycleMessages()[cycles].replaceAll("%", player.getName()));

			}

			@Override
			public void stop() {
				RandomEvent.resetEvents(player);
			}
		}, interfaceClickEvent.cycleDuration());
	}

	/*public void sendModelsRotation(int npcId) {
		final InterfaceClickEvent interfaceClickEvent = getEvents(npcId);

		for (int model : interfaceClickEvent.modelToRotate())
			player.getActionSender().sendDialogueAnimation(model, 2715);

	}*/

	public void openInterface(int npcId) {
		InterfaceClickEvent interfaceClickEvent = getEvents(npcId);
		//player.setStatedInterface("" + npcId);
		player.getPA().showInterface(interfaceClickEvent.interfaceSent());
	}

	public void sayGoodBye(final NPC npc, final boolean failed) {
		final InterfaceClickEvent interfaceClickEvent = getEvents(npc.index);
		final int randNum = Misc.random(EventsConstants.remoteLocations.length - 1);
		player.getPA().closeAllWindows();
		if (!failed) {
			interfaceClickEvent.handleSuccess(player);
			npc.forceChat(interfaceClickEvent.goodByeMessage()[0]);
			npc.animation(EventsConstants.GOOD_BYE_EMOTE);
			player.getRandomInterfaceClick().completed = true;
			//player.getItems().addItem(interfaceClickEvent.rewards()[randomNumber]);
		} else {
			npc.sendPlayerAway(player, 402, 2304, EventsConstants.remoteLocations[randNum].getX(), EventsConstants.remoteLocations[randNum].getY(), EventsConstants.remoteLocations[randNum].getZ(), interfaceClickEvent.goodByeMessage()[1], false);
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				//player.getPA().createPlayersStillGfx(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
				container.stop();
			}
			@Override
			public void stop() {
				//NpcLoader.destroyNpc(npc);
			}
		}, 5);

	}

	public boolean handleButtonClicking(int buttonId) {
		if (player.getSpawnedNpc() == null)
			return false;
		InterfaceClickEvent interfaceClickEvent = player.getRandomInterfaceClick().getEvents(player.getSpawnedNpc().index);
		if (interfaceClickEvent == null)
			return false;
		if (interfaceClickEvent.buttonsDisplayed().contains(buttonId)) {
			if (buttonId == interfaceClickEvent.buttonsDisplayed().get(player.getRandomInterfaceClick().randomNumber))
				player.getRandomInterfaceClick().stage++;
			else {
				interfaceClickEvent.handleFailure(player);
			}
			if (player.getRandomInterfaceClick().stage == interfaceClickEvent.numberOfStages())
				player.getRandomInterfaceClick().sayGoodBye(player.getSpawnedNpc(), false);

		}
		return true;
	}

	
	
}
