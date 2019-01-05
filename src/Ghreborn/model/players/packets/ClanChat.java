package Ghreborn.model.players.packets;

import Ghreborn.util.Misc;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.Server;
import Ghreborn.net.Packet;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		String textSent = Misc.longToPlayerName2(packet.getLong());
		textSent = textSent.replaceAll("_", " ");
		// c.sendMessage(textSent);
		//Server.clanChat.handleClanChat(c, textSent);
	}
}
