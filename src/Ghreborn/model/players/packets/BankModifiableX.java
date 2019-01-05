package Ghreborn.model.players.packets;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class BankModifiableX implements PacketType {


	public void processPacket(Client player, Packet packet) {
		int slot = packet.getUnsignedShortA();
		int component = packet.getUnsignedShort();
		int item = packet.getUnsignedShortA();
		int amount = packet.getInt();
		//if (player.getInterfaceEvent().isActive()) {
			//player.sendMessage("Please finish what you're doing.");
			//return;
		//}
		if(amount <= 0)
			return;
		switch(component) {
			case 5382:
            	if(player.getBank().getBankSearch().isSearching()) {
            		player.getBank().getBankSearch().removeItem(item, amount);
            		return;
            	}
                player.getItems().removeFromBank(item, amount, true);
                break;
		}
	}

}