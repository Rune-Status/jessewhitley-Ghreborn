package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class OperateItem implements PacketType {

	@Override
	public void processPacket(Client c,Packet packet) {
		int slot = packet.getShortA(); // the row of the action
		int itemId = packet.getShortA(); //the item's id
		
		boolean hasItem = false;
		for(int i = 0; i < c.playerEquipment.length; i++) {
		    if(c.playerEquipment[i] == itemId && slot == i)
		        hasItem = true;
		}
		if(!hasItem)
		    return;
		switch(itemId) {
		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566: //Ring of dueling
			if(slot == 1) {
				System.out.println(" to Duel Arena");
			}
			if(slot == 2) {
				System.out.println(" to Castle Wars");
			}
			if(slot == 3) {
				System.out.println(" to Clan Wars");
			}
			break;
		default:
			if(c.getRights().isOwner()) {
				//c.sendMessage("Operate Item - itemId: " + itemId + " slot: " + slot + " int ID: " + intId);
			}
			break;
		}
	}
}
