package Ghreborn.model.players.packets;

import java.util.Objects;

import Ghreborn.Server;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.multiplayer_session.MultiplayerSession;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.multiplayer_session.trade.TradeSession;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Client c, Packet packet) {
		int Xamount = packet.getInt();
		if (Xamount < 0)// this should work fine
		{
			Xamount = c.getItems().getItemAmount(c.xRemoveId);
		}
		if (Xamount == 0) {
			Xamount = 1;
		}
		switch (c.xInterfaceId) {
		case 5064:
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			break;

		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
        		c.sendMessage("You cannot bank items whilst trading.");
        		return;
        	}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot).getId() - 1, Xamount);
				return;
			}
			if(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot) != null)
				c.getItems().removeFromBank(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot).getId() - 1, Xamount, true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.addItem(c, new GameItem(c.xRemoveId, Xamount));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, c.xRemoveSlot, new GameItem(c.xRemoveId, Xamount));
			}
			break;

		case 6669:
			//c.getTradeAndDuel().fromDuel(c.xRemoveId, c.xRemoveSlot, Xamount);
			break;
		}
		if (c.getPrayer().getAltarBone().isPresent()) {
			c.getPrayer().alter(Xamount);
			return;
		}
	}
}