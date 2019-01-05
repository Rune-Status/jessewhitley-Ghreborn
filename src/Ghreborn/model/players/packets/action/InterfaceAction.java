package Ghreborn.model.players.packets.action;

import java.util.Optional;

import Ghreborn.core.PlayerHandler;
import Ghreborn.model.content.clan.Clan;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;

public class InterfaceAction implements PacketType {

	@Override
	public void processPacket(Client player, Packet packet) {
		int id = packet.getUnsignedShort();
		int action = packet.getUnsignedShort();
		switch (id) {
		case 18304:
			if (action == 1) {
				player.getPA().getClan().delete();
				player.getPA().setClanData();
			}
			break;
		case 18307:
		case 18310:
		case 18313:
		case 18316:
			Clan clan = player.getPA().getClan();
			if (clan != null) {
				if (id == 18307) {
					clan.setRankCanJoin(action == 0 ? -1 : action);
				} else if (id == 18310) {
					clan.setRankCanTalk(action == 0 ? -1 : action);
				} else if (id == 18313) {
					clan.setRankCanKick(action == 0 ? -1 : action);
				} else if (id == 18316) {
					clan.setRankCanBan(action == 0 ? -1 : action);
				}
				String title = "";
				if (id == 18307) {
					title = clan.getRankTitle(clan.whoCanJoin)
							+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18310) {
					title = clan.getRankTitle(clan.whoCanTalk)
							+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18313) {
					title = clan.getRankTitle(clan.whoCanKick)
							+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18316) {
					title = clan.getRankTitle(clan.whoCanBan)
							+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
				}
				player.getPA().sendString(title, id + 2);
			}
			break;

		default:
			// System.out.println("Interface action: [id=" + id +",action=" +
			// action +"]");
			break;
		}
		if (id >= 18323 && id < 18423) {
			Clan clan = player.getPA().getClan();
			if (clan != null && clan.rankedMembers != null && !clan.rankedMembers.isEmpty()) {
				String member = clan.rankedMembers.get(id - 18323);
				switch (action) {
				case 0:
					clan.demote(member);
					break;
				default:
					clan.setRank(member, action);
					break;
				}
				player.getPA().setClanData();
			}
		}
		if (id >= 18424 && id < 18524) {
			Clan clan = player.getPA().getClan();
			if (clan != null && clan.bannedMembers != null && !clan.bannedMembers.isEmpty()) {
				String member = clan.bannedMembers.get(id - 18424);
				switch (action) {
				case 0:
					clan.unbanMember(member);
					break;
				}
				player.getPA().setClanData();
			}
		}
		if (player.lastSent > 0) {
			player.lastSent = 0;
			return;
		}
		if (id >= 18144 && id < 18244) {
			for (int index = 0; index < 100; index++) {
				if (id == index + 18144) {
					String member = player.clan.activeMembers.get(id - 18144);
					player.lastSent++;
					switch (action) {
					case 0:
						if (player.clan.isFounder(player.playerName)) {
							player.getPA().showInterface(18300);
						}
						break;
					case 1:
						if (member.equalsIgnoreCase(player.playerName)) {
							player.sendMessage("You can't kick yourself!");
						} else {
							if (player.clan.canKick(player.playerName)) {
								player.clan.kickMember(member);
							} else {
								player.sendMessage("You do not have sufficient privileges to do this.");
							}
						}
						break;
					case 2:
						if (member.length() == 0) {
							break;
						} else if (member.length() > 12) {
							member = member.substring(0, 12);
						}
						if (member.equalsIgnoreCase(player.playerName)) {
							break;
						}
						if (!PlayerSave.playerExists(member)) {
							player.sendMessage("This player doesn't exist!");
							break;
						}
						Clan clan = player.getPA().getClan();
						if (clan.isRanked(member)) {
							try {
								player.sendMessage("You cannot ban a ranked member.");
								break;
							} catch (Exception e) {
							}
						}
						if (clan != null) {
							clan.banMember(Misc.formatPlayerName(member));
							player.getPA().setClanData();
							clan.save();
						}
						break;
					case 3:
						player.getFriends().add(packet.getLong());
						break;
					case 4:
						player.getIgnores().add(packet.getLong());
						break;
					case 5:
						player.memberName = "";
						Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(member);
						if (optionalPlayer.isPresent()) {
							Player c2 = optionalPlayer.get();
							if (c2.playerName == player.playerName) {
								return;
							}
							if (System.currentTimeMillis() - player.lastClanTeleport < 120_000) {
								player.sendMessage("You can only use this once every 2 minutes.");
								return;
							}

							if (!player.clan.getTeleport()) {
								player.sendMessage("The clan founder has not enabled the use of this ability.");
								return;
							}

							if (!c2.inWild()) {
								player.sendMessage("The player your teleporting to must be in the wilderness.");
								return;
							}
							if (!player.inWild()) {
								player.sendMessage("You must be in atleast @blu@1@bla@ wilderness to use this.");
								return;
							}
							if (player.playerIndex > 0 || player.npcIndex > 0) {
								player.sendMessage("You cannot teleport whilst in combat.");
								return;
							}
							if (player.wildLevel > 20) {
								player.sendMessage("You can't use this above level @blu@20@bla@ wilderness.");
								return;
							}

/*							if (c2.hungerGames) {
								player.sendMessage("You can't use this in the hunger games.");
								return;
							}*/
/*							if (player.dialogueAction != 6669) {
								player.getDH().sendPlayerChat2(
										"@bla@This player is in @blu@(" + c2.wildLevel
												+ ")@bla@ wilderness are you sure?",
										"@red@This is not a safe location and you can lose items!", 596);
								player.nextChat = 6670;
								player.memberName = "" + c2.playerName + "";
								return;
							}*/
						}
						break;
					case 6:
						if (!player.clan.getCanCopy()) {
							player.sendMessage("The clan founder has not enabled the use of this ability.");
							return;
						}

						if (player.getItems().freeSlots() < 28 || player.getItems().isWearingItems()) {
							player.sendMessage("You must bank all of your items before copying another clan member!");
							return;
						}
						if (player.inWild()) {
							player.sendMessage("You cannot copy a clan member whilst in the wilderness");
							return;
						}
						/*int[] equipment = new int[14];
						Optional<Player> otherPlayer = PlayerHandler.getOptionalPlayer(member);
						if (otherPlayer.isPresent()) {
							Player c2 = otherPlayer.get();
							if (c2.playerName == player.playerName) {
								return;
							}
							for (int i = 0; i < c2.playerEquipment.length; i++) {
								for (String name : Unspawnable.names) {
								//if (c2.playerEquipment[i] == c2.getItems().getItemIdContains(Unspawnable.canSpawn(c2.playerEquipment[i]))) {
									if (c2.playerEquipment[i] == c2.getItems().getItemIdContains(name)) {
									player.sendMessage("You do not have a "
											+ player.getItems().getItemName(c2.playerEquipment[c2.playerWeapon])
											+ " to equip.");
									continue;
								}}
								if (!Requirement.canEquip(player, c2.playerEquipment[i]))
									continue;
								equipment[i] = c2.playerEquipment[i];
								player.playerEquipment[i] = c2.playerEquipment[i];
								player.getItems().wearItem(equipment[i], 1, i);
								player.playerItems = c2.playerItems;
								player.playerItemsN = c2.playerItemsN;
								c2.playerItems = player.playerItems;
								c2.playerItemsN = player.playerItemsN;
								player.getItems().resetItems(3214);
								int[] skillIds = { 0, 1, 2, 3, 4, 5, 6

								};
								for (int xp : skillIds) {
									player.playerXP[xp] = 1;
									player.playerLevel[xp] = 1;
									player.playerLevel[xp] = c2.playerLevel[xp];
									player.getPA().addSkillXP10(c2.playerXP[xp], xp);
									player.getPA().refreshSkill(i);
								}
							}
						}*/
						player.sendMessage("Currently disabled.");
						break;
					}
					break;
				}
			}
		}
	}
}