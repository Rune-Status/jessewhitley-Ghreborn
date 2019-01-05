package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Magic on items
 **/
public class MagicOnItems implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int slot = packet.getShort();
		int itemId = packet.getShortA();
		int junk = packet.getShort();
		int spellId = packet.getShortA();
		if (!c.canUsePackets) {
			return;
		}
		//System.out.println("Spell Id: " + spellId);
		c.usingMagic = true;
		c.getPA().magicOnItems(slot, itemId, spellId);
		c.usingMagic = false;

	}

}
