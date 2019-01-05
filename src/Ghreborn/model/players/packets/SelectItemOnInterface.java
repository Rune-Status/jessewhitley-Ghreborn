package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * @author Jason MacKeigan
 * @date Dec 29, 2014, 1:12:35 PM
 */
public class SelectItemOnInterface implements PacketType {

	@Override
	public void processPacket(Client player, Packet packet) {
		int interfaceId = packet.getInt();
		int slot = packet.getInt();
		int itemId = packet.getInt();
		int itemAmount = packet.getInt();
		if (!player.canUsePackets) {
			return;
		}
		switch (interfaceId) {
		}
	}

}