package Ghreborn.model.players.packets;

import Ghreborn.Server;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;
import Ghreborn.world.GlobalDropsHandler;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		Server.itemHandler.reloadItems(c);
		Server.getGlobalObjects().updateRegionObjects(c);
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		GlobalDropsHandler.load(c);
		c.getMusic().updateRegionMusic(c.getRegionId());
		c.getPA().removeObjects();
		c.getPA().castleWarsObjects();
		//c.getMusic().updateRegionMusic(c.getRegionId());
		c.saveFile = true;

		if (c.skullTimer > 0) {
			c.isSkulled = true;
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}

	}

}
