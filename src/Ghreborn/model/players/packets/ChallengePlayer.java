package Ghreborn.model.players.packets;

import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		switch (packet.getOpcode()) {
		case 128:
			int answerPlayer = packet.getUnsignedShort();
			if (PlayerHandler.players[answerPlayer] == null) {
				return;
			}

			if (c.arenas() || c.duelStatus == 5) {
				c.sendMessage("You can't challenge inside the arena!");
				return;
			}

			//c.getTradeAndDuel().requestDuel(answerPlayer);
			break;
		}
	}
}
