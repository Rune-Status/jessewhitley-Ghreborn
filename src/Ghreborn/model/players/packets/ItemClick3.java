package Ghreborn.model.players.packets;


import Ghreborn.model.content.dialogue.impl.CombatBraceletDialogue;
import Ghreborn.model.content.dialogue.impl.GloryDialogue;
import Ghreborn.model.content.dialogue.impl.RingofDuelingDialogue;
import Ghreborn.model.content.dialogue.impl.SkillsNecklaceDialogue;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.Rights;
import Ghreborn.model.players.TeleportTablets;
import Ghreborn.model.players.combat.Degrade;
import Ghreborn.util.Misc;
import Ghreborn.net.Packet;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int itemId11 = packet.getLEShortA();
		int itemId1 = packet.getShortA();
		int itemId = packet.getShortA();
		c.setClickItem(itemId);
		if (!c.canUsePackets) {
			return;
		}
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		TeleportTablets.operate(c, itemId);

		switch (itemId) {
		case 12926:
			if (c.getToxicBlowpipeAmmo() == 0 || c.getToxicBlowpipeAmmoAmount() == 0) {
				c.sendMessage("You have no ammo in the pipe.");
				return;
			}
			if (c.getItems().addItem(c.getToxicBlowpipeAmmo(), c.getToxicBlowpipeAmmoAmount())) {
				c.setToxicBlowpipeAmmoAmount(0);
				c.sendMessage("You unload the pipe.");
			}
			break;
			case 11978:
			case 11976:
			case 1712:
			case 1710:
			case 1708:
			case 1706:
				c.start(new GloryDialogue());
			break;
			case 11968:
			case 11970:
			case 11105:
			case 11107:
			case 11109:
			case 11111:
				c.start(new SkillsNecklaceDialogue());
			break;
			case 11972:
			case 11974:
			case 11118:
			case 11120:
			case 11122:
			case 11124:
				c.start(new CombatBraceletDialogue());
			break;
			case 2552:
			case 2554:
			case 2556:
			case 2558:
			case 2560:
			case 2562:
			case 2564:
			case 2566:
				c.start(new RingofDuelingDialogue());
			break;
		case 12006:
			Degrade.checkRemaining(c, 12006);
		break;

		case 15340:
			Degrade.checkRemaining(c, 15340);
		break;
		
		case 15341:
			Degrade.checkRemaining(c, 15341);
		break;
		
		case 15342:
			Degrade.checkRemaining(c, 15342);
		break;
		
		case 15343:
			Degrade.checkRemaining(c, 15343);
		break;
		case 25343:
			Degrade.checkRemaining(c, 25343);
		break;
				case 25344:
			Degrade.checkRemaining(c, 25344);
		break;
				case 25345:
			Degrade.checkRemaining(c, 25345);
		break;
				case 25342:
			Degrade.checkRemaining(c, 25342);
		break;

		default:
			if (c.getRights().isOwner())
				Misc.println(c.playerName + " - Item3rdOption: " + itemId
						+ " : " + itemId11 + " : " + itemId1);
			break;
		}

	}

}
