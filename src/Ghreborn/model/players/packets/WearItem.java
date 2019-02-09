package Ghreborn.model.players.packets;

import Ghreborn.Server;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.skills.runecrafting.Pouches;
import Ghreborn.model.players.skills.runecrafting.Pouches.Pouch;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;

/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		c.wearId = packet.getUnsignedShort();
		c.wearSlot = packet.getUnsignedShortA();
		c.interfaceId = packet.getUnsignedShortA();

		if (!c.getItems().playerHasItem(c.wearId, 1, c.wearSlot)) {
			return;
		}
		if ((c.playerIndex > 0 || c.npcIndex > 0) && c.wearId != 4153 && c.wearId != 12848 && !c.usingMagic && !c.usingBow && !c.usingOtherRangeWeapons && !c.usingCross/* && !c.usingBallista*/)
			c.getCombat().resetPlayerAttack();
		if (c.canChangeAppearance) {
			c.sendMessage("You can't wear an item while changing appearence.");
			return;
		}
		if(c.wearId == 295) {
			if(!c.getRights().isDonator() && !c.getRights().isStaff()) {
				c.sendMessage("this item is donator and staff only.", 255);
				return;
			}
				
		}
		switch (c.wearId) {
		}
		if(c.wearId == 5509) {
			Pouches.empty(c, Pouch.forId(c.wearId), c.wearId, 0);
			return;
		}
		if(c.wearId == 5510) {
			Pouches.empty(c, Pouch.forId(c.wearId), c.wearId, 1);
			return;
		}
		if(c.wearId == 5512) {
			Pouches.empty(c, Pouch.forId(c.wearId), c.wearId, 2);
			return;
		}
		if(c.wearId == 23821) {
			if(!c.playerName.equalsIgnoreCase("justin")) {
				c.sendMessage("Only Justin can wear this.");
				c.getItems().deleteItem(23821, 1);
				return;
			}
		}
		if (c.wearId == 4155) {
			if (!c.getSlayer().getTask().isPresent()) {
				c.sendMessage("You do not have a task!", 255);
				return;
			}
			c.sendMessage("I currently have " + c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName() + " to kill.", 255);
			//c.sendMessage("I currently have <col=0000FF>" + c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName() + "<col=000000> to kill.");
			c.getPA().closeAllWindows();
			return;
		}
		if(c.wearId == 7927) {
				c.resetWalkingQueue();
		for (int i = 0; i < 14; i++) {
			c.setSidebarInterface(i, 6014);
		}
		c.isMorphed = true;
		c.sendMessage("As you put on the ring you turn into an egg!");
		c.npcId2 = 5538 + Misc.random(5);
		c.isNpc = true;
		c.updateRequired = true;
		c.appearanceUpdateRequired = true;
		}
		if(c.wearId == 6583) {
			c.resetWalkingQueue();
	for (int i = 0; i < 14; i++) {
		c.setSidebarInterface(i, 6014);
	}
	c.isMorphed = true;
	c.sendMessage("As you put on the ring you turn into a rock!");
	c.npcId2 = 2188;
	c.isNpc = true;
	c.updateRequired = true;
	c.appearanceUpdateRequired = true;
	}
		if(c.wearId == 20017) {
			c.resetWalkingQueue();
	for (int i = 0; i < 14; i++) {
		c.setSidebarInterface(i, 6014);
	}
	c.isMorphed = true;
	//c.sendMessage("As you put on the ring you turn into a rock!");
	c.npcId2 = 7315;
	c.isNpc = true;
	c.updateRequired = true;
	c.appearanceUpdateRequired = true;
	}
		if(c.wearId == 20005) {
			c.resetWalkingQueue();
	for (int i = 0; i < 14; i++) {
		c.setSidebarInterface(i, 6014);
	}
	c.isMorphed = true;
	//c.sendMessage("As you put on the ring you turn into a rock!");
	c.npcId2 = 7314;
	c.isNpc = true;
	c.updateRequired = true;
	c.appearanceUpdateRequired = true;
	}
		// c.attackTimer = oldCombatTimer;
		if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			c.getItems().wearItem(c.wearId, c.wearSlot);
		}
	}

}
