package Ghreborn.model.players.packets.action;

import Ghreborn.Server;
import Ghreborn.model.content.clan.Clan;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;

public class JoinChat implements PacketType {

	@Override
	public void processPacket(Client player, Packet packet) {
		String owner = Misc.longToPlayerName2(packet.getLong())
				.replaceAll("_", " ");
		if (owner != null && owner.length() > 0) {
			if (player.clan == null) {
				/*if (player.inArdiCC) {
					return;
				}*/
				Clan clan = Server.clanManager.getClan(owner);
				if (clan != null) {
					clan.addMember(player);
				} else if (owner.equalsIgnoreCase(player.playerName)) {
					Server.clanManager.create(player);
				} else {
					player.sendMessage(Misc.formatPlayerName(owner)
							+ " has not created a clan yet.");
				}
				player.getPA().refreshSkill(21);
				player.getPA().refreshSkill(22);
				player.getPA().refreshSkill(23);
			}
		}
	}

}