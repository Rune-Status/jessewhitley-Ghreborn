package Ghreborn.model.players.packets;

import Ghreborn.Server;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

public class InputField implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int id = packet.getInt();
		String text = packet.getRS2String();
		//System.out.println("ID: " + id);
		switch (id) {
		case 58063:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (c.isBanking) {
				c.getBank().getBankSearch().setText(text);
				c.getBank().setLastSearch(System.currentTimeMillis());
				if (text.length() > 2) {
					c.getBank().getBankSearch().updateItems();
					c.getBank().setCurrentBankTab(c.getBank().getBankSearch().getTab());
					c.getItems().resetBank();
					c.getBank().getBankSearch().setSearching(true);
				} else {
					if (c.getBank().getBankSearch().isSearching())
						c.getBank().getBankSearch().reset();
					c.getBank().getBankSearch().setSearching(false);
				}
			}
			break;

/*		case 59507:
			if (player.getBankPin().getPinState() == BankPin.PinState.CREATE_NEW)
				player.getBankPin().create(text);
			else if (player.getBankPin().getPinState() == BankPin.PinState.UNLOCK)
				player.getBankPin().unlock(text);
			else if (player.getBankPin().getPinState() == BankPin.PinState.CANCEL_PIN)
				player.getBankPin().cancel(text);
			else if (player.getBankPin().getPinState() == BankPin.PinState.CANCEL_REQUEST)
				player.getBankPin().cancel(text);
			break;*/
		}
		
	}

}
