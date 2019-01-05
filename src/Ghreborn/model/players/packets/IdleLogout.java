package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.skills.Skilling;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;

public class IdleLogout implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		if (!c.isIdle) {
			if (c.debugMessage)
				c.sendMessage("You are now in idle mode.");
			c.isIdle = true;
		}
/*		if(!c.getRights().isAdministrator() || !c.getRights().isOwner() || !c.getRights().isCoOwner() && !c.getRights().isDonator()) {
		if (c.underAttackBy > 0 || c.underAttackBy2 > 0 || c.getSkilling().isSkilling()) {
			return;
		} else {
			c.logout();
			Misc.println(c.playerName + " is idle, kicked.");
		}
	}*/
	}
}