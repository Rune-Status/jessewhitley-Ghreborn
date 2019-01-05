package Ghreborn.model.players.packets;

import Ghreborn.model.items.bank.BankItem;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class BankAllButOne implements PacketType {

	@Override
	public void processPacket(Client player, Packet packet) {
		int interfaceId = packet.getLEShortA();
		int itemId = packet.getLEShortA();
		int itemSlot = packet.getLEShort();
		if (!player.canUsePackets) {
			return;
		}
		//if (player.getInterfaceEvent().isActive()) {
			//player.sendMessage("Please finish what you're doing.");
			//return;
		//}
		switch(interfaceId) {
			case 5382:
				int amount = player.getBank().getCurrentBankTab().getItemAmount(new BankItem(itemId + 1));
				if(amount < 1)
					return;
				if(amount == 1) {
					player.sendMessage("Your bank only contains one of this item.");
					return;
				}
				if(player.getBank().getBankSearch().isSearching()) {
            		player.getBank().getBankSearch().removeItem(itemId, amount - 1);
            		return;
            	}
				if((player.getBank().getCurrentBankTab().getItemAmount(new BankItem(itemId + 1)) - 1) > 1)
					player.getItems().removeFromBank(itemId, amount - 1, true);
				break;
		}
	}

}

