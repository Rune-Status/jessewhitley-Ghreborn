package Ghreborn.model.players;

import Ghreborn.net.Packet;

public interface PacketType {
	public void processPacket(Client c, Packet packet);
}
