package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class ItemClick2OnGroundItem implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		final int itemX = packet.getLEShort();
		final int itemY = packet.getLEShortA();
		final int itemId = packet.getUnsignedShortA();
		if (!c.canUsePackets) {
			return;
		}
		//
	}
}