package Ghreborn.model.players.packets;

import Ghreborn.Server;
import Ghreborn.model.multiplayer_session.MultiplayerSessionFinalizeType;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Move Items
 **/
public class MoveItems implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int interfaceId = packet.getLEShortA();//was getUnsignedShortA
		boolean insertMode = packet.getByteC() == 1;
		int from = packet.getLEShortA();//was getUnsignedShortA
		int to = packet.getLEShort();//was getUnsignedShort
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
	//	if (c.getInterfaceEvent().isActive()) {
		//	c.sendMessage("Please finish what you're doing.");
			//return;
		//}
		if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			c.sendMessage("You cannot move items whilst trading.");
			return;
		}
		c.getItems().moveItems(from, to, interfaceId, insertMode);
	}
}
