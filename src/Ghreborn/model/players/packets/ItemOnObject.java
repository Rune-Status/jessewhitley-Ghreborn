package Ghreborn.model.players.packets;

import Ghreborn.model.content.fillables.Fillables;
import Ghreborn.model.content.fillables.sandtoBucket;

/**
 * @author Ryan / Lmctruck30
 */

import Ghreborn.model.items.UseItem;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.skills.cooking.Cooking;
import Ghreborn.model.players.skills.crafting.GlassMaking;
import Ghreborn.model.players.skills.farming.Farming;
import Ghreborn.net.Packet;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		/*
		 * a = ? b = ?
		 */

		int a = packet.getUnsignedShort();
		int objectId = packet.getLEShort();
		int objectY = packet.getLEShortA();
		int b = packet.getUnsignedShort();
		int objectX = packet.getLEShortA();
		int itemId = packet.getUnsignedShort();
		if (!c.canUsePackets) {
			return;
		}
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		c.face(objectX, objectY);
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		switch (objectId) {
		/*
		 * case ###: //Glory recharging if (itemId == 1710 || itemId == 1708 ||
		 * itemId == 1706 || itemId == 1704) { int amount =
		 * (c.getItems().getItemCount(1710) + c.getItems().getItemCount(1708) +
		 * c.getItems().getItemCount(1706) + c.getItems().getItemCount(1704));
		 * int[] glories = {1710, 1708, 1706, 1704}; for (int i : glories) {
		 * c.getItems().deleteItem(i, c.getItems().getItemCount(i)); }
		 * c.startAnimation(832); c.getItems().addItem(1712, amount); } break;
		 */
		case 12269:
		case 2732:
		case 3039:
		case 114:
		case 4488:
		case 25155:
		case 26181:
                    	c.face(objectX, objectY);
                    	Cooking.cookThisFood(c, itemId, objectId);
			break;
		case 884:
		case 879:
		case 873: 
		case 878:
		case 3264:
		case 3305:
		case 3359:
		case 4004:
		case 4005:
		case 6097:
		case 6249:
		case 6549:
		case 8927:
		case 12001:
		case 12897:
		case 8747:
		case 3485:
		case 14868:
		case 12974:
		case 7143:
		case 3646:
		case 5125:
		case 5598:
		case 8699:
		case 8702:
		case 8703:
		case 24004:
		case 24102:
		case 24150:
			Fillables.fillTheItem(c, itemId, objectId);
			break;
		case 14890:
			sandtoBucket.fillTheItem(c, itemId, objectId);
			break;
		case 24009:
			GlassMaking.MakeGlass(c, itemId, objectId);
			break;
		}
	}

}
