package Ghreborn.model.players.packets;

import java.util.Objects;

import Ghreborn.Server;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.bank.BankItem;
import Ghreborn.model.multiplayer_session.MultiplayerSession;
import Ghreborn.model.multiplayer_session.trade.TradeSession;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int removeSlot = packet.getUnsignedShortA();
		int interfaceId = packet.getUnsignedShort();
		int removeId = packet.getUnsignedShortA();
		if (!c.canUsePackets) {
			return;
		}

		switch (interfaceId) {
		case 3900:
			if(c.myShopId == 250) {
				return;
			}
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case 5064:
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, c.getItems().getItemAmount(removeId), true);
			}
			break;

		case 5382:
			if (!c.isBanking) {
				return;
			}
			if(c.getBank().getBankSearch().isSearching()) {
        		c.getBank().getBankSearch().removeItem(removeId, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(removeId + 1)));
        		return;
        	}
			c.getItems().removeFromBank(removeId, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(removeId + 1)), true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession ) {
				session.addItem(c, new GameItem(removeId, c.getItems().getItemAmount(removeId)));
			}
			break;


		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, Integer.MAX_VALUE));
			}
			break;

		case 6669:

			break;

		}
	}

}
