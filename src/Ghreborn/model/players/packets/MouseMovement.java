package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class MouseMovement implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		if (c.isIdle)
			c.isIdle = false;
	}
	

}
