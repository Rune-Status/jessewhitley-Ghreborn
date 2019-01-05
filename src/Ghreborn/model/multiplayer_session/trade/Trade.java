package Ghreborn.model.multiplayer_session.trade;

import java.util.Arrays;
import java.util.Objects;

import Ghreborn.Server;
import Ghreborn.model.multiplayer_session.Multiplayer;
import Ghreborn.model.multiplayer_session.MultiplayerSession;
import Ghreborn.model.multiplayer_session.MultiplayerSessionStage;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.players.Client;

public class Trade extends Multiplayer {

	public Trade(Client player) {
		super(player);
	}

	@Override
	public boolean requestable(Client requested) {
		if (requested == null) {
			player.sendMessage("The requested player cannot be found.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().requestAvailable(requested, player, MultiplayerSessionType.TRADE) != null) {
			player.sendMessage("You have already sent a request to this player.");
			return false;
		}
		if (player.getBH().hasTarget() && player.getBH().getTarget() != null && player.getBH().getTarget().getName() != null) {
			if (player.getBH().getTarget().getName().equalsIgnoreCase(requested.playerName)) {
				player.sendMessage("You cannot trade your bounty hunter target.");
				return false;
			}
		}
		if(player.getRights().isRainbow()) {
			player.sendMessage("As a rainbow donator You cant trade.");
			return false;
		}
		if (Objects.equals(player, requested)) {
			player.sendMessage("You cannot trade yourself.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot request a trade whilst in a session.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(requested)) {
			player.sendMessage("This player is currently is a session with another player.");
			return false;
		}
		if(requested.getRights().isIronman()) {
			player.sendMessage("You cannot trade an ironman!");
			return false;
		}
		if (player.teleTimer > 0 || requested.teleTimer > 0) {
			player.sendMessage("You cannot request or accept whilst you, or the other player are teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public void request(Client requested) {
		if (Objects.isNull(requested)) {
			player.sendMessage("The player cannot be found, try again shortly.");
			return;
		}
		if(player.getRights().isIronman()) {
			player.sendMessage("You cant trade as a ironman/ironwoman.");
			return;
		}
		if(player.getRights().isRainbow()) {
			return;
		}
		if (Objects.equals(player, requested)) {
			player.sendMessage("You cannot trade yourself.");
			return;
		}
		player.face(requested);
		MultiplayerSession session = Server.getMultiplayerSessionListener().requestAvailable(player, requested,
				MultiplayerSessionType.TRADE);
		if (session != null) {
			session.getStage().setStage(MultiplayerSessionStage.OFFER_ITEMS);
			session.populatePresetItems();
			session.updateMainComponent();
			Server.getMultiplayerSessionListener().removeOldRequests(player);
			Server.getMultiplayerSessionListener().removeOldRequests(requested);
			session.getStage().setAttachment(null);
		} else {
    		session = new TradeSession(Arrays.asList(player, requested), MultiplayerSessionType.TRADE);
    		if (Server.getMultiplayerSessionListener().appendable(session)) {
    			player.sendMessage("Sending trade offer...");
    			requested.sendMessage(player.playerName+":tradereq:");
     			session.getStage().setAttachment(player);
    			Server.getMultiplayerSessionListener().add(session);
    		}
		}
	}

}

