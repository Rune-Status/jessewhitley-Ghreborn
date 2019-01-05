package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class Report implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		if (!c.canUsePackets) {
			return;
		}
		try {
			ReportHandler.handleReport(c, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}