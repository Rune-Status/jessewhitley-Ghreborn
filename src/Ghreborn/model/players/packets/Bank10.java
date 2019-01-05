package Ghreborn.model.players.packets;

import java.util.Objects;

import Ghreborn.Server;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.multiplayer_session.MultiplayerSession;
import Ghreborn.model.multiplayer_session.MultiplayerSessionFinalizeType;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.multiplayer_session.trade.TradeSession;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.skills.crafting.JewelryMaking;
import Ghreborn.net.Packet;

/**
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int interfaceId = packet.getLEShort();
		int removeId = packet.getUnsignedShortA();
		int removeSlot = packet.getUnsignedShortA();
		if (!c.canUsePackets) {
			return;
		}
		switch (interfaceId) {
		case 4233:
		case 4239:
		case 4245:
			JewelryMaking.mouldItem(c, removeId, 10);
			break;
		case 1688:
			c.getPA().useOperate(removeId);
			break;
		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 5);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 5);
			break;

		case 5064:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot do this whilst trading.");
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, 10, true);
			}
			break;

		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot do this whilst trading.");
				return;
			}
        	if(c.getBank().getBankSearch().isSearching()) {
        		c.getBank().getBankSearch().removeItem(removeId, 10);
        		return;
        	}
			c.getItems().removeFromBank(removeId, 10, true);
			break;


		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.addItem(c, new GameItem(removeId, 10));
			}
			break;




		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 5);
			break;
		}
	}

}
