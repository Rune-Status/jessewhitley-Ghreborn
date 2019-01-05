package Ghreborn.model.players.packets;

import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.multiplayer_session.MultiplayerSession;
import Ghreborn.model.multiplayer_session.MultiplayerSessionFinalizeType;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.util.Misc;
import Ghreborn.net.Packet;

/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		if (!c.canUsePackets) {
			return;
		}
		if (c.tradeWith >= PlayerHandler.players.length || c.tradeWith < 0) {
			return;
		}
		MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.TRADE);
		if (session != null && Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			c.sendMessage("You have declined the trade.");
			session.getOther(c).sendMessage(c.playerName + " has declined the trade.");
			session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
	//	if (c.duelStatus == 6) {
		//	c.getTradeAndDuel().claimStakedItems();
		//}

	}

}
