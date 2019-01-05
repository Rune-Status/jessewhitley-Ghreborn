package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.Rights;
import Ghreborn.util.Misc;
import Ghreborn.*;
import Ghreborn.net.Packet;

public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int a1 = packet.getLEShort();
		int itemUsed = packet.getShortA();
		int groundItem = packet.getUnsignedShort();
		int gItemY = packet.getShortA();
		int itemUsedSlot = packet.getLEShortA();
		int gItemX = packet.getUnsignedShort();
		if (!c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
			return;
		}
		if (!Server.itemHandler.itemExists(groundItem, gItemX, gItemY, c.heightLevel)) {
			return;
		}

		switch (itemUsed) {

		default:
			if (c.rights == Rights.OWNER)
				Misc.println("ItemUsed " + itemUsed + " on Ground Item "
						+ groundItem);
			break;
		}
	}

}
