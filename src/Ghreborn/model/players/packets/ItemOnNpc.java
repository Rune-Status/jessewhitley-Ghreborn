package Ghreborn.model.players.packets;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.items.UseItem;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int itemId = packet.getShortA();
		int i = packet.getShortA();
		int slot = packet.getLEShort();
		if(i >= NPCHandler.npcs.length || i < 0 || NPCHandler.npcs[i] == null) {
			return;
		}
		c.npcClickIndex = i;
		int npcId = NPCHandler.npcs[i].npcType;
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (!c.canUsePackets) {
			return;
		}

		if (c.goodDistance(NPCHandler.npcs[c.npcClickIndex].getX(), NPCHandler.npcs[c.npcClickIndex].getY(), c.getX(), c.getY(),
				NPCHandler.npcs[c.npcClickIndex].getSize())) {
			UseItem.ItemonNpc(c, itemId, npcId, slot);
		} else {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (NPCHandler.npcs[c.npcClickIndex] != null) {
						if (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.npcClickIndex].getX(), NPCHandler.npcs[c.npcClickIndex].getY(),
								NPCHandler.npcs[c.npcClickIndex].getSize())) {
							c.face(NPCHandler.npcs[c.npcClickIndex].getX(), NPCHandler.npcs[c.npcClickIndex].getY());
							NPCHandler.npcs[c.npcClickIndex].face(c);
							UseItem.ItemonNpc(c, itemId, npcId, slot);
							container.stop();
						}
					}
					if (c.clickNpcType == 0 || c.clickNpcType > 1)
						container.stop();
				}
			

				@Override
				public void stop() {
					c.clickNpcType = 0;
				}
			}, 1);
		}
	}
}
