package Ghreborn.model.players.packets;

import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Dialogue
 **/
public class Dialogue implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		final Boundary resourcearena = new Boundary(3182, 3942, 3184, 3944);
		if (!c.canUsePackets) {
			return;
		}
		handleDialogue(c);
	}
	public boolean handleDialogue(Client c) {
		if ((c.getDialogue() == null) || (c.getDialogue().getNext() == -1)) {
			c.getPA().removeAllWindows();
		} else if (c.getDialogue().getNext() > -1) {
			c.getDialogue().execute();
			return true;
		}
		return false;
	}
}
