package Ghreborn.model.players.packets;

import java.util.Objects;

import Ghreborn.Config;
import Ghreborn.Connection;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;


/**
 * Private messaging, friends etc
 **/
public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215,
			CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 74, ADD_IGNORE = 133;

	@Override
	public void processPacket(Client c, Packet packet) {
		if (!c.canUsePackets) {
			return;
		}
		switch (packet.getOpcode()) {

		case ADD_FRIEND:
			c.getFriends().add(packet.getLong());
			break;
		case SEND_PM:
		//	if (System.currentTimeMillis() < c.muteEnd) {
	      //      c.sendMessage("You are muted for breaking a rule.");
		//		return;
		//	}
			//c.muteEnd = 0;
			final long recipient = packet.getLong();
			int pm_message_size = packet.getLength() - 8;	
			final byte pm_chat_message[] = new byte[pm_message_size];
			packet.readBytes(pm_chat_message, pm_message_size, 0);
			c.getFriends().sendPrivateMessage(recipient, pm_chat_message);
			if (Objects.nonNull(PlayerHandler.getPlayerByLongName(recipient)) && Objects.nonNull(c)) {
				//System.out.println(c.playerName + " PM: " + Misc.decodeMessage(pm_chat_message, pm_chat_message.length));
				//Server.getChatLogHandler().logMessage(c, "Private Message", PlayerHandler.getPlayerByLongName(recipient).playerName, 
						//Misc.decodeMessage(pm_chat_message, pm_chat_message.length));
			}
			break;

		case REMOVE_FRIEND:
			c.getFriends().remove(packet.getLong());
			PlayerSave.saveGame(c);
			break;

		case REMOVE_IGNORE:
			c.getIgnores().remove(packet.getLong());
			break;

		case CHANGE_PM_STATUS:
			packet.getUnsignedByte();
			c.setPrivateChat(packet.getUnsignedByte());
			packet.getUnsignedByte();
			c.getFriends().notifyFriendsOfUpdate();
			break;

		case ADD_IGNORE:
			c.getIgnores().add(packet.getLong());
			break;

		}

	}
}
