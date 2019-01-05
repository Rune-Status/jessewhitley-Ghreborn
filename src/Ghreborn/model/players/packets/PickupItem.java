package Ghreborn.model.players.packets;

import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.items.GroundItem;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.Player;
import Ghreborn.net.Packet;
import Ghreborn.world.GlobalDropsHandler;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Client c, Packet packet) {
		c.walkingToItem = false;
		c.pItemY = packet.getLEShort();
		c.pItemId = packet.getUnsignedShort();
		c.pItemX = packet.getLEShort();
		if (Math.abs(c.getX() - c.pItemX) > 25
				|| Math.abs(c.getY() - c.pItemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
		GroundItem item = Server.itemHandler.getGroundItem(c.pItemId, c.pItemX, c.pItemY, c.heightLevel);
		if (item == null) {
			return;
		}
		if (c.getRights().isIronman()) {
			Player owner = PlayerHandler.getPlayer(item.getController());
			if (owner == null || !c.playerName.equalsIgnoreCase(item.getController())) {
				c.sendMessage("Your mode restricts you from picking up items that are not yours.");
				return;
			}
		}
		c.getCombat().resetPlayerAttack();
		if (c.getX() == c.pItemX && c.getY() == c.pItemY && c.getHeight() == c.heightLevel) {
			Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX,
					c.pItemY, c.heightLevel, true);
			GlobalDropsHandler.pickup(c, c.pItemId, c.pItemX, c.pItemY, c.heightLevel);
		} else {
			c.walkingToItem = true;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if(!c.walkingToItem)
						container.stop();
					if(c.getX() == c.pItemX && c.getY() == c.pItemY && c.getHeight() == c.heightLevel) {
						Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY,c.heightLevel, true);
						container.stop();
					}
				}
				@Override
				public void stop() {
					c.walkingToItem = false;
				}
			}, 1);
		}

	}

}
