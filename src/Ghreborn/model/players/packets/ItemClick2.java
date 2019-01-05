package Ghreborn.model.players.packets;

import Ghreborn.model.content.lootingbag.LootingBag;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.items.item_combinations.Godswords;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.Rights;
import Ghreborn.model.players.TeleportTablets;
import Ghreborn.model.players.skills.hunter.Implings;
import Ghreborn.model.players.skills.runecrafting.Pouches;
import Ghreborn.model.players.skills.runecrafting.Pouches.Pouch;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int itemId = packet.getShortA();
		if (!c.canUsePackets) {
			return;
		}
		
		if (!c.getItems().playerHasItem(itemId, 1))
			return;

		if(Implings.ImpRewards.impReward.containsKey(itemId)) {
			Implings.ImpRewards.getReward(c, itemId);
			return;
		}
		TeleportTablets.operate(c, itemId);
		ItemDefinition def = ItemDefinition.forId(itemId);
		switch (itemId) {
		case 5509:
			Pouches.check(c, Pouch.forId(itemId), itemId, 0);
			break;
		case 5510:
			Pouches.check(c, Pouch.forId(itemId), itemId, 1);
			break;
		case 5512:
			Pouches.check(c, Pouch.forId(itemId), itemId, 2);
			break;
		case 12926:
			def = ItemDefinition.forId(c.getToxicBlowpipeAmmo());
			c.sendMessage("The blowpipe has "+c.getToxicBlowpipeAmmoAmount()+" darts and " + c.getToxicBlowpipeCharge() + " charge remaining.");
			break;
		case 11802:
		case 11804:
		case 11806:
		case 11808:
			Godswords.dismantle(c,itemId);
			break;
		default:
			if (c.getRights().isOwner())
				Misc.println(c.playerName + " - Item3rdOption: " + itemId);
			break;
		}

	}

}
