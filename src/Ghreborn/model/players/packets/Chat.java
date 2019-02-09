package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;
import Ghreborn.Connection;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		c.setChatTextEffects(packet.getByteS());
		c.setChatTextColor(packet.getByteS());
		c.setChatTextSize((byte) (packet.getLength() - 2));
		packet.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		if(Misc.textUnpack(c.getChatText(), (packet.getLength() - 2)).contains("Nigger") || Misc.textUnpack(c.getChatText(), (packet.getLength() - 2)).contains("nigger")){
			c.sendMessage("Watch your language.");
			return;
		}
		if (!c.canUsePackets) {
			return;
		}
		if (!Connection.isMuted(c))
			c.setChatTextUpdateRequired(true);
	}
	
}
