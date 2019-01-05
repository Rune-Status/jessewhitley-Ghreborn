package Ghreborn.model.players.packets;

import Ghreborn.Server;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class ChangeRegion implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		c.getPA().removeObjects();
		Server.objectHandler.updateObjects(c);
		//new RegionMusic().playMusic(c);
		// Server.objectManager.loadObjects(c);
	}

}
