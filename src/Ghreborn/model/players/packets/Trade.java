package Ghreborn.model.players.packets;

import java.util.Objects;

import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Trading
 */
public class Trade implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int tradeId = packet.getLEShort();
		Client requested = (Client) PlayerHandler.players[tradeId];
		c.getPA().resetFollow();
		if (!c.canUsePackets) {
			return;
		}
		if (!Config.ADMIN_ATTACKABLE && requested.getRights().isAdministrator()) {
			c.sendMessage("You cannot trade with " + Config.SERVER_NAME + " administrators!");
			return;
		}
		if(requested.getRights().isRainbow() && c.getRights().isRainbow()) {
			return;
		}

		if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			c.sendMessage("You cannot trade whilst inside the duel arena.");
			return;
		}
		if (Objects.equals(requested, c)) {
			c.sendMessage("You cannot trade yourself.");
			return;
		}

		if (c.ironman) {
			c.sendMessage("You cannot trade as an ironman!");
			return;
		}
		//if (c.getInterfaceEvent().isActive()) {
			//c.sendMessage("Please finish what you're doing.");
			//return;
		//}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		// if (Server.serverlistenerPort != 5555) {
		if (c.getTrade().requestable(requested)) {
			c.getTrade().request(requested);
			return;
		}
		if (tradeId < 1)
			return;

	}

}
