package Ghreborn.model.players.packets;

import java.util.Objects;

import Ghreborn.Server;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.multiplayer_session.MultiplayerSession;
import Ghreborn.model.multiplayer_session.trade.TradeSession;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.skills.crafting.JewelryMaking;
import Ghreborn.net.Packet;

/**
 * Remove Item
 **/
public class RemoveItem implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int interfaceId = packet.getUnsignedShortA();
		int removeSlot = packet.getUnsignedShortA();
		int removeId = packet.getUnsignedShortA();
		switch (interfaceId) {
		case 4233:
		case 4239:
		case 4245:
			JewelryMaking.mouldItem(c, removeId, 1);
			break;
		case 1688:
			c.getItems().removeItem(removeId, removeSlot);
			break;

		case 5064:
			if (c.isBanking) {
				c.getItems().addToBank(removeId, 1, true);
			}
			break;

		case 5382:
        	if(c.getBank().getBankSearch().isSearching()) {
        		c.getBank().getBankSearch().removeItem(removeId, 1);
        		return;
        	}
            c.getItems().removeFromBank(removeId, 1, true);
			break;

		case 3900:
			c.getShops().buyFromShopPrice(removeId, removeSlot);
			break;

		case 3823:
			c.getShops().sellToShopPrice(removeId, removeSlot);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession /*|| session instanceof DuelSession*/) {
				session.addItem(c, new GameItem(removeId, 1));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, 1));
			}
			break;

		case 6669:
			//c.getTradeAndDuel().fromDuel(removeId, removeSlot, 1);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 1);
			break;
		}
	}

}
