package Ghreborn.model.players.packets;

import Ghreborn.model.items.UseItem;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;
public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int usedWithSlot = packet.getUnsignedShort();
		int itemUsedSlot = packet.getUnsignedShortA();
		if(usedWithSlot >= c.playerItems.length || usedWithSlot < 0 || itemUsedSlot >= c.playerItems.length || itemUsedSlot < 0) {
			return;
		}
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		if (!c.getItems().playerHasItem(useWith, 1)
				|| !c.getItems().playerHasItem(itemUsed, 1)) {
			return;
		}

		if (!c.canUsePackets) {
			return;
		}
		UseItem.ItemonItem(c, itemUsed, useWith, itemUsedSlot, usedWithSlot);
	}

}
